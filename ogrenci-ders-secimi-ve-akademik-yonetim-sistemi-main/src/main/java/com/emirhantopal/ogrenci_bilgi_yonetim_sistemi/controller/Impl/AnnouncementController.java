package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IAnnouncementController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncement;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoAnnouncementIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/announcement")
@CrossOrigin(origins = "*")
public class AnnouncementController implements IAnnouncementController {

    @Autowired
    private IAnnouncementService announcementService;

    @PostMapping("/add")
    @Override
    public DtoAnnouncement addAnnouncement(@RequestBody DtoAnnouncementIU dtoAnnouncementIU) {
        return announcementService.addAnnouncement(dtoAnnouncementIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoAnnouncement updateAnnouncement(@PathVariable Long id, @RequestBody DtoAnnouncementIU dtoAnnouncementIU) {
        return announcementService.updateAnnouncement(id, dtoAnnouncementIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void deleteAnnouncement(@PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
    }

    @GetMapping("/get/{id}")
    @Override
    public DtoAnnouncement getAnnouncementById(@PathVariable Long id) {
        return announcementService.getAnnouncementById(id);
    }

    @GetMapping("/all")
    @Override
    public List<DtoAnnouncement> getAllAnnouncements() {
        return announcementService.getAllAnnouncements();
    }
}
