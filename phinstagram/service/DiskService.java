package com.beam.sample.phinstagram.service;


import com.beam.sample.phinstagram.model.Account;
import com.beam.sample.phinstagram.model.Photo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
@Slf4j
@RequiredArgsConstructor
@Service
public class DiskService {

    private final  String base = "C:\\Users\\turabi.aslan\\Desktop\\ToyProjects\\Disks\\PhinstagramDisk/";

    private final String avatar = "avatar/";



    public String saveAvatar(MultipartFile file, Account account) {
        String filename = "";
        try {
            byte[] data = file.getBytes();
            filename = UUID.randomUUID().toString();
            Path path = Paths.get(base + avatar + filename);
            Files.write(path, data);
            account.getUserSettings().setAvatar(avatar + filename);
            return filename;
        }catch (IOException exception) {
            log.error(exception.toString());
            return null;
        }
    }


    public byte[] readAvatar(String path) throws IOException{
        return Files.readAllBytes(Paths.get(base  + path));
    }


    public void savePhoto(Account account, byte[] data, String id) throws IOException {
        if(!Files.exists(Paths.get(base + account.getId()))){
            Files.createDirectory(Paths.get(base + account.getId()));
        }
        Path path = Paths.get(base + account.getId() + File.separator + id );
        Files.write(path, data);

    }

    public byte[] readPhoto(String id, String filename) {
        try{
        return Files.readAllBytes(Paths.get(base + id + File.separator + filename));}
        catch (IOException exception){
            log.error(exception.toString());
            return null;
        }
    }

    public boolean deletePhoto(String id, String filename) {
        try {
            Files.deleteIfExists(Paths.get(base +id + File.separator + filename));
            return true;
        }catch (IOException exception){
            log.error(exception.toString());
            return false;
        }

    }

    public byte[] readPhoto(String path){
        try {
            return Files.readAllBytes(Paths.get(base + path));
        }catch (IOException exception) {
            log.error(exception.toString());
            return null;
        }
    }




}
