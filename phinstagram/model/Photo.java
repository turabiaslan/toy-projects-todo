package com.beam.sample.phinstagram.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class Photo extends Base{

    private Date date;

    private String caption;

    private String path;

    private String username;

    private String avatar;






}
