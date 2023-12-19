package by.mitrakhovich.resourceservice.mapper;

import by.mitrakhovich.resourceservice.model.RecordDTO;
import com.amazonaws.services.s3.model.S3Object;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface S3ResponseMapper {
    @Mapping(target = "resource", expression = "java(s3Object.getObjectContent())")
    @Mapping(target = "contentType", expression = "java(s3Object.getObjectMetadata().getContentType())")
    @Mapping(target = "contentLength", expression = "java(s3Object.getObjectMetadata().getContentLength())")
    @Mapping(target = "fileName", ignore = true)
    RecordDTO toRecordDTO(S3Object s3Object);
}
