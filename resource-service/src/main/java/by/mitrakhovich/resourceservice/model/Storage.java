package by.mitrakhovich.resourceservice.model;


import by.mitrakhovich.resourceservice.dal.entity.StorageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Storage {
    Long id;
    StorageType storageType;
    String bucket;
    String path;
}
