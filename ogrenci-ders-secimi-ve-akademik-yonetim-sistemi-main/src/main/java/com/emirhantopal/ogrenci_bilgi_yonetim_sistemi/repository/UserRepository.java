package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}