package by.mitrakhovich.resourceservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {
    private static final String MP3_SONG_REGEX = "^.+\\.mp3$";

    @Override
    public void initialize(ValidFile constraintAnnotation) {
      
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return value.getOriginalFilename().toLowerCase().matches(MP3_SONG_REGEX);
    }
}
