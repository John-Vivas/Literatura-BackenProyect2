
package com.alura.literatura.main;

import com.alura.literatura.model.*;
import com.alura.literatura.model.Date;
import com.alura.literatura.repository.AuthorRepository;
import com.alura.literatura.service.ConsultaAPI;
import com.alura.literatura.service.ConvierteDatosAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Main {
    private final ConsultaAPI consultaAPI;
    private final ConvierteDatosAPI convierteDatosAPI;
    private final AuthorRepository repository;

    private static final String URL_BOOKS = "https://gutendex.com/books/";



    private final Scanner readBusqueda = new Scanner(System.in);
    @Autowired
    public Main(AuthorRepository repository) {
        this.repository = repository;
        this.consultaAPI = new ConsultaAPI();
        this.convierteDatosAPI = new ConvierteDatosAPI();



    }

    public void ShowToClient() {
        Scanner read = new Scanner(System.in);

        var json = consultaAPI.getLibro(URL_BOOKS);
        System.out.println(json);
        var date = convierteDatosAPI.lockingLibro(json, Date.class);

        // Libros más descargados lógica
        System.out.println("\nLibros más descargados\n");
        date.books().stream()
                .sorted(Comparator.comparing(LibrosDate::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);
        System.out.println("######################################");

        while (true) {
            showMenu();

            try {
                int currencyChoice = read.nextInt();
                read.nextLine();

                if (currencyChoice == 6) {
                    System.out.println("Saliendo...");
                    break;
                }

                if (currencyChoice < 1 || currencyChoice > 6) {
                    System.out.println("Opción incorrecta. Por favor, ingrese del 1 al 6.");
                    continue;
                }
                switch (currencyChoice) {
                    case 1:
                        searchLibros();
                        break;
                    case 2:
                        booksRegistrados();
                        break;
                    case 3:
                        authorsRegistrados();
                        break;
                    case 4:
                        authorsLifeDate();
                        break;
                    case 5:
                        booksForIdioma();
                        break;
                    default:
                        System.out.println("Opción no encontrada.");
                        return;
                }

            } catch (InputMismatchException e) {
                System.out.println("Error debes ingresar un numero entre 1 al 6. NO OTRO CARACTER :)");
                read.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("Divisa no encontrada: " + e.getMessage());
            }
        }

        read.close();
        readBusqueda.close();
    }

    // Búsqueda de libro
    private void searchLibros() {
        System.out.println("Ingresa el nombre del libro deseado: ");
        var titleBook = readBusqueda.nextLine();
        var json = consultaAPI.getLibro(URL_BOOKS + "?search=" + titleBook.replace(" ", ""));
        var searchBook = convierteDatosAPI.lockingLibro(json, Date.class);
        Optional<LibrosDate> libroSearch = searchBook.books().stream()
                .filter(l -> l.titulo().toUpperCase().contains(titleBook.toUpperCase()))
                .findFirst();
        if (libroSearch.isPresent()) {
            System.out.println("Libro encontrado" +
                    "\n----- LIBRO -----" +
                    "\nTitulo: " + libroSearch.get().titulo() +
                    "\nAutor: " + libroSearch.get().autor().stream()
                    .map(a -> a.nombre()).limit(1).collect(Collectors.joining()) +
                    "\nIdioma: " + libroSearch.get().idiomas().stream().collect(Collectors.joining()) +
                    "\nNumero de descargas: " + libroSearch.get().numeroDeDescargas() +
                    "\n-----------------\n");

            try {
                List<Book> libroEncontrado = libroSearch.stream()
                        .map(Book::new)
                        .collect(Collectors.toList());
                Author autorAPI = libroSearch.stream()
                        .flatMap(l -> l.autor().stream().map(Author::new))
                        .findFirst().orElseThrow();
                Optional<Author> autorBD = repository.SearchAutorName(libroSearch.get().autor().stream()
                        .map(a -> a.nombre())
                        .collect(Collectors.joining()));
                Optional<Book> libroOptional = repository.SearchLibroName(autorAPI.getName());
                if (libroOptional.isPresent()) {
                    System.out.println("El libro ya está guardado en la base de datos.");
                } else {
                    Author autor;
                    if (autorBD.isPresent()) {
                        autor = autorBD.get();
                        System.out.println("El autor ya está guardado en la BD!");
                    } else {
                        autor = autorAPI;
                        repository.save(autor);
                    }
                    autor.setBooks(libroEncontrado);
                    repository.save(autor);
                }
            } catch (Exception e) {
                System.out.println("Advertencia! " + e.getMessage());
            }
        } else {
            System.out.println("Libro no encontrado!");
        }
    }

    // Función para mostrar los libros registrados
    private void booksRegistrados() {
        System.out.println("\nLos libros registrados son: \n");
        List<Book> libros = repository.SearchLibrosRegistrados();
        libros.forEach(l -> System.out.println(
                "----- LIBRO -----" +
                        "\nTitulo: " + l.getTitle() +
                        "\nAutor: " + l.getAuthor().getName() +
                        "\nIdioma: " + l.getLanguage().getIdioma() +
                        "\nNumero de descargas: " + l.getDownload() +
                        "\n-----------------\n"
        ));
    }

    // Función para mostrar los autores registrados
    private void authorsRegistrados() {
        System.out.println("\nLos autores registrados son: \n");
        List<Author> autores = repository.findAll();
        autores.forEach(l -> System.out.println(
                "Autor: " + l.getName() +
                        "\nFecha de nacimiento: " + l.getBirthYear() +
                        "\nFecha de fallecimiento: " + l.getDeathDate() +
                        "\nLibros: " + l.getBooks().stream()
                        .map(Book::getTitle)
                        .collect(Collectors.joining(", ")) + "\n"
        ));
    }

    // Función para mostrar los autores vivos por fecha
    private void authorsLifeDate() {
        System.out.println("\nLos autores vivos por fecha específica: \n");
        System.out.println("Ingrese el año que deseas buscar: ");
        try {
            var fecha = readBusqueda.nextLine();
            List<Author> autores = repository.SearchAutoresAlive(fecha);
            if (!autores.isEmpty()) {
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getName() +
                                "\nFecha de nacimiento: " + a.getBirthYear() +
                                "\nFecha de fallecimiento: " + a.getDeathDate() +
                                "\nLibros: " + a.getBooks().stream()
                                .map(Book::getTitle)
                                .collect(Collectors.joining(", ")) + "\n"
                ));
            } else {
                System.out.println("No hay autores vivos en ese año registrados específicamente!");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un año válido: " + e.getMessage());
        }
    }

    // Función para mostrar libros por idioma
    private void booksForIdioma() {
        System.out.println("Seleccione el idioma deseado: \n");
        var menu = """
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """;
        System.out.println(menu);

        var idioma = readBusqueda.nextLine();
        if (idioma.equalsIgnoreCase("es") || idioma.equalsIgnoreCase("en") ||
                idioma.equalsIgnoreCase("fr") || idioma.equalsIgnoreCase("pt")) {
            Language lenguaje = Language.fromString(idioma);
            List<Book> libros = repository.SearchLibrosLanguage(lenguaje);
            if (libros.isEmpty()) {
                System.out.println("No hay libros registrados en ese idioma!");
            } else {
                libros.forEach(l -> System.out.println(
                        "----- LIBRO -----" +
                                "\nTitulo: " + l.getTitle() +
                                "\nAutor: " + l.getAuthor().getName() +
                                "\nIdioma: " + l.getLanguage().getIdioma() +
                                "\nNumero de descargas: " + l.getDownload() +
                                "\n-----------------\n"
                ));
            }
        } else {
            System.out.println("Introduce un idioma en el formato válido");
        }
    }

    // Menú que se le va a presentar al cliente
    private static void showMenu() {
        System.out.println(
                "\n1-buscar libro por título \n" +
                        "2-listar libros registrados \n" +
                        "3-listar autores registrados \n" +
                        "4-listar autores vivos en un determinado año \n" +
                        "5-listar libros por idioma \n" +
                        "6-salir \n"
        );
        System.out.println("Escoge la opción deseada: ");
        System.out.println("_________________________");
    }
}
