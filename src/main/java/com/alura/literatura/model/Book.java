package com.alura.literatura.model;

import jakarta.persistence.*;

import java.util.stream.Collectors;

@Entity
@Table(name = "libros")
public class Book {
    @Id
    private Long id;
    private String title;
    @Enumerated(EnumType.STRING)
    private Language language;
    private double download;
    @ManyToOne
    @JoinColumn(name = "author_id")
    private  Author author;
    public Book (){}

    public Book(LibrosDate librosDate){
        this.id = librosDate.id();
        this.title = librosDate.titulo();
        this.language = Language.fromString(librosDate.idiomas().stream()
                .limit(1).collect(Collectors.joining()));

        this.download = librosDate.numeroDeDescargas();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public double getDownload() {
        return download;
    }

    public void setDownload(double download) {
        this.download = download;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", language=" + language +
                ", download=" + download +
                ", author=" + author +
                '}';
    }
}

