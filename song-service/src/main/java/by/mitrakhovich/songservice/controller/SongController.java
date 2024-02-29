package by.mitrakhovich.songservice.controller;

import brave.Tracing;
import brave.propagation.TraceContext;
import by.mitrakhovich.songservice.Model.Song;
import by.mitrakhovich.songservice.service.SongService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/songs")
@Slf4j
public class SongController {
    private SongService songService;

    @Autowired
    Tracing tracing;

    @PostMapping
    public ResponseEntity<?> createSong(@Valid @RequestBody Song song) {
        TraceContext traceContext = tracing.currentTraceContext().get();

        log.info("Request to create Song-{}", song);
        Map<String, Long> songId = songService.saveSong(song);
        log.info("Song was created {}", songId);
        return ResponseEntity.ok(songId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSongById(@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @DeleteMapping
    public ResponseEntity<?> removeSongsByIds(@RequestParam("id") List<Long> ids) {
        return ResponseEntity.ok(songService.removeSongsByIds(ids));
    }
}
