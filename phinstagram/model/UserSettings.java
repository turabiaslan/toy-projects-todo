package com.beam.sample.phinstagram.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UserSettings extends Base {

    private String fullname;

    private String avatar;

    private Date birthDate;


    @DBRef
    private List<Photo> photoList;

    public List<Photo> getPhotoList() {
        if (photoList == null) {
            photoList = new ArrayList<>();
        }
        return photoList;
    }

    public String getFullname(){
        if(fullname == null){
            fullname =null;
        }
    return fullname;}

    public String getAvatar(){
        if(avatar == null){
            avatar =null;
        }
        return avatar;}

    public Date getBirthDate(){
        if(birthDate == null){
            birthDate =null;
        }
        return birthDate;}



}
