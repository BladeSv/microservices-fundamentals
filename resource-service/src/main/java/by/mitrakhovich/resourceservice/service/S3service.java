package by.mitrakhovich.resourceservice.service;

import by.mitrakhovich.resourceservice.dal.entity.SoundRecord;
import by.mitrakhovich.resourceservice.dal.repository.S3Repository;
import by.mitrakhovich.resourceservice.mapper.S3ResponseMapper;
import by.mitrakhovich.resourceservice.model.RecordDTO;
import by.mitrakhovich.resourceservice.model.Storage;
import com.amazonaws.services.s3.model.S3Object;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

//@Slf4j
//@AllArgsConstructor
@Service
public class S3service {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final S3ResponseMapper s3ResponseMapper;
    private final S3Repository s3Repository;

    public S3service(S3ResponseMapper s3ResponseMapper, S3Repository s3Repository) {
        this.s3ResponseMapper = s3ResponseMapper;
        this.s3Repository = s3Repository;
    }

    public void uploadFile(final MultipartFile file, SoundRecord soundRecord) {
        String path = soundRecord.getPath() + soundRecord.getId().toString();
        s3Repository.uploadFile(file, soundRecord.getBucket(), path);
    }

    public RecordDTO getS3Resource(SoundRecord soundRecord, HttpRange httpRange) {
        if (soundRecord.getId() < 0) {
            return null;
        }
        RecordDTO result;

        String path = soundRecord.getPath() + soundRecord.getId();

        S3Object download;
        if (httpRange != null) {
            download = s3Repository.download(soundRecord.getBucket(), path, httpRange.getRangeStart(Long.MAX_VALUE), httpRange.getRangeEnd(Long.MAX_VALUE));
        } else {
            download = s3Repository.download(soundRecord.getBucket(), path, -1, -1);
        }
        result = download == null ? null : s3ResponseMapper.toRecordDTO(download);

        return result;
    }

    public void removeAllS3ResourceById(List<SoundRecord> soundRecords) {
        var deleteMap = soundRecords.stream()
                .collect(Collectors.groupingBy(SoundRecord::getBucket,
                        Collectors.mapping((record -> record.getPath() + record.getId()), toList())));
        s3Repository.removeRecordByKeys(deleteMap);

    }

    public void moveSoundRecord(SoundRecord soundRecord, Storage destinationStorage) {
        String formPath = soundRecord.getPath() + soundRecord.getId();
        String toPath = destinationStorage.getPath() + soundRecord.getId();
        s3Repository.copyFile(soundRecord.getBucket(), formPath, destinationStorage.getBucket(), toPath);
        removeAllS3ResourceById(List.of(soundRecord));
    }
}
