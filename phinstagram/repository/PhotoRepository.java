package com.beam.sample.phinstagram.repository;

import com.beam.sample.phinstagram.model.Photo;


import com.mongodb.client.AggregateIterable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.RandomAccess;
import java.util.random.RandomGenerator;


public interface PhotoRepository extends MongoRepository<Photo, String> {




}
