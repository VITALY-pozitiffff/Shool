package ru.hogwarts.school.controller;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@RestController
@RequestMapping("/avatars")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Avatar>> paginatedAvatars(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size) {

        Pageable paging = PageRequest.of(page, size);
        Page<Avatar> avatars = avatarService.paginatedAvatars((SpringDataWebProperties.Pageable) paging);
        return ResponseEntity.ok(avatars);
    }
    @PostMapping("/upload")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Генерируем объект Avatar
        Avatar avatar = new Avatar();
        avatar.setFilePath(file.getOriginalFilename());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        // Сохраняем в базу данных
        avatarService.saveAvatar(avatar);

        // Сохраняем файл на диск
        File dir = new File("uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        file.transferTo(new File(dir, file.getOriginalFilename()));

        return ResponseEntity.ok("Аватар успешно загружён");
    }


    @GetMapping("/from-database/{id}")
    public ResponseEntity<byte[]> getAvatarFromDatabase(@PathVariable Long id) {
        Optional<Avatar> avatarOpt = avatarService.findAvatarById(id);
        if (avatarOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Avatar avatar = avatarOpt.get();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(avatar.getMediaType()))
                .body(avatar.getData());
    }


    @GetMapping("/from-folder/{filename:.+}")
    public ResponseEntity<InputStreamSource> getAvatarFromFolder(@PathVariable String filename) throws IOException {
        File imageFile = new File("uploads/" + filename);
        InputStreamSource inputStreamSource = new FileSystemResource(imageFile);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(inputStreamSource);
    }
}