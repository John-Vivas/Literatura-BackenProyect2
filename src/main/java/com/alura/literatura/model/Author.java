package com.alura.literatura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "autores")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    private String birthYear;
    private  String deathDate;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Book> books;

    public Author (){

    }
    public Author(AuthorDate authorDate){
        this.name = authorDate.nombre();
        this.birthYear = authorDate.fechaDeNacimiento();
        this.deathDate = authorDate.fechaDeFallecimieto();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getDeathDate() {
        return deathDate;
    }

    public void setDeathDate(String deathDate) {
        this.deathDate = deathDate;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        books.forEach(l -> l.setAuthor(this));
        this.books = books;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthYear='" + birthYear + '\'' +
                ", deathDate='" + deathDate + '\'' +
                ", books=" + books +
                '}';
    }
}
