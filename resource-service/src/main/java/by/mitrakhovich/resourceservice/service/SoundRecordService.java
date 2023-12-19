package by.mitrakhovich.resourceservice.service;

import by.mitrakhovich.resourceservice.dal.entity.SoundRecord;
import by.mitrakhovich.resourceservice.dal.repository.SoundRecordRepository;
import by.mitrakhovich.resourceservice.model.RecordDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class SoundRecordService {
    private SoundRecordRepository soundRecordRepository;
    private S3service s3service;

    private MessageService messageService;

    public Map<String, Long> saveRecord(final MultipartFile file) {
        SoundRecord soundRecord = new SoundRecord();
        soundRecord.setFileName(file.getOriginalFilename());
        SoundRecord savedSoundRecord = soundRecordRepository.save(soundRecord);

        Long recordId = savedSoundRecord.getId();
        s3service.uploadFile(file, recordId.toString());
        messageService.sentMessage(recordId.toString());
        return Map.of("id", recordId);
    }

    public RecordDTO getRecord(long id, HttpRange httpRange) {
        Optional<SoundRecord> soundRecord = soundRecordRepository.findById(id);

        if (soundRecord.isPresent()) {
            RecordDTO s3Resource = s3service.getS3Resource(id, httpRange);
            s3Resource.setFileName(soundRecord.get().getFileName());
            return s3Resource;
        }
        return null;
    }


    public Map<String, List<Long>> removeRecordsByIds(List<Long> ids) {
        List<Long> removedIds = ids.stream().map(id -> {
            try {
                soundRecordRepository.deleteById(id);
                return id;
            } catch (EmptyResultDataAccessException ex) {
                log.info("Record by id: " + id + "do not exist");
            }
            return null;
        }).filter(Objects::nonNull).toList();

        if (!removedIds.isEmpty()) {
            List<String> removedIdsForS3 = removedIds.stream().map(Object::toString).collect(Collectors.toList());
            s3service.removeAllS3ResourceById(removedIdsForS3);
        }

        return Map.of("ids:", removedIds);
    }
}

