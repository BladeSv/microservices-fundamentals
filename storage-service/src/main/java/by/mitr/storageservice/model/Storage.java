package by.mitr.storageservice.model;

import by.mitr.storageservice.dal.entity.StorageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Storage {
    Long id;
    @NotNull
    StorageType storageType;
    @NotBlank
    String bucket;
    @NotBlank
    String path;
}
