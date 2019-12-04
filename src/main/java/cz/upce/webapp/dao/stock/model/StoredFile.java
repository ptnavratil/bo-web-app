package cz.upce.webapp.dao.stock.model;

import javax.persistence.*;
import java.sql.Blob;

@Entity
public class StoredFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Integer id;

    @Column
    private String contentType;

    @Lob
    private byte[] data;

    @Column
    private String fileName;

    public StoredFile() {
    }

    public StoredFile(String contentType, byte[] data, String fileName) {
        this.contentType = contentType;
        this.data = data;
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
