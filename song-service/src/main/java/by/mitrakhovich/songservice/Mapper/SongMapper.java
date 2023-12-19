package by.mitrakhovich.songservice.Mapper;

import by.mitrakhovich.songservice.Model.Song;
import by.mitrakhovich.songservice.dal.entity.SongDao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SongMapper {
    Song toSong(SongDao songDao);

    SongDao toSongDao(Song song);
}
