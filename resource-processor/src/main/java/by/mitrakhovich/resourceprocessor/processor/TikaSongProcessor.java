package by.mitrakhovich.resourceprocessor.processor;

import by.mitrakhovich.resourceprocessor.processor.Model.Song;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.XMPDM;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Optional;


@Component
public class TikaSongProcessor {

    private static final String NO_DATA = "No Data";

    public Song processSound(InputStream inputStream, String resourceId) {
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();
        Mp3Parser Mp3Parser = new Mp3Parser();
        try {
            Mp3Parser.parse(inputStream, handler, metadata, context);
        } catch (IOException | SAXException | TikaException e) {
            throw new RuntimeException("Can not parse song metadata", e);
        }

        Optional<String> title = Optional.ofNullable(metadata.get("dc:title"));
        Optional<String> artist = Optional.ofNullable(metadata.get(XMPDM.ARTIST));
        Optional<String> album = Optional.ofNullable(metadata.get(XMPDM.ALBUM));
        Duration lengthInSeconds = Duration.ofSeconds(Long.parseLong(metadata.get(XMPDM.DURATION).split("[.]")[0]));
        Optional<String> lengthInMilliseconds = Optional.ofNullable(String
                .format("%s:%s", lengthInSeconds.toMinutesPart(), lengthInSeconds.toSecondsPart()));
        Optional<String> year = Optional.ofNullable(metadata.get(XMPDM.SHOT_DATE));

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
