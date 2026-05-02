package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGrade;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoGradeIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Enrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Grade;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.EnrollmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.GradeRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IGradeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements IGradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Override
    @Transactional
    public DtoGrade gradeAdd(DtoGradeIU dtoGradeIU) {
        Grade grade = new Grade();
        BeanUtils.copyProperties(dtoGradeIU, grade);
        // Calculate average, letter grade and pass status
        calculateAndSetGradeDetails(grade);
        Grade savedGrade = gradeRepository.save(grade);
        return convertToDto(savedGrade);
    }

    @Override
    @Transactional
    public DtoGrade gradeUpdate(Long id, DtoGradeIU dtoGradeIU) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_GRADE_ID, HttpStatus.BAD_REQUEST));
        
        grade.setMidTerm(dtoGradeIU.getMidTerm());
        grade.setFinalExam(dtoGradeIU.getFinalExam());
        // Calculate average, letter grade and pass status
        calculateAndSetGradeDetails(grade);

        Grade savedGrade = gradeRepository.save(grade);
        return convertToDto(savedGrade);
    }

    @Override
    @Transactional
    public void gradeDelete(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_GRADE_ID, HttpStatus.BAD_REQUEST));
        gradeRepository.delete(grade);
    }

    @Override
    public List<DtoGrade> getAllGrades() {
        List<Grade> allGrades = gradeRepository.findAll();
        return allGrades.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public DtoGrade findByGradeId(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_GRADE_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(grade);
    }

    @Override
    public List<DtoGrade> getGradesByCourseSectionAndStudent(Long courseSectionId, Long studentId) {
        // Find enrollment first
        List<Enrollment> enrollments = enrollmentRepository.findByCourseSection_Id(courseSectionId);
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getStudent().getId().equals(studentId))
                .findFirst()
                .orElse(null);
                
        if (enrollment == null) {
            return new ArrayList<>();
        }
        
        List<Grade> grades = gradeRepository.findByEnrollment(enrollment);
        return grades.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DtoGrade updateStudentGrade(Long courseSectionId, Long studentId, String examType, Double gradeValue) {
        // Find enrollment first
        List<Enrollment> enrollments = enrollmentRepository.findByCourseSection_Id(courseSectionId);
        Enrollment enrollment = enrollments.stream()
                .filter(e -> e.getStudent().getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ENROLLMENT_ID, HttpStatus.BAD_REQUEST));

        List<Grade> grades = gradeRepository.findByEnrollment(enrollment);
        Grade grade;
        
        if (grades.isEmpty()) {
            grade = new Grade();
            grade.setEnrollment(enrollment);
        } else {
            grade = grades.get(0);
        }

        if ("MIDTERM".equalsIgnoreCase(examType)) {
            grade.setMidTerm(gradeValue);
        } else if ("FINAL".equalsIgnoreCase(examType)) {
            grade.setFinalExam(gradeValue);
        }
        
        // Calculate average, letter grade and pass status
        calculateAndSetGradeDetails(grade);
        Grade savedGrade = gradeRepository.save(grade);
        return convertToDto(savedGrade);
    }

    private void calculateAndSetGradeDetails(Grade grade) {
        Double midterm = grade.getMidTerm() != null ? grade.getMidTerm() : 0.0;
        Double finalExam = grade.getFinalExam() != null ? grade.getFinalExam() : 0.0;
        Double average;
        String letterGrade;
        String passStatus;

        // Final notu 45'in altında ise doğrudan KALDI
        if (finalExam < 45.0) {
            average = (midterm * 0.4) + (finalExam * 0.6); // Ortalama yine de hesaplanır
            letterGrade = "FF";
            passStatus = "KALDI";
        } else {
            average = (midterm * 0.4) + (finalExam * 0.6);
            letterGrade = calculateLetterGrade(average);
            passStatus = (average >= 50.0) ? "GEÇTİ" : "KALDI";
        }

        grade.setAverage(average);
        grade.setLetterGrade(letterGrade);
        grade.setPassStatus(passStatus);
    }

    private String calculateLetterGrade(Double average) {
        if (average >= 90) {
            return "AA";
        } else if (average >= 85) {
            return "BA";
        } else if (average >= 80) {
            return "BB";
        } else if (average >= 75) {
            return "CB";
        } else if (average >= 70) {
            return "CC";
        } else if (average >= 60) {
            return "DC";
        } else if (average >= 50) {
            return "DD";
        } else if (average >= 40) {
            return "FD";
        } else {
            return "FF";
        }
    }

    private DtoGrade convertToDto(Grade grade) {
        DtoGrade dto = new DtoGrade();
        BeanUtils.copyProperties(grade, dto);
        dto.setId(grade.getId());
        dto.setLetterGrade(grade.getLetterGrade()); // Yeni eklendi
        dto.setPassStatus(grade.getPassStatus());   // Yeni eklendi
        return dto;
    }
}
