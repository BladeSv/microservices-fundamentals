package by.mitrakhovich.resourceservice.controller;

import by.mitrakhovich.resourceservice.model.RecordDTO;
import by.mitrakhovich.resourceservice.service.SoundRecordService;
import by.mitrakhovich.resourceservice.validator.ValidFile;
import org.aspectj.util.FileUtil;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Validated
@RestController
@RequestMapping("/resources")
public class ResourceController {
    private final SoundRecordService soundRecordService;

    public ResourceController(SoundRecordService soundRecordService) {
        this.soundRecordService = soundRecordService;
    }

    @PostMapping
    public ResponseEntity<?> uploadResource(@ValidFile @RequestParam("file") MultipartFile file) {
        Map<String, Long> responseBody = soundRecordService.saveRecord(file);

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getResourceById(@PathVariable long id, @RequestHeader(required = false) String range) throws IOException {

        HttpRange httpRange = HttpRange.parseRanges(range).stream().findFirst().orElse(null);
        RecordDTO recordDTO = soundRecordService.getRecord(id, httpRange);

        if (recordDTO != null) {
            return createResponseEntity(recordDTO, httpRange);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The resource with the specified id does not exist");
    }

    @DeleteMapping()
    public ResponseEntity<?> removeResourcesById(@RequestParam(value = "id") List<Long> ids) {
        return ResponseEntity.ok(soundRecordService.removeRecordsByIds(ids));
    }

    private ResponseEntity<?> createResponseEntity(RecordDTO s3Resource, HttpRange httpRange) throws IOException {
        ResponseEntity.BodyBuilder responseEntityBuilder;

        if (httpRange == null) {
            responseEntityBuilder = ResponseEntity.status(HttpStatus.OK);
        } else {
            responseEntityBuilder = ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .header(HttpHeaders.CONTENT_RANGE, httpRange.toString());
        }
        ByteArrayResource resource = new ByteArrayResource(FileUtil.readAsByteArray(s3Resource.getResource()));

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(s3Resource.getFileName())
                .build();

        return responseEntityBuilder.contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(s3Resource.getContentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(resource);
    }
}
