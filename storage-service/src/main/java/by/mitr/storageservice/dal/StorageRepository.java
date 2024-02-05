package by.mitr.storageservice.dal;

import by.mitr.storageservice.dal.entity.StorageDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends CrudRepository<StorageDao, Long> {
}
