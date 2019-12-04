package cz.upce.webapp.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import com.google.common.net.HttpHeaders;
import cz.upce.webapp.controller.dto.PriceListDTO;
import cz.upce.webapp.controller.dto.SupplierItemsDTO;
import cz.upce.webapp.dao.stock.StoredFileStorageService;
import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.StoredFile;
import cz.upce.webapp.dao.stock.model.Supplier;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import cz.upce.webapp.dao.stock.repository.StoredFileRepository;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import cz.upce.webapp.service.CartServiceImpl;
import cz.upce.webapp.service.ProcessorService;
import cz.upce.webapp.utils.xlsprocessors.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cz.upce.webapp.service.ItemServiceImpl;
import cz.upce.webapp.utils.FileManipulator;

/**
 * Class used to importItemsFromFile the .xls or .xlsx file
 *
 * @author Tomas Kodym
 */
@Controller
public class ImportController
{
    private static final String UPLOADING_DIR = System.getProperty("user.dir") + "/uploadingDir/";

    private static final Logger LOGGER = LoggerFactory.getLogger(ImportController.class);

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @Autowired
    CartServiceImpl cartService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private StoredFileRepository storedFileRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private StoredFileStorageService storedFileService;

    @Autowired
    private ProcessorService processorService;

    @PostMapping("/upload")
    public String singleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("supplierId") Integer supplierId, RedirectAttributes redirectAttributes)
    {
        if (file.isEmpty())
        {
            redirectAttributes.addFlashAttribute("message", "No file for import was specified!");
            LOGGER.warn("No file for import was specified!");
            return "redirect:/notifier";
        }

        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(".xls") && !file.getOriginalFilename().contains(".xlsx"))
        {
            redirectAttributes.addFlashAttribute("message", "File with content type: " + file.getContentType() + "is not supported!");
            LOGGER.warn("File with content type: " + file.getContentType() + "is not supported!");
            return "redirect:/notifier";
        }

        try
        {
            //before import, remove all previous data according to the supplier
            itemServiceImpl.deleteAllBySupplier(supplierId);


            Supplier s = supplierRepository.getOne(supplierId);

            FileManipulator.saveFile(file, UPLOADING_DIR);
            redirectAttributes.addFlashAttribute("message", "File was successfully uploaded!");
            //select processor aligned with combobox value
            ISheetProcessor sheetProcessor = processorService.selectProcessor(supplierId);

            sheetProcessor.importItemsFromFile(file, UPLOADING_DIR, itemRepository);

            storedFileService.storeFile(file);

            Path filePath = FileManipulator.getFilePath(file, UPLOADING_DIR);
            StoredFile storedFile = new StoredFile(file.getContentType(), FileUtils.readFileToByteArray(filePath.toFile()), file.getOriginalFilename());
            storedFileRepository.save(storedFile);

            if (s.getPricelist()!=null) {
                Integer priceListId = s.getPricelist().getId();
                if (priceListId !=null) {
                    storedFileRepository.deleteById(priceListId);
                }
            }
            s.setPricelist(storedFile);
            s.setPricelistName(file.getOriginalFilename());
            supplierRepository.save(s);

            FileManipulator.deleteFile(file, UPLOADING_DIR);


        }
        catch (IOException e)
        {
            redirectAttributes.addFlashAttribute("message", "There was an error during the file import!");
            LOGGER.error("File: " + file.getName() + " could not been save to local file system because of: ", e);
        }

        return "redirect:/notifier";
    }

    @GetMapping("/notifier")
    public String uploadStatus()
    {
        return "importer/notifier";
    }



    @GetMapping("/download/{supplierId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer supplierId) {
        // Load file from database

        StoredFile dbFile = storedFileService.getFile(supplierRepository.getOne(supplierId).getPricelist().getId());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(dbFile.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dbFile.getFileName() + "\"")
                .body(new ByteArrayResource(dbFile.getData()));
    }
    @GetMapping("/download-filled/{supplierId}")
    public ResponseEntity<Resource> downloadFilledFile(@PathVariable Integer supplierId) {

        try {
            PriceListDTO priceListDTO = processorService.getFilledPriceListWithOrder(supplierId);
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(priceListDTO.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + priceListDTO.getFilename() + "\"")
                .body(new ByteArrayResource(priceListDTO.getContent()));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write file", e);
        }

    }

}
