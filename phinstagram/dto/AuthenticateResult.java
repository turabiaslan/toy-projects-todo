package com.beam.sample.phinstagram.dto;


import com.beam.sample.phinstagram.model.Account;
import com.beam.sample.phinstagram.model.UserSettings;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class AuthenticateResult {

    private boolean success;

    private UserSettings userSettings;

    private Account account;

    private String message;

    private String  username;

}
