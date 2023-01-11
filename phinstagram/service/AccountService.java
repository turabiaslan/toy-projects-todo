package com.beam.sample.phinstagram.service;


import com.beam.sample.phinstagram.dto.AuthenticateResult;
import com.beam.sample.phinstagram.model.Account;
import com.beam.sample.phinstagram.model.GenericResponse;
import com.beam.sample.phinstagram.model.Photo;
import com.beam.sample.phinstagram.model.UserSettings;
import com.beam.sample.phinstagram.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountService {


    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    private final DiskService diskService;

    private final PhotoService photoService;

    private final UserSettingService userSettingService;


    //true is reserved for first case for the functions taking boolean variable!!

    public void addOrRemoveFollower(String name, Account account, boolean addOrRemove) {
        Account followedAccount = accountRepository.findByUsername(name);
        List<UserSettings> followedAccountFollowers = followedAccount.getFollowers();
        if (addOrRemove) {
            followedAccountFollowers.add(account.getUserSettings());
        } else {
            int x = followedAccountFollowers.indexOf(account.getUserSettings());
            followedAccountFollowers.remove(x);
        }
        followedAccount.setFollowers(followedAccountFollowers);
        accountRepository.save(followedAccount);

    }

    public void addOrRemoveFollowing(String name, Account account, boolean addOrRemove) {
        UserSettings followedAccountUser = accountRepository.findByUsername(name).getUserSettings();
        List<UserSettings> following = account.getFollowing();
        if (addOrRemove) {
            following.add(followedAccountUser);
        } else {
            int x= following.indexOf(followedAccountUser);
            following.remove(x);
        }
        account.setFollowing(following);
        accountRepository.save(account);
    }

    public void follow(String name, Account account) {
        if (!account.getUsername().equals(name)) {

            addOrRemoveFollowing(name, account, true);
            addOrRemoveFollower(name, account, true);
        }

    }

    public void unFollow(String name, Account account) {
        addOrRemoveFollowing(name, account, false);
        addOrRemoveFollower(name, account, false);

    }

    public Photo post(MultipartFile file, String caption, Account account) {
        try {
            String id = UUID.randomUUID().toString();
            diskService.savePhoto(account, file.getBytes(), id);
            List<Photo> photos = account.getUserSettings().getPhotoList();
            Photo foto = new Photo()
                    .setCaption(caption)
                    .setDate(new Date())
                    .setUsername(account.getUsername())
                    .setAvatar(account.getUserSettings().getAvatar())
                    .setPath(account.getId() + File.separator +id);
            foto.setId(id);
            photoService.save(foto);
            photos.add(0, foto);
            account.getUserSettings().setPhotoList(photos);
            userSettingService.save(account.getUserSettings());
            accountRepository.save(account);
            return foto;

        } catch (IOException exception) {
            log.error(exception.toString());
            return null;
        }


    }

    public Page<Photo> showProfile(String username, int page, int size) {
        Account account = accountRepository.findByUsername(username);
        List<Photo> fotos = account.getUserSettings().getPhotoList();
        return manualPaging(fotos, page, size);
    }

    public Page<Photo> manualPaging(List<Photo> fotos, int page, int size) {
        int start = Math.min(page * size, fotos.size());
        int end = Math.min((page + 1) * size, fotos.size());
        Page<Photo> photoPage = new PageImpl<>(fotos.subList(start, end));
        return photoPage;
    }


    public List<byte[]> findProfilePhotos(String id, int page, int size) {
        List<Photo> photoList = accountRepository.findById(id).get().getUserSettings().getPhotoList();
        List<String> path = new ArrayList<>();
        photoList.forEach(photo -> path.add(photo.getId()));
        List<byte[]> fotos = new ArrayList<>();
        path.forEach(p -> fotos.add(diskService.readPhoto(id, p)));
        return fotos;
    }

    public List<String> showFollowersOrFollowing(String name, boolean followOrFollower) {
        List<String> followersOrFollowingUsername = new ArrayList<>();
        Account account = accountRepository.findByUsername(name);
        if (followOrFollower) {
            List<UserSettings> follower = account.getFollowers();
            follower.forEach(f -> followersOrFollowingUsername.add(f.getFullname()));
        } else {
            List<UserSettings> following = account.getFollowing();
            following.forEach(f -> followersOrFollowingUsername.add(f.getFullname()));
        }
        return followersOrFollowingUsername;
    }


    public Page<Photo> personalFeed(Account account, int page, int size) {
        List<UserSettings> following = account.getFollowing();
        List<Photo> feedPhotos = new ArrayList<>();
        following.forEach(f -> feedPhotos.addAll(f.getPhotoList()));
        feedPhotos.addAll(account.getUserSettings().getPhotoList());
        Collections.sort(feedPhotos, new Comparator<Photo>() {
            public int compare(Photo p1, Photo p2) {
                return p2.getDate().compareTo(p1.getDate());
            }
        });

        return manualPaging(feedPhotos, page, size);
    }

    public List<byte[]> personalFeedPhotos(Account account, int page, int size) {
        Page<Photo> photoPage = personalFeed(account, page, size);
        List<byte[]> photos = new ArrayList<>();
        photoPage.forEach(p -> photos.add(diskService.readPhoto(account.getId(), p.getId())));
        return photos;
    }

    public Page<Photo> discover(int page, int size) {
        List<Photo> all = photoService.findAll();
        Collections.shuffle(all);
        return manualPaging(all, page, size);


    }

    public List<byte[]> discoverPhotos(int page, int size) {
        Page<Photo> photoPage = discover(page, size);
        List<byte[]> photos = new ArrayList<>();
        photoPage.forEach(p -> photos.add(diskService.readPhoto(p.getPath())));
        return photos;
    }

    public GenericResponse deletePhoto(Account account, String id) {
        String accountId = account.getId();
        photoService.delete(photoService.findById(id));
        return new GenericResponse().setStatus(diskService.deletePhoto(accountId, id));
    }

    public byte[] readAvatar(String path) {
        try {
            return diskService.readAvatar(path);

        } catch (IOException exception) {
            log.error(exception.toString());
            return null;
        }
    }

    public AuthenticateResult register(Account account, MultipartFile file) {
        String username = account.getUsername();
        String password = account.getPassword();
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            return new AuthenticateResult().setMessage("username & password cannot be null or empty");
        }
        if (accountRepository.existsByUsername(username)) {
            return new AuthenticateResult().setMessage("This username is already taken. Please choose another one");
        } else {
            if (file != null) {
                saveAccountAvatar(account, file);
            }
            //2.
            account.setPassword(passwordEncoder.encode(account.getPassword()));
            account.setId(UUID.randomUUID().toString());
            UserSettings userSettings = new UserSettings();
            userSettings.setId(UUID.randomUUID().toString());
            account.setUserSettings(userSettings);
            account = accountRepository.save(account);
            userSettingService.save(userSettings);
            return new AuthenticateResult()
                    .setSuccess(true)
                    .setUserSettings(account.getUserSettings())
                    .setAccount(account);
        }
    }

    public void saveAccountAvatar(Account account, MultipartFile file) {
        String filename = "";

        filename = diskService.saveAvatar(file, account);

        account.getUserSettings().setAvatar("/avatar/" + filename);
    }

    public AuthenticateResult authenticate(String username, String password) {

        Account account = accountRepository.findByUsername(username);

        if (account != null && passwordEncoder.matches(password, account.getPassword())) {
            return new AuthenticateResult()
                    .setSuccess(true)
                    .setAccount(account)
                    .setUserSettings(account.getUserSettings())
                    .setUsername(account.getUsername());
        } else {
            return new AuthenticateResult();
        }
    }


    public UserSettings update(Account account, UserSettings userSettings, MultipartFile file) {
        account.setUserSettings(userSettings);
        if (file != null) {
            saveAccountAvatar(account, file);
        }
        accountRepository.save(account);
        return account.getUserSettings();
    }

    public byte[] readPhoto(String path){
        return diskService.readPhoto(path);
    }

    public GenericResponse isAlreadyFollowing(String name, Account account){
        UserSettings userSettings = accountRepository.findByUsername(name).getUserSettings();
        if(account.getFollowing().contains(userSettings)){
            return new GenericResponse().setStatus(true);
        }else{
            return new GenericResponse().setStatus(false);
        }
    }

    public UserSettings userSettings(String name) {
        Account account = accountRepository.findByUsername(name);
        String id = account.getUserSettings().getId();
        return userSettingService.findById(id);
    }


    public List<Account> searchUser(String name) {
        name = "^" + name;
        return accountRepository.findByUsernameStartingWith(name);
    }
}
