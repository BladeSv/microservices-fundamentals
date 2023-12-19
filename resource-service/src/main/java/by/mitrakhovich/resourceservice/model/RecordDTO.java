package by.mitrakhovich.resourceservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO {
    private InputStream resource;
    private String fileName;
    private String contentType;
    private Long contentLength;
}
