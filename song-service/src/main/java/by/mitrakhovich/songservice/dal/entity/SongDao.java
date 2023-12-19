package by.mitrakhovich.songservice.dal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.stereotype.Component;


@Data
@Entity
@Component
public class SongDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String artist;
    private String album;
    private String length;
    private String resourceId;
    private String year;
}
