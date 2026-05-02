package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.controller.IStudentController;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IStudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/student")
@CrossOrigin(origins = "*")
public class StudentController implements IStudentController {

    private final IStudentService studentService;

    @Autowired
    public StudentController(IStudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/add")
    @Override
    public DtoStudent studentAdd(@Valid @RequestBody DtoStudentIU dtoStudentIU) {
        return studentService.studentAdd(dtoStudentIU);
    }

    @PutMapping("/update/{id}")
    @Override
    public DtoStudent studentUpdate(@PathVariable(name = "id") Long id, @Valid @RequestBody DtoStudentIU dtoStudentIU) {
        return studentService.studentUpdate(id, dtoStudentIU);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public void studentDelete(@PathVariable(name = "id") Long id) {
        studentService.studentDelete(id);
    }

    @GetMapping("/getAll")
    @Override
    public List<DtoStudent> getAllStudent() {
        return studentService.getAllStudents();
    }

    @GetMapping("/getById/{id}")
    @Override
    public DtoStudent getStudentById(@PathVariable(name = "id") Long id) {
        return studentService.findByStudentId(id);
    }

    @GetMapping("/by-username")
    @Override
    public DtoStudent getStudentByUsername(@RequestParam(name = "username") String username) {
        return studentService.findByUsername(username);
    }

    @PutMapping("/update-profile/{id}")
    @Override
    public DtoStudent updateStudentProfile(@PathVariable(name = "id") Long id, @RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String telNumber = payload.get("telNumber");
        String newPassword = payload.get("newPassword");
        return studentService.updateStudentProfile(id, email, telNumber, newPassword);
    }
}