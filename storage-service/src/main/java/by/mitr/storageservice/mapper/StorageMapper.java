package by.mitr.storageservice.mapper;

import by.mitr.storageservice.dal.entity.StorageDao;
import by.mitr.storageservice.model.Storage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StorageMapper {
    Storage toStorage(StorageDao storageDao);

    StorageDao toStorageDao(Storage storage);
}
