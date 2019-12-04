package cz.upce.webapp.dao.stock;

import cz.upce.webapp.dao.stock.model.StoredFile;
import cz.upce.webapp.dao.stock.repository.StoredFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class StoredFileStorageService {

    @Autowired
    private StoredFileRepository dbFileRepository;

    public StoredFile storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new IllegalStateException("Sorry ! Filename contains invalid path sequence " + fileName);
            }

            StoredFile dbFile = new StoredFile(file.getContentType(), file.getBytes(), fileName);

            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public StoredFile getFile(Integer fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalStateException("File not found with id " + fileId));
    }
}