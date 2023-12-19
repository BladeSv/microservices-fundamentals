package by.mitrakhovich.resourceprocessor.processor;

import by.mitrakhovich.resourceprocessor.processor.Model.Song;
import com.groupdocs.metadata.Metadata;
import com.groupdocs.metadata.core.ID3V2Tag;
import com.groupdocs.metadata.core.MP3RootPackage;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Optional;

@Component
public class SongProcessor {
    private static final String NO_DATA = "No Data";

    public Song processSound(InputStream inputStream, String resourceId) {
        Metadata metadata = new Metadata(inputStream);
        MP3RootPackage rootPackageGeneric = metadata.getRootPackageGeneric();

        ID3V2Tag id3V2 = rootPackageGeneric.getID3V2();
        Optional<String> title = Optional.ofNullable(id3V2.getTitle());
        Optional<String> artist = Optional.ofNullable(id3V2.getArtist());
        Optional<String> album = Optional.ofNullable(id3V2.getAlbum());
        Optional<String> lengthInMilliseconds = Optional.ofNullable(id3V2.getLengthInMilliseconds());
        Optional<String> year = Optional.ofNullable(id3V2.getYear());

        return Song.builder()
                .name(title.orElse(NO_DATA))
                .artist(artist.orElse(NO_DATA))
                .album(album.orElse(NO_DATA))
                .length(lengthInMilliseconds.orElse("0:00"))
                .year(year.orElse("0000"))
                .resourceId(resourceId)
                .build();
    }
}
