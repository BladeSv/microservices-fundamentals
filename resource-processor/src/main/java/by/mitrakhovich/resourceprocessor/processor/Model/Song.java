package by.mitrakhovich.resourceprocessor.processor.Model;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class Song {
    private String name;
    private String artist;
    private String album;
    private String length;
    private String resourceId;
    private String year;
}
