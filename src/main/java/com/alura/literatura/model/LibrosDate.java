package com.alura.literatura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LibrosDate(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors")  List <AuthorDate> autor,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count")   Double numeroDeDescargas
                         ) {


}
