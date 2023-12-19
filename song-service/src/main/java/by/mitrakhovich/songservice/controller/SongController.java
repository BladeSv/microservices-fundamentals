package by.mitrakhovich.songservice.controller;

import by.mitrakhovich.songservice.Model.Song;
import by.mitrakhovich.songservice.service.SongService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/songs")
public class SongController {
    private SongService songService;

    @PostMapping
    public ResponseEntity<?> createSong(@Valid @RequestBody Song song) {

        System.out.println("song-" + song);
        return ResponseEntity.ok(songService.saveSong(song));
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
