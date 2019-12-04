package cz.upce.webapp.service;

import cz.upce.webapp.controller.dto.PriceListDTO;
import cz.upce.webapp.controller.dto.SupplierItemsDTO;
import cz.upce.webapp.dao.stock.StoredFileStorageService;
import cz.upce.webapp.dao.stock.model.Item;
import cz.upce.webapp.dao.stock.model.StoredFile;
import cz.upce.webapp.dao.stock.repository.ItemRepository;
import cz.upce.webapp.dao.stock.repository.StoredFileRepository;
import cz.upce.webapp.dao.stock.repository.SupplierRepository;
import cz.upce.webapp.utils.xlsprocessors.AbstractSheetProcessor;
import cz.upce.webapp.utils.xlsprocessors.ISheetProcessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ProcessorService {

    private static final String UPLOADING_DIR = System.getProperty("user.dir") + "/uploadingDir/";

    @Autowired
    private ItemServiceImpl itemServiceImpl;

    @Autowired
    CartServiceImpl cartService;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private StoredFileStorageService storedFileService;


    @Autowired
    List<AbstractSheetProcessor> processors;

    public ISheetProcessor selectProcessor(Integer supplierId)
    {
        for (AbstractSheetProcessor processor : processors) {
            if (processor.supplerId().equals(supplierId)) {
                return processor;
            }
        }
        // Lets select first one
        return processors.get(0);
    }

    public PriceListDTO getFilledPriceListWithOrder(@PathVariable Integer supplierId) throws IOException {
        // Load file from database
        StoredFile dbFile = storedFileService.getFile(supplierRepository.getOne(supplierId).getPricelist().getId());
        String contentType = dbFile.getContentType();

        String filename = UPLOADING_DIR  + RandomStringUtils.randomAlphabetic(8)+"-" + dbFile.getFileName();
        FileUtils.writeByteArrayToFile(new File(filename), dbFile.getData());

        Map<Integer, SupplierItemsDTO> cartItems = cartService.getItemsInCart();
        Map<Item, Integer> orderedItems = cartItems.get(supplierId);
        ISheetProcessor sheetProcessor = selectProcessor(supplierId);
        Workbook workbook = sheetProcessor.fillOrder(new File(filename), orderedItems);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            workbook.write(bos);
        } finally {
            bos.close();
        }
        FileUtils.deleteQuietly(new File(filename));
        return new PriceListDTO(bos.toByteArray(), dbFile.getFileName(), dbFile.getContentType());
    }
}
