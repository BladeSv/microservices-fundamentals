package by.mitrakhovich.resourceprocessor.processor.Model;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    private String name;
    private String artist;
    private String album;
    private String length;
    private String resourceId;
    private String year;
}
