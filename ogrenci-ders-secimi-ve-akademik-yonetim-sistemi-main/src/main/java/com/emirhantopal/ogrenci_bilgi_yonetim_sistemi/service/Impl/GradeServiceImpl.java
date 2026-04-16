package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGrade;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGradeIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Grade;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.GradeRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IGradeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GradeServiceImpl implements IGradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public DtoGrade gradeAdd(DtoGradeIU dtoGradeIU) {
        Grade grade = new Grade();
        DtoGrade dtoGrade = new DtoGrade();

        BeanUtils.copyProperties(dtoGradeIU, grade);

        Grade savedGrade = gradeRepository.save(grade);
        BeanUtils.copyProperties(savedGrade, dtoGrade);

        return dtoGrade;
    }

    @Override
    public DtoGrade gradeUpdate(Long id, DtoGradeIU dtoGradeIU) {

        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NOT ID BULUNAMADI: " + id));

        DtoGrade dtoGrade = new DtoGrade();


        grade.setMidTerm(dtoGradeIU.getMidTerm());
        grade.setFinalExam(dtoGradeIU.getFinalExam());
        grade.setAverage(dtoGradeIU.getAverage());

        Grade savedGrade = gradeRepository.save(grade);
        BeanUtils.copyProperties(savedGrade, dtoGrade);

        return dtoGrade;
    }

    @Override
    public void gradeDelete(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NOT ID BULUNAMADI: " + id));
        gradeRepository.delete(grade);
        System.out.println("Not kaydı silindi. " + id);
    }

    @Override
    public List<DtoGrade> getAllGrades() {
        List<Grade> allGrades = gradeRepository.findAll();
        
        if (allGrades.isEmpty()) {
            throw new RuntimeException("NOT LİSTESİ BOŞ");
        }
        
        List<DtoGrade> dtoGrades = new ArrayList<>();
        
        for (Grade grade : allGrades) {
            DtoGrade dtoGrade = new DtoGrade();
            BeanUtils.copyProperties(grade, dtoGrade);
            dtoGrades.add(dtoGrade);
        }
        
        return dtoGrades;
    }

    @Override
    public DtoGrade findByGradeId(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NOT ID BULUNAMADI: " + id));

        DtoGrade dtoGrade = new DtoGrade();
        BeanUtils.copyProperties(grade, dtoGrade);
        
        return dtoGrade;
    }
}