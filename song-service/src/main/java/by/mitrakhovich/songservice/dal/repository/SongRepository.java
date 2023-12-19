package by.mitrakhovich.songservice.dal.repository;


import by.mitrakhovich.songservice.dal.entity.SongDao;
import org.springframework.data.repository.CrudRepository;

public interface SongRepository extends CrudRepository<SongDao, Long> {
}
