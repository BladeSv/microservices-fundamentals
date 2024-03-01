package by.mitrakhovich.resourceservice.service;

import by.mitrakhovich.resourceservice.dal.entity.SoundRecord;
import by.mitrakhovich.resourceservice.dal.repository.SoundRecordRepository;
import by.mitrakhovich.resourceservice.model.RecordDTO;
import by.mitrakhovich.resourceservice.model.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;

//@Slf4j
//@AllArgsConstructor
@Service
public class SoundRecordService {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final SoundRecordRepository soundRecordRepository;
    private final S3service s3service;
    private final MessageService messageService;
    private final StorageService storageService;

    public SoundRecordService(SoundRecordRepository soundRecordRepository, S3service s3service, MessageService messageService, StorageService storageService) {
        this.soundRecordRepository = soundRecordRepository;
        this.s3service = s3service;
        this.messageService = messageService;
        this.storageService = storageService;
    }

//    @Autowired
//    Tracer tracer;

    public Map<String, Long> saveRecord(final MultipartFile file) {
//        log.info("!!!!context tracing {}", tracer.currentSpan().context());
//        log.info("!!!!parrent tracing {}", tracer.currentSpan().context().parentId());
        if (file == null) return Collections.emptyMap();
        List<Storage> storages = storageService.getStorages();
        Storage stagingStorage = storageService.getStagingStorage(storages);
        SoundRecord soundRecord = createSoundRecord(file);
        setToSoundRecordStorageData(soundRecord, stagingStorage);
        SoundRecord savedSoundRecord = soundRecordRepository.save(soundRecord);

        Long recordId = savedSoundRecord.getId();
        s3service.uploadFile(file, soundRecord);
        messageService.sentMessage(recordId.toString());
        return Map.of("id", recordId);
    }

    public void moveToPermanentBucket(String id) {
        SoundRecord soundRecord = soundRecordRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Not exist Sound Record with id " + id));
        List<Storage> storages = storageService.getStorages();
        Storage permanentStorage = storageService.getPermanentStorage(storages);
        s3service.moveSoundRecord(soundRecord, permanentStorage);
        setToSoundRecordStorageData(soundRecord, permanentStorage);
        soundRecordRepository.save(soundRecord);
    }

    public RecordDTO getRecord(long id, HttpRange httpRange) {
        if (id <= 0) return null;
        Optional<SoundRecord> soundRecord = soundRecordRepository.findById(id);

        if (soundRecord.isPresent()) {
            RecordDTO s3Resource = s3service.getS3Resource(soundRecord.get(), httpRange);
            s3Resource.setFileName(soundRecord.get().getFileName());
            return s3Resource;
        }

        return null;
    }

    public Map<String, List<Long>> removeRecordsByIds(List<Long> ids) {
        List<Long> existIds = ids.stream().
                filter(id -> soundRecordRepository.existsById(id))
                .toList();
        List<SoundRecord> soundRecords = StreamSupport
                .stream(soundRecordRepository.findAllById(existIds).spliterator(), false)
                .toList();

        if (!soundRecords.isEmpty()) {
            s3service.removeAllS3ResourceById(soundRecords);
        }

        soundRecordRepository.deleteAllById(existIds);

        return Map.of("ids:", existIds);
    }

    private SoundRecord createSoundRecord(final MultipartFile file) {
        SoundRecord soundRecord = new SoundRecord();
        soundRecord.setFileName(file.getOriginalFilename());
        return soundRecord;
    }

    private void setToSoundRecordStorageData(SoundRecord soundRecord, Storage storage) {
        soundRecord.setStorageType(storage.getStorageType());
        soundRecord.setBucket(storage.getBucket());
        soundRecord.setPath(storage.getPath());
    }


}

