package by.mitrakhovich.resourceservice.service;

import brave.Tracing;
import by.mitrakhovich.resourceservice.dal.entity.StorageType;
import by.mitrakhovich.resourceservice.model.Storage;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageService {
    @Value("${user.storage-service}")
    private String storageServiceUrl;

    @Autowired
    @Qualifier("defaultStorages")
    private List<Storage> defaultStorages;

    @Autowired
    Tracing tracing;
    @Autowired
    private RestTemplate restTemplate;

    public Storage getStagingStorage(List<Storage> storages) {
        return getStorage(storages, StorageType.STAGING);
    }

    public Storage getPermanentStorage(List<Storage> storages) {
        return getStorage(storages, StorageType.PERMANENT);
    }

    private Storage getStorage(List<Storage> storages, StorageType storageType) {
        if (storages != null && !storages.isEmpty()) {
            return storages.stream().filter(storage -> storage.getStorageType().equals(storageType))
                    .findFirst().orElseThrow(() -> new RuntimeException("Do not have " + storageType + " storage"));
        } else {
            throw new RuntimeException("Do not have storages");
        }
    }

    @CircuitBreaker(name = "circuit", fallbackMethod = "getStoragesFailed")
    public List<Storage> getStorages() {
//        log.info("!!parent tracing id {}", tracing.tracer().currentSpan().context().parentIdString());
        Storage[] storages = restTemplate.getForObject(storageServiceUrl, Storage[].class);
        log.info("get Storages from StorageService {}", Arrays.toString(storages));
        return storages != null ? Arrays.asList(storages) : Collections.emptyList();
    }

    public List<Storage> getStoragesFailed(Exception e) {
        log.info("get Default Storages {}", defaultStorages);
        return defaultStorages;
    }

    private Storage getStorageFromDefault(StorageType storageType) {
        return defaultStorages.stream().filter(storage -> storage.getStorageType().equals(storageType))
                .findFirst().orElseThrow(() -> new RuntimeException("Not exist storage with type " + storageType.name()));
    }
}
