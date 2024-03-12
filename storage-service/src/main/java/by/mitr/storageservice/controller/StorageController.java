package by.mitr.storageservice.controller;

import by.mitr.storageservice.model.Storage;
import by.mitr.storageservice.service.StorageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/storages")
@AllArgsConstructor
public class StorageController {
    private StorageService storageService;

    @PostMapping
    public ResponseEntity<?> createStorage(@Valid @RequestBody Storage storage) {
        log.info("Request to create Storage {}", storage);
        Map<String, Long> storageId = storageService.create(storage);
        log.info("Storage was created with {}", storageId);
        return ResponseEntity.ok(storageId);
    }


    @GetMapping
    public ResponseEntity<?> getStorages() {

        List<Storage> allStorages = storageService.getAllStorages();
        log.info("Request to get all Storages {}", allStorages);
        return ResponseEntity.ok(allStorages);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteStorages(@RequestParam("id") List<Long> ids) {
        log.info("Request to delete Storages with ids {}", ids);
        Object deleteStoragesByIds = storageService.deleteStoragesByIds(ids);
        log.info("Storage was deleted with ids {}", deleteStoragesByIds);
        return ResponseEntity.ok(deleteStoragesByIds);
    }
}
