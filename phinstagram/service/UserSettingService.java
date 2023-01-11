package com.beam.sample.phinstagram.service;


import com.beam.sample.phinstagram.model.UserSettings;
import com.beam.sample.phinstagram.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSettingService {

    private final UserSettingsRepository userSettingsRepository;

    public void save(UserSettings userSettings){
        userSettingsRepository.save(userSettings);
    }

    public UserSettings findById(String id) {
        return userSettingsRepository.findById(id).get();
    }
}
