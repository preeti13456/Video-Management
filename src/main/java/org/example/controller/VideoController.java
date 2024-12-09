package org.example.controller;


import org.example.model.Video;
import org.example.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/record")
    public ResponseEntity<?> saveVideo(@RequestParam("file") MultipartFile file) {
        return videoService.saveVideo(file);
    }

    @GetMapping
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getVideo(@PathVariable String id) {
        return videoService.getVideo(id);
    }
}
