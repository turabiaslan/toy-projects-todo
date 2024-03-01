package com.beam.todo.service;


import com.beam.todo.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiskService {

    private static String base = "C:\\Users\\turabi.aslan\\Desktop\\ToDoDisk/";

    private static String avatar = "avatar/";
    public String saveUserAvatar(byte[] data, User user) throws IOException {
        String filename = UUID.randomUUID().toString();
        Path path = Paths.get(base+ avatar + filename);
        Files.write(path, data);
        return filename;
    }

    public byte[] readAvatar(String filename) throws IOException{
        return Files.readAllBytes(Paths.get(base + filename));
    }
}
