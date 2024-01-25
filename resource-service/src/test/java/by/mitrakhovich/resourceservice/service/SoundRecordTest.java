package by.mitrakhovich.resourceservice.service;

import by.mitrakhovich.resourceservice.dal.entity.SoundRecord;
import by.mitrakhovich.resourceservice.dal.repository.SoundRecordRepository;
import by.mitrakhovich.resourceservice.model.RecordDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpRange;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class SoundRecordTest {
    @Captor
    ArgumentCaptor<SoundRecord> soundRecordCaptor;
    @InjectMocks
    private SoundRecordService soundRecordService;
    @Mock
    private SoundRecordRepository soundRecordRepository;
    @Mock
    private S3service s3service;
    @Mock
    private MessageService messageService;

    @Test
    public void testSaveRecordWhenEnterCorrectDataReturnCorrectData() {

        Long recordId = 555L;
        String fileName = "Artist1-song1";

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(fileName);
        SoundRecord soundRecord = mock(SoundRecord.class);
        when(soundRecord.getId()).thenReturn(recordId);
        when(soundRecordRepository.save(any(SoundRecord.class))).thenReturn(soundRecord);

        Map<String, Long> actual = soundRecordService.saveRecord(file);

        Map<String, Long> expected = Map.of("id", recordId);

        assertEquals(expected, actual);
        verify(soundRecordRepository).save(soundRecordCaptor.capture());
        SoundRecord record = soundRecordCaptor.getValue();
        String recordFileName = record.getFileName();
        assertEquals(fileName, recordFileName);
        verify(s3service).uploadFile(eq(file), eq(recordId.toString()));
        verify(messageService).sentMessage(eq(recordId.toString()));
    }

    @Test
    public void testSaveRecordWhenEnterNullDataReturnEmptyMap() {
        Map<String, Long> result = soundRecordService.saveRecord(null);
        assertEquals(Collections.emptyMap(), result);
    }

    @Test
    public void testGetRecordWhenEnterCorrectDataReturnCorrectData() {
        long recordId = 555L;
        String fileName = "Artist1-song1";
        HttpRange httpRange = mock(HttpRange.class);
        SoundRecord soundRecord = new SoundRecord();
        soundRecord.setFileName(fileName);
        RecordDTO recordDTO = mock(RecordDTO.class);
        when(soundRecordRepository.findById(recordId)).thenReturn(Optional.of(soundRecord));
        when(s3service.getS3Resource(recordId, httpRange)).thenReturn(recordDTO);

        RecordDTO actual = soundRecordService.getRecord(recordId, httpRange);

        assertEquals(recordDTO, actual);

        verify(s3service).getS3Resource(eq(recordId), eq(httpRange));
        verify(recordDTO).setFileName(fileName);
    }

    @Test
    public void testGetRecordWhenEnterNullHttpRangeReturnNull() {
        long recordId = 555L;
        RecordDTO actual = soundRecordService.getRecord(recordId, null);
        assertNull(actual);
    }

    @Test
    public void testGetRecordWhenEnterZeroIdReturnNull() {
        long recordId = 0;
        HttpRange httpRange = mock(HttpRange.class);
        RecordDTO actual = soundRecordService.getRecord(recordId, httpRange);
        assertNull(actual);
    }

    @Test
    public void testGetRecordWhenNullSoundRecordReturnNull() {
        long recordId = 555L;
        HttpRange httpRange = mock(HttpRange.class);
        RecordDTO recordDTO = mock(RecordDTO.class);
        when(soundRecordRepository.findById(recordId)).thenReturn(Optional.empty());

        RecordDTO actual = soundRecordService.getRecord(recordId, httpRange);

        assertNull(actual);
        verify(s3service, never()).getS3Resource(any(), any());
        verify(recordDTO, never()).setFileName(any());
    }
}
