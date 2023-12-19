package by.mitrakhovich.resourceservice.service;

import by.mitrakhovich.resourceservice.dal.repository.S3Repository;
import by.mitrakhovich.resourceservice.mapper.S3ResponseMapper;
import by.mitrakhovich.resourceservice.model.RecordDTO;
import com.amazonaws.services.s3.model.S3Object;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class S3service {
    private S3ResponseMapper s3ResponseMapper;
    private S3Repository s3Repository;

    public void uploadFile(final MultipartFile file, String s3FileName) {
        s3Repository.uploadFile(file, s3FileName);
    }

    public RecordDTO getS3Resource(Long id, HttpRange httpRange) {
        if (id < 0) {
            return null;
        }
        RecordDTO result;

        S3Object download;
        if (httpRange != null) {
            download = s3Repository.download(id, httpRange.getRangeStart(Long.MAX_VALUE), httpRange.getRangeEnd(Long.MAX_VALUE));
        } else {
            download = s3Repository.download(id, -1, -1);
        }
        result = download == null ? null : s3ResponseMapper.toRecordDTO(download);

        return result;
    }

    public void removeAllS3ResourceById(List<String> ids) {
        s3Repository.removeRecordByKeys(ids);
    }
}
