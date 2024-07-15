package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Date ( @JsonAlias("results")
                     List<LibrosDate> books ,
    @JsonAlias("death_year")
    List<LibrosDate> deathDate
){


}
