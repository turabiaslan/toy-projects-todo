package com.beam.sample.phinstagram.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Accessors(chain = true)
public class GenericResponse {

    private boolean status;
}
