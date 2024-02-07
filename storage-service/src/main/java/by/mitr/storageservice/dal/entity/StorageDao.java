package by.mitr.storageservice.dal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    StorageType storageType;
    String bucket;
    String path;
}
