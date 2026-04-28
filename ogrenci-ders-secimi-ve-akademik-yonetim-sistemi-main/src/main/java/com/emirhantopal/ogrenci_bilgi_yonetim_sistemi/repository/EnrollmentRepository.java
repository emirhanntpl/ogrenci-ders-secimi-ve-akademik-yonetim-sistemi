package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Enrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student); // Bu metot eklendi
}
