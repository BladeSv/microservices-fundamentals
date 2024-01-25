package by.mitrakhovich.resourceservice.dal.repository;

import by.mitrakhovich.resourceservice.dal.entity.SoundRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource(
        locations = "classpath:application-test.properties")
public class SoundRecordRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SoundRecordRepository soundRecordRepository;

    @Test
    public void whenFindByName_thenReturnUser() {
        SoundRecord soundRecord = new SoundRecord(null, "Metallica.mp3");
        entityManager.persist(soundRecord);
        entityManager.flush();
        Optional<SoundRecord> found = soundRecordRepository.findById(1L);
        assertEquals(found.get().getFileName(), soundRecord.getFileName());
    }
}
