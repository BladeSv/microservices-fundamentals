package by.mitrakhovich.resourceservice.dal.repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.Copy;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.amazonaws.services.s3.transfer.Upload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

//@Slf4j
@Repository
public class S3Repository {

    Logger log = LoggerFactory.getLogger(this.getClass());
    private final AmazonS3 s3;

//    @Value("${user.aws.s3.bucket.name:bladesv}")
//    private String bucketName;

    public S3Repository(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void bucketCheck(String bucketName) {
        boolean existS3 = s3.doesBucketExistV2(bucketName);
        if (!existS3) {
            s3.createBucket(bucketName);
        }
    }

    public void uploadFile(final MultipartFile file, String bucketName, String path) {
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            log.error("Can not get input stream from input MultipartFile", e);
            throw new RuntimeException(e);
        }
        bucketCheck(bucketName);
        String contentType = file.getContentType();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(contentType);
        objectMetadata.setContentLength(file.getSize());
        PutObjectRequest request = new PutObjectRequest(bucketName, path, inputStream, objectMetadata);

        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3).build();
        try {
            Upload upload = transferManager.upload(request);
            upload.waitForCompletion();
            log.info("file with path {} was uploaded to bucket {}", path, bucketName);
        } catch (InterruptedException e) {
            log.error("Can not upload file to s3 bucket {} and path {}", bucketName, path, e);
            throw new RuntimeException(e);
        }
    }

    public S3Object download(String bucketName, String path, long startRange, long endRange) {
        bucketCheck(bucketName);
        GetObjectRequest request = new GetObjectRequest(bucketName, path);
        if (startRange >= 0 && endRange >= 0) {
            request.setRange(startRange, endRange);
        } else if (startRange >= 0) {
            request.setRange(startRange);
        }
        S3Object object = null;
        try {
            object = s3.getObject(request);
            log.info("S3 do return object from bucket {} and path {}", bucketName, path);
        } catch (RuntimeException e) {
            log.error("S3 do not return object " + path, e);
            throw new RuntimeException(e);
        }

        return object;
    }

    public void removeRecordByKeys(Map<String, List<String>> bucketNameAndPathMap) {
        if (!bucketNameAndPathMap.isEmpty()) {
            bucketNameAndPathMap.forEach((bucketName, paths) -> {
                bucketCheck(bucketName);
                if (!paths.isEmpty()) {
                    DeleteObjectsRequest request = new DeleteObjectsRequest(bucketName);
                    List<DeleteObjectsRequest.KeyVersion> keys = paths.stream().map(DeleteObjectsRequest.KeyVersion::new).toList();
                    request.setKeys(keys);
                    try {
                        s3.deleteObjects(request);
                        log.info("S3 delete object bucket {} and paths {} ", bucketName, paths);
                    } catch (RuntimeException e) {
                        log.error("S3 do not delete object bucket {} and paths {} ", bucketName, paths, e);
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }


    public void copyFile(String fromBucket, String fromPath, String toBucket, String toPath) {
        bucketCheck(fromBucket);
        bucketCheck(toBucket);
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest();
        copyObjectRequest.withSourceBucketName(fromBucket)
                .withSourceKey(fromPath)
                .withDestinationBucketName(toBucket)
                .withDestinationKey(toPath);
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(s3).build();
        try {
            Copy copy = transferManager.copy(copyObjectRequest);
            copy.waitForCompletion();
            s3.copyObject(copyObjectRequest);
            log.info("S3 copy object from {}/{} to {}/{} ", fromBucket, fromPath, toBucket, toPath);
        } catch (InterruptedException e) {
            log.error("S3 do not copy object from {}/{} to {}/{} ", fromBucket, fromPath, toBucket, toPath, e);
            throw new RuntimeException(e);
        }
    }
}
