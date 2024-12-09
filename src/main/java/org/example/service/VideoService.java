package org.example.service;


import org.example.model.Video;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VideoService {


        private final Path rootLocation = Paths.get("video-storage");

        public VideoService() {
            try {
                Files.createDirectories(rootLocation);
            } catch (Exception e) {
                throw new RuntimeException("Could not initialize storage location");
            }
        }

        public ResponseEntity<?> saveVideo(MultipartFile file) {
            try {
                String filename = UUID.randomUUID().toString() +
                        file.getOriginalFilename();
                Files.copy(file.getInputStream(),
                        this.rootLocation.resolve(filename));

                Video video = new Video();
                video.setId(UUID.randomUUID().toString());
                video.setName(file.getOriginalFilename());
                video.setPath(filename);
                // Save video metadata to database or file system

                return ResponseEntity.ok().body("Video saved successfully");
            } catch (Exception e) {
                return ResponseEntity.badRequest()
                        .body("Failed to save video: " + e.getMessage());
            }
        }

        public List<Video> getAllVideos() {
            List<Video> videos = new ArrayList<>();
            try {
                Files.walk(rootLocation)
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            Video video = new Video();
                            video.setId(UUID.randomUUID().toString());
                            video.setName(path.getFileName().toString());
                            video.setPath(path.toString());
                            videos.add(video);
                        });
            } catch (Exception e) {
                throw new RuntimeException("Failed to read stored videos");
            }
            return videos;
        }

        public ResponseEntity<Resource> getVideo(String id) {
            try {
                // In real implementation, fetch video path from database using id
                Path file = rootLocation.resolve("sample-video-path");
                Resource resource = new UrlResource(file.toUri());

                if (resource.exists() || resource.isReadable()) {
                    return ResponseEntity.ok().body(resource);
                } else {
                    throw new RuntimeException("Could not read the file!");
                }
            } catch (Exception e) {
                throw new RuntimeException("Error: " + e.getMessage());
            }
        }
}
