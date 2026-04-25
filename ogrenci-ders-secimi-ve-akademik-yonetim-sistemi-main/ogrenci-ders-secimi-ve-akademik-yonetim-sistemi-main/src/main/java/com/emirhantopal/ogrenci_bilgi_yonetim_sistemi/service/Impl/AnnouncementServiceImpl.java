package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncementIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Announcement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Teacher;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.AnnouncementRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.TeacherRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementServiceImpl implements IAnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public DtoAnnouncement addAnnouncement(DtoAnnouncementIU dtoAnnouncementIU) {
        Announcement announcement = new Announcement();
        announcement.setTitle(dtoAnnouncementIU.getTitle());
        announcement.setContent(dtoAnnouncementIU.getContent());

        if (dtoAnnouncementIU.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dtoAnnouncementIU.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Öğretmen bulunamadı"));
            announcement.setTeacher(teacher);
        } else {
            throw new RuntimeException("Duyuru için öğretmen ID'si gereklidir");
        }

        Announcement savedAnnouncement = announcementRepository.save(announcement);
        return convertToDto(savedAnnouncement);
    }

    @Override
    public List<DtoAnnouncement> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();
        List<DtoAnnouncement> dtoList = new ArrayList<>();
        
        for (Announcement ann : announcements) {
            dtoList.add(convertToDto(ann));
        }
        
        return dtoList;
    }

    @Override
    public DtoAnnouncement getAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Duyuru bulunamadı"));
        return convertToDto(announcement);
    }

    @Override
    public void deleteAnnouncement(Long id) {
        if (!announcementRepository.existsById(id)) {
            throw new RuntimeException("Silinecek duyuru bulunamadı");
        }
        announcementRepository.deleteById(id);
    }

    private DtoAnnouncement convertToDto(Announcement announcement) {
        DtoAnnouncement dto = new DtoAnnouncement();
        dto.setId(announcement.getId());
        dto.setTitle(announcement.getTitle());
        dto.setContent(announcement.getContent());
        dto.setCreatedDate(announcement.getCreatedDate());
        
        if (announcement.getTeacher() != null) {
            dto.setTeacherFirstName(announcement.getTeacher().getFirstName());
            dto.setTeacherLastName(announcement.getTeacher().getLastName());
        }
        
        return dto;
    }
}