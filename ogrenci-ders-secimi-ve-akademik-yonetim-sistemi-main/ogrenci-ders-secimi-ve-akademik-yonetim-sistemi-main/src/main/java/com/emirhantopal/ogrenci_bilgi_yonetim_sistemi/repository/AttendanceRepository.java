package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
