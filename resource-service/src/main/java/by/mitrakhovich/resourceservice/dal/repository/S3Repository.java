package by.mitrakhovich.resourceservice.dal.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Repository
public class S3Repository {

    private final AmazonS3 s3;

    @Value("${user.aws.s3.bucket.name:bladesv}")
    private String bucketName;

    public S3Repository(AmazonS3 s3) {
        this.s3 = s3;
    }

    @PostConstruct
    public void init() {
        boolean existS3 = s3.doesBucketExistV2(bucketName);
        if (!existS3) {
            s3.createBucket(bucketName);
        }
    }

    public void uploadFile(final MultipartFile file, String s3FileName) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.error("Can not get input stream from input MultipartFile", e);
            throw new RuntimeException(e);
        }
        String contentType = file.getContentType();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(file.getSize());

        PutObjectRequest request = new PutObjectRequest(bucketName, s3FileName, inputStream, objectMetadata);

        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3).build();

        try {
            Upload upload = transferManager.upload(request);
            upload.waitForCompletion();
            log.info("file with name-{} was uploaded to bucket-{}", s3FileName, bucketName);
        } catch (InterruptedException e) {
            log.error("Can not upload file to s3 bucket", e);
            throw new RuntimeException(e);
        }
    }

    public S3Object download(Long id, long startRange, long endRange) {
        GetObjectRequest request = new GetObjectRequest(bucketName, id.toString());
        if (startRange >= 0 && endRange >= 0) {
            request.setRange(startRange, endRange);
        } else if (startRange >= 0) {
            request.setRange(startRange);
        }
        S3Object object = null;

        try {
            object = s3.getObject(request);
        } catch (RuntimeException e) {
            log.info("S3 do not return object by id " + id, e);
            throw new RuntimeException(e);
        }

        return object;
    }

    public void removeRecordByKeys(List<String> ids) {
        if (!ids.isEmpty()) {
            DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
            List<DeleteObjectsRequest.KeyVersion> keys = ids.stream().map(DeleteObjectsRequest.KeyVersion::new).toList();
            request.setKeys(keys);
            s3.deleteObjects(request);
        }
    }
}
