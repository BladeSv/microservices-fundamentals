package by.mitr.storageservice.service;

import by.mitr.storageservice.dal.StorageRepository;
import by.mitr.storageservice.mapper.StorageMapper;
import by.mitr.storageservice.model.Storage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@AllArgsConstructor
public class StorageService {
    private StorageRepository storageRepository;
    private StorageMapper storageMapper;

    public Map<String, Long> create(Storage storage) {
        var id = storageRepository.save(storageMapper.toStorageDao(storage)).getId();
        log.info("Storage {} was created with id {}", storage, id);
        return Map.of("id", id);
    }

    public List<Storage> getAllStorages() {
        var storages = StreamSupport.stream(storageRepository.findAll().spliterator(), false)
                .map(storageMapper::toStorage)
                .toList();
        log.info("Get storages {}", storages);
        return storages;
    }

    public Object deleteStoragesByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return Map.of("ids", Collections.emptyList());

        List<Long> removedIds = ids.stream().map(id -> {
            if (storageRepository.existsById(id)) {
                storageRepository.deleteById(id);
                return id;
            }
            return null;
        }).filter(Objects::nonNull).toList();

        return Map.of("ids", removedIds);
    }
}
