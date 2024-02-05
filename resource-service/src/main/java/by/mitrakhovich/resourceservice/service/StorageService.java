package by.mitrakhovich.resourceservice.service;

import by.mitrakhovich.resourceservice.dal.entity.StorageType;
import by.mitrakhovich.resourceservice.model.Storage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageService {
    @Value("${user.storage-service}")
    private String storageServiceUrl;

    //    @Value("#{${application.defaultStorages}}")
    @Autowired
    @Qualifier("defaultStorages")
    private List<Storage> defaultStorages;

    @Autowired
    private RestTemplate restTemplate;

    public Storage getStagingStorage() {
        return getStorage(StorageType.STAGING);
    }

    public Storage getPermanentStorage() {
        return getStorage(StorageType.PERMANENT);
    }

    private Storage getStorage(StorageType storageType) {
        var storages = restTemplate.getForObject(storageServiceUrl, Storage[].class);
        if (storages != null && storages.length > 0) {
            return Arrays.stream(storages).filter(storage -> storage.getStorageType().equals(storageType))
                    .findFirst().orElseGet(() -> getStorageFromDefault(storageType));
        } else {
            return getStorageFromDefault(storageType);
        }
    }

    private Storage getStorageFromDefault(StorageType storageType) {
        return defaultStorages.stream().filter(storage -> storage.getStorageType().equals(storageType))
                .findFirst().orElseThrow(() -> new RuntimeException("Not exist storage with type " + storageType.name()));
    }

}
