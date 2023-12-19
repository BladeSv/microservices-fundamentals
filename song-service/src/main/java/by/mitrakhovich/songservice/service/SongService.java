package by.mitrakhovich.songservice.service;

import by.mitrakhovich.songservice.Mapper.SongMapper;
import by.mitrakhovich.songservice.Model.Song;
import by.mitrakhovich.songservice.dal.entity.SongDao;
import by.mitrakhovich.songservice.dal.repository.SongRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class SongService {
    private SongRepository songRepository;
    SongMapper songMapper;

    public Map<String, Long> saveSong(Song song) {
        SongDao songDao = songMapper.toSongDao(song);
        return Map.of("id", songRepository.save(songDao).getId());
    }

    public Song getSongById(Long id) {
        SongDao songDao = songRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Song with id: " + id + "do not exist"));
        return songMapper.toSong(songDao);
    }

    public Map<String, List<Long>> removeSongsByIds(List<Long> ids) {
        List<Long> removedIds = ids.stream().map(id -> {
            try {
                songRepository.deleteById(id);
                return id;
            } catch (EmptyResultDataAccessException ex) {
                log.info("Record by id: " + id + "do not exist");
            }
            return null;
        }).filter(Objects::nonNull).toList();

        return Map.of("ids", removedIds);
    }
}
