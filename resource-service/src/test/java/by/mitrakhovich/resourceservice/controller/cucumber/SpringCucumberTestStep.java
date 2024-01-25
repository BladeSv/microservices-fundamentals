package by.mitrakhovich.resourceservice.controller.cucumber;

import by.mitrakhovich.resourceservice.configuration.TestConfiguration;
import by.mitrakhovich.resourceservice.dal.entity.SoundRecord;
import by.mitrakhovich.resourceservice.dal.repository.SoundRecordRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@TestPropertySource(locations = "classpath:application-test.properties")

@Import(TestConfiguration.class)
public class SpringCucumberTestStep {

    @LocalServerPort
    private String port;

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private SoundRecordRepository soundRecordRepository;
    private ResponseEntity<String> latestResponse;

    @Before
    public void init() {
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
        soundRecordRepository.save(soundRecord);
    }

    public void executeGet(String url) {
        RestTemplate restTemplate = new RestTemplate();
        latestResponse = restTemplate.getForEntity("http://localhost:" + port + url, String.class);
    }

    public ResponseEntity<String> getLatestResponse() {
        return latestResponse;
    }

    @When("^the client calls (.+)$")
    public void the_client_issues_GET_version(String url) {
        executeGet(url);
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) {
        HttpStatusCode currentStatusCode = getLatestResponse().getStatusCode();
        assertThat(currentStatusCode.value()).isEqualTo(statusCode);
    }

    @And("^the client receives mp3 file with (.+) name")
    public void the_client_receives_server_mp3_file(String name) {
        assertThat(latestResponse.getBody().getBytes()).isEqualTo(new byte[]{1, 1});
        assertThat(latestResponse.getHeaders().getContentDisposition().toString()).isEqualTo(String.format("attachment; filename=\"=?UTF-8?Q?%s?=\"; filename*=UTF-8''%s", name, name));

    }
}
