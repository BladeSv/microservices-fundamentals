package by.mitrakhovich.resourceservice.controller;


import by.mitrakhovich.resourceservice.dal.entity.SoundRecord;
import by.mitrakhovich.resourceservice.dal.repository.SoundRecordRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext
@AutoConfigureMessageVerifier
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@TestPropertySource(locations = "classpath:application-test.properties")
public class ResourceControllerContractTest {


    @Autowired
    private ResourceController ResourceController;

    @MockBean
    private AmazonS3 amazonS3;

    @MockBean
    private SoundRecordRepository soundRecordRepository;

    @BeforeEach
    public void setup() {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(ResourceController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

        RestAssuredMockMvc.standaloneSetup(ResourceController);
        GetObjectRequest request = new GetObjectRequest("bladesv", "1");
        S3Object s3Object = mock(S3Object.class);
        InputStream ins = new ByteArrayInputStream(new byte[]{1, 1});
        S3ObjectInputStream s3ObjectInputStream = new S3ObjectInputStream(ins, null);
        when(s3Object.getObjectContent()).thenReturn(s3ObjectInputStream);
        ObjectMetadata objectMetadata = mock(ObjectMetadata.class);
        when(s3Object.getObjectMetadata()).thenReturn(objectMetadata);
        when(objectMetadata.getContentType()).thenReturn("ContentType/Media");
        when(objectMetadata.getContentLength()).thenReturn(2L);
        when(amazonS3.getObject(eq(request))).thenReturn(s3Object);

        SoundRecord soundRecord = new SoundRecord(null, "Metallica");
        when(soundRecordRepository.findById(1L)).thenReturn(Optional.of(soundRecord));
    }
}
