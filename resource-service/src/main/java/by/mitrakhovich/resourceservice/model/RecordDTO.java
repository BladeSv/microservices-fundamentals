package by.mitrakhovich.resourceservice.model;

import java.io.InputStream;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class RecordDTO {
    private InputStream resource;
    private String fileName;
    private String contentType;
    private Long contentLength;

    public RecordDTO() {
    }

    public RecordDTO(InputStream resource, String fileName, String contentType, Long contentLength) {
        this.resource = resource;
        this.fileName = fileName;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public InputStream getResource() {
        return resource;
    }

    public void setResource(InputStream resource) {
        this.resource = resource;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }
}

