package com.beam.sample.phinstagram.service;


import com.beam.sample.phinstagram.model.Photo;
import com.beam.sample.phinstagram.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class PhotoService {



    private final PhotoRepository photoRepository;


    public void save (Photo photo) {photoRepository.save(photo);}


    public List<Photo> findAll() {
        return photoRepository.findAll();
    }

    public Photo findById(String id) {
        return photoRepository.findById(id).get();
    }

    public void delete(Photo byId) {
        photoRepository.delete(byId);
    }
}
