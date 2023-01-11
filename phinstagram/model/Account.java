package com.beam.sample.phinstagram.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.catalina.User;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
@Accessors(chain = true)

public class Account extends Base{

    private String username;

    private String password;

    @DBRef
    private UserSettings userSettings;


    @DBRef
    private List<UserSettings> followers;

    @DBRef
    private List<UserSettings> following;

    public UserSettings getUserSettings() {
        if (userSettings == null){
            userSettings = new UserSettings();
        }
        return userSettings;
    }



    public List<UserSettings> getFollowers() {
        if (followers == null) {
            followers = new ArrayList<>();
        }
        return followers;
    }

    public List<UserSettings> getFollowing() {
        if (following == null) {
            following = new ArrayList<>();
        }
        return following;
    }


   /** @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final UserSettings other = (UserSettings) obj;
        if ((this.getId() == null) ? (other.getId() != null) : !this.getId().equals(other.getId())) {
            return false;
        }

        return true;
    }
   */

}
