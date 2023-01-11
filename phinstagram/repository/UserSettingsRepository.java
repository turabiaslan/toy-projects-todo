package com.beam.sample.phinstagram.repository;

import com.beam.sample.phinstagram.model.UserSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserSettingsRepository extends MongoRepository<UserSettings, String> {



}
