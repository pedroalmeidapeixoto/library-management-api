package com.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BibliotecaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaApplication.class, args);
        System.out.println(" Library Application started successfully!");
        System.out.println(" Available Endpoints:");
        System.out.println("   Livros: http://localhost:8080/api/livros");
        System.out.println("   Usuários: http://localhost:8080/api/usuarios");
        System.out.println("/emprestimos/{id}/devolver");     // Procedure
        System.out.println("/usuarios/{id}/multas");        // Function 1
        System.out.println("/exemplares/{id}/disponivel");    // Function 2
    }
}
