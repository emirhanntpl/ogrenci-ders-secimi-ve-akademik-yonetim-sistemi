package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncementIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Announcement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.AnnouncementRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.StudentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.EmailService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAnnouncementService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AnnouncementService implements IAnnouncementService {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Override
    @Transactional
    public DtoAnnouncement addAnnouncement(DtoAnnouncementIU dtoAnnouncementIU) {
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(dtoAnnouncementIU, announcement);
        announcement.setPublishDate(LocalDateTime.now());
        
        Announcement savedAnnouncement = announcementRepository.save(announcement);

        // Duyuru eklendiğinde tüm öğrencilere e-posta gönder
        List<Student> students = studentRepository.findAll();
        
        new Thread(() -> {
            for (Student student : students) {
                if (student.getEmail() != null && !student.getEmail().isEmpty()) {
                    String studentName = student.getFirstName() + " " + student.getLastName();
                    try {
                        emailService.sendAnnouncementEmail(
                            student.getEmail(), 
                            studentName, 
                            savedAnnouncement.getTitle(), 
                            savedAnnouncement.getContent(),
                            savedAnnouncement.getAuthor()
                        );
                    } catch (Exception e) {
                        System.err.println("Duyuru e-postası gönderilemedi: " + e.getMessage());
                    }
                }
            }
        }).start();

        return convertToDto(savedAnnouncement);
    }

    @Override
    @Transactional
    public DtoAnnouncement updateAnnouncement(Long id, DtoAnnouncementIU dtoAnnouncementIU) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ANNOUNCEMENT_ID, HttpStatus.BAD_REQUEST));
        
        announcement.setTitle(dtoAnnouncementIU.getTitle());
        announcement.setContent(dtoAnnouncementIU.getContent());
        announcement.setAuthor(dtoAnnouncementIU.getAuthor());
        
        Announcement updatedAnnouncement = announcementRepository.save(announcement);
        return convertToDto(updatedAnnouncement);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ANNOUNCEMENT_ID, HttpStatus.BAD_REQUEST));
        announcementRepository.delete(announcement);
    }

    @Override
    public DtoAnnouncement getAnnouncementById(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ANNOUNCEMENT_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(announcement);
    }

    @Override
    public List<DtoAnnouncement> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();
        List<DtoAnnouncement> dtoAnnouncements = new ArrayList<>();
        for (Announcement a : announcements) {
            dtoAnnouncements.add(convertToDto(a));
        }
        return dtoAnnouncements;
    }

    private DtoAnnouncement convertToDto(Announcement announcement) {
        DtoAnnouncement dto = new DtoAnnouncement();
        BeanUtils.copyProperties(announcement, dto);
        dto.setId(announcement.getId());
        if (announcement.getPublishDate() != null) {
            dto.setPublishDate(announcement.getPublishDate().toString());
        }
        if (announcement.getCreatedDate() != null) {
            dto.setCreatedDate(announcement.getCreatedDate().toString());
        }
        return dto;
    }
}
