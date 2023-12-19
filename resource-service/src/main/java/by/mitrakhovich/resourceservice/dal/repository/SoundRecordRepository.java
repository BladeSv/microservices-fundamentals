package by.mitrakhovich.resourceservice.dal.repository;

import by.mitrakhovich.resourceservice.dal.entity.SoundRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoundRecordRepository extends CrudRepository<SoundRecord, Long> {
}
