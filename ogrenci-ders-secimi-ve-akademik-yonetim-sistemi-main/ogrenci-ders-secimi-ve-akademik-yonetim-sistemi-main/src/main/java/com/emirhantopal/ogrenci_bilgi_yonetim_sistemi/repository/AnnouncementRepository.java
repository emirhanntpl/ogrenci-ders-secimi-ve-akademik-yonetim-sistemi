package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}