package by.mitrakhovich.resourceservice.controller;

import by.mitrakhovich.resourceservice.model.RecordDTO;
import by.mitrakhovich.resourceservice.service.SoundRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResourceController.class)
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoundRecordService soundRecordService;

    @Test
    void shouldReturnMessageFromService() throws Exception {

        RecordDTO recordDTO = new RecordDTO(InputStream.nullInputStream(), "Metallica", "contentType", 0L);
        when(soundRecordService.getRecord(eq(1L), any())).thenReturn(recordDTO);
        this.mockMvc.perform(get("/resources/{id}", 1)).andDo(print()).andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"=?UTF-8?Q?Metallica?=\"; filename*=UTF-8''Metallica"));
    }
}
