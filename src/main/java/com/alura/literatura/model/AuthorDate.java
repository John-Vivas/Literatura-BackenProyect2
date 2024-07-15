package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AuthorDate(
       @JsonAlias("name") String nombre,
      @JsonAlias("birth_year")  String fechaDeNacimiento,
       @JsonAlias("death_year")  String fechaDeFallecimieto


) {

}
