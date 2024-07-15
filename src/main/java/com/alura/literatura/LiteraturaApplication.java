package com.alura.literatura;

import com.alura.literatura.main.Main;

import com.alura.literatura.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication


public class LiteraturaApplication implements CommandLineRunner {


	private final AuthorRepository repository;
	@Autowired
    public LiteraturaApplication(AuthorRepository repository) {
        this.repository = repository;
    }


    public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(repository);
		main.ShowToClient();
	}
}

