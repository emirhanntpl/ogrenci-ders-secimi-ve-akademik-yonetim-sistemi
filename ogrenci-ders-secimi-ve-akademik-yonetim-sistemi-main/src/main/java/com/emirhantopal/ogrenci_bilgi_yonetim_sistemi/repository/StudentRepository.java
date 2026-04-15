package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {










}
