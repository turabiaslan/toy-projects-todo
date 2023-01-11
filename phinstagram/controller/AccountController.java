package com.beam.sample.phinstagram.controller;


import com.beam.sample.phinstagram.dto.AuthenticateResult;
import com.beam.sample.phinstagram.dto.LoginRequest;
import com.beam.sample.phinstagram.model.Account;
import com.beam.sample.phinstagram.model.GenericResponse;
import com.beam.sample.phinstagram.model.Photo;
import com.beam.sample.phinstagram.model.UserSettings;
import com.beam.sample.phinstagram.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    public static final String SESSION_ACCOUNT = "session_account";

    @PostMapping("login")
    public AuthenticateResult login(@RequestBody LoginRequest request,
                                    HttpSession session) {
        AuthenticateResult result = accountService.authenticate(request.getUsername(), request.getPassword());
        if (result.isSuccess()) {
            session.setAttribute(SESSION_ACCOUNT, result.getAccount());
            result.setAccount(null);
            return result;
        } else {
            return result;
        }
    }

    @GetMapping("logout")
    public AuthenticateResult logout(HttpSession session) {
        session.removeAttribute(SESSION_ACCOUNT);
        return new AuthenticateResult()
                .setSuccess(true);
    }

    @PostMapping("register")
    public AuthenticateResult register(Account account,
                                       MultipartFile file,
                                       HttpSession session) {
        AuthenticateResult result = accountService.register(account, file);
        if (result.isSuccess()) {
            session.setAttribute(SESSION_ACCOUNT, result.getAccount());
            result.setAccount(null);
            return result;
        } else {
            return null;
        }
    }

    @PostMapping("me")
    public AuthenticateResult saveUserSettings(@RequestParam String fullname,
                                               @RequestParam Date birthDate,
                                               MultipartFile file,
                                               HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            UserSettings userSettings = account.getUserSettings()
                    .setFullname(fullname)
                    .setBirthDate(birthDate);
            return new AuthenticateResult()
                    .setSuccess(true)
                    .setUserSettings(accountService.update(account, userSettings, file));
        } else {
            return null;
        }

    }

    @GetMapping("me")
    public AuthenticateResult whoami(HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return new AuthenticateResult()
                    .setSuccess(true)
                    .setUserSettings(account.getUserSettings());
        } else {
            return new AuthenticateResult()
                    .setSuccess(false);
        }
    }

    /**
     * @GetMapping("/avatar/{filename}") public ResponseEntity<byte[]> image(HttpSession session) {
     * Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
     * byte[] readAvatar = accountService.readAvatar(account);
     * return ResponseEntity.ok(readAvatar);
     * }
     */


    @PostMapping("post")
    public Photo post(@RequestParam String caption,
                      MultipartFile file,
                      HttpSession session) {
        Object account = session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            Account account1 = (Account) account;
            return accountService.post(file, caption, account1);
        } else {
            return null;
        }
    }

    @GetMapping("follow")
    public void follow(@RequestParam String name, HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            accountService.follow(name, account);
        }
    }

    @GetMapping("unfollow")
    public void unFollow(@RequestParam String name, HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            accountService.unFollow(name, account);
        }
    }

    @GetMapping("show-profile")
    public List<byte[]> profile(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "9") int size,
                                @RequestParam String id,
                                HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.findProfilePhotos(id, page, size);
        } else {
            return null;
        }
    }

    @GetMapping("profile/{username}")
    public Page<Photo> showProfile(@PathVariable String username,
                                   @RequestParam(required = false, defaultValue = "0") int page,
                                   @RequestParam(required = false, defaultValue = "9") int size,

                                   HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.showProfile(username, page, size);
        } else {
            return null;
        }
    }

    @GetMapping("show-followers")
    public List<String> showFollowers(@RequestParam String name, HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.showFollowersOrFollowing(name, true);
        } else {
            return null;
        }

    }

    @GetMapping("show-following")
    public List<String> showFollowing(@RequestParam String name, HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.showFollowersOrFollowing(name, false);
        } else {
            return null;
        }
    }

    @GetMapping("discover")
    public Page<Photo> discover(@RequestParam(required = false, defaultValue = "0") int page,
                                @RequestParam(required = false, defaultValue = "9") int size,
                                HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.discover(page, size);
        } else {
            return null;
        }
    }

    @GetMapping("discover-photos")
    public List<byte[]> discoverPhotos(@RequestParam(required = false, defaultValue = "0") int page,
                                       @RequestParam(required = false, defaultValue = "9") int size,
                                       HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.discoverPhotos(page, size);
        } else {
            return null;
        }
    }

    @GetMapping("personal-feed")
    public Page<Photo> personalFeed(@RequestParam(required = false, defaultValue = "0") int page,
                                    @RequestParam(required = false, defaultValue = "9") int size,
                                    HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.personalFeed(account, page, size);
        } else {
            return null;
        }
    }

    @GetMapping("personal-feed-photos")
    public List<byte[]> personalFeedPhotos(@RequestParam(required = false, defaultValue = "0") int page,
                                           @RequestParam(required = false, defaultValue = "9") int size,
                                           HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.personalFeedPhotos(account, page, size);
        } else {
            return null;
        }
    }


    @GetMapping("delete")
    public GenericResponse deletePhoto(@RequestParam String id,
                                       HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.deletePhoto(account, id);
        } else {
            return new GenericResponse();
        }

    }

    @GetMapping("/{path1}/{path2}")
    public ResponseEntity<byte[]> photo(@PathVariable String path1,
                                        @PathVariable String path2,
                                        HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        byte[] img = accountService.readPhoto(path1 + "/" + path2);
        return ResponseEntity.ok(img);
    }

    @GetMapping("/avatar/img/{path}")
    public ResponseEntity<byte[]> avatar(@PathVariable String path,
                                         HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        byte[] avatar = accountService.readAvatar(path);
        return ResponseEntity.ok(avatar);
    }

    @GetMapping("already-following")
    public GenericResponse isAlreadyFollowing(@RequestParam String username,
                                              HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        return accountService.isAlreadyFollowing(username, account);
    }

    @GetMapping("user-settings")
    public UserSettings userSettings(@RequestParam String name,
                                     HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.userSettings(name);
        } else {
            return null;
        }
    }

    @GetMapping("/search/{name}")
    public List<Account> searchUser(@PathVariable String name,
                                    HttpSession session) {
        Account account = (Account) session.getAttribute(SESSION_ACCOUNT);
        if (account != null) {
            return accountService.searchUser(name);
        } else {
            return null;
        }
    }


}
