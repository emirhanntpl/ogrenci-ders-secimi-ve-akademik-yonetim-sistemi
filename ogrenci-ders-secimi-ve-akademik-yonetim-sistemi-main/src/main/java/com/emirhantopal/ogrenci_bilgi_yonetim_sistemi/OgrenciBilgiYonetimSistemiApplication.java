package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.enums.Role;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.User;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class OgrenciBilgiYonetimSistemiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OgrenciBilgiYonetimSistemiApplication.class, args);
    }

    @Bean
    public CommandLineRunner createInitialAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername(  "admin");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Varsayılan admin kullanıcısı oluşturuldu: admin/admin");
            }
        };
    }
}
