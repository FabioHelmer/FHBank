package br.com.fhbank;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminPasswordGenerator {

    @Bean
    CommandLineRunner gerarSenhaAdmin(PasswordEncoder passwordEncoder) {
        return args -> {
            String senhaPlana = "admin123"; // <<< escolha a senha aqui
            String senhaHash = passwordEncoder.encode(senhaPlana);

            System.out.println("======================================");
            System.out.println("SENHA ADMIN (BCrypt):");
            System.out.println(senhaHash);
            System.out.println("======================================");
        };
    }
}
