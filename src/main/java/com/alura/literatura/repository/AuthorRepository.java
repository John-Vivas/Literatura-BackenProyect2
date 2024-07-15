package com.alura.literatura.repository;

import com.alura.literatura.model.Author;
import com.alura.literatura.model.Book;
import com.alura.literatura.model.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query("SELECT a FROM Book l JOIN l.author a WHERE a.name LIKE %:name%")
    Optional<Author> SearchAutorName( String name);

    @Query("SELECT l FROM Book l JOIN l.author a WHERE l.title LIKE %:name%")
    Optional<Book> SearchLibroName( String name);



    @Query("SELECT l FROM Author a JOIN a.books l")
    List<Book> SearchLibrosRegistrados();

    @Query("SELECT a FROM Author a WHERE a.deathDate > :fecha")
    List<Author>SearchAutoresAlive(String fecha);

    @Query("SELECT l FROM Author a JOIN a.books l WHERE l.language = :language ")
    List<Book> SearchLibrosLanguage(@Param("language") Language language);




}
