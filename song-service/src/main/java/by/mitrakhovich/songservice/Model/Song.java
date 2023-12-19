package by.mitrakhovich.songservice.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Song {
    @NotBlank
    private String name;
    @NotBlank
    private String artist;
    @NotBlank
    private String album;
    @NotBlank
    private String length;
    @Pattern(regexp = "^\\d+$")
    private String resourceId;
    @Pattern(regexp = "^\\d{4}$")
    private String year;
}
