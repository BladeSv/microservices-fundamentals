package by.mitr.storageservice.controller;

import by.mitr.storageservice.model.Storage;
import by.mitr.storageservice.service.StorageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/storages")
@AllArgsConstructor
public class StorageController {
    private StorageService storageService;

    @PostMapping
    public ResponseEntity<?> createStorage(@Valid @RequestBody Storage storage) {
        return ResponseEntity.ok(storageService.create(storage));
    }

    @GetMapping
    public ResponseEntity<?> getStorages() {
        return ResponseEntity.ok(storageService.getAllStorages());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteStorages(@RequestParam("id") List<Long> ids) {
        return ResponseEntity.ok(storageService.deleteStoragesByIds(ids));
    }
}
