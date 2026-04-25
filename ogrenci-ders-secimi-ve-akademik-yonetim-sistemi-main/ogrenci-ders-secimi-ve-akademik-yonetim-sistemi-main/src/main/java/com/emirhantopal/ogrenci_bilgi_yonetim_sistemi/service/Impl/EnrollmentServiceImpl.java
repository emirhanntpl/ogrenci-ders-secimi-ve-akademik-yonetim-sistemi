package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.CourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Enrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseSectionRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.EnrollmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.StudentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IEnrollmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements IEnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Override
    public DtoEnrollment enrollStudent(DtoEnrollmentIU dtoIU) {
        Student student = studentRepository.findById(dtoIU.getStudentId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));

        CourseSection courseSection = courseSectionRepository.findById(dtoIU.getCourseSectionId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_SECTION_ID, HttpStatus.BAD_REQUEST));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourseSection(courseSection);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return convertToDto(savedEnrollment);
    }

    @Override
    public void dropCourse(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ENROLLMENT_ID, HttpStatus.BAD_REQUEST));
        enrollmentRepository.delete(enrollment);
    }

    @Override
    public List<DtoEnrollment> getAllEnrollments() {
        List<Enrollment> allEnrollments = enrollmentRepository.findAll();
        if (allEnrollments.isEmpty()) {
            throw new BaseException(MessageType.ENROLLMENT_LIST_IS_EMPTY, HttpStatus.BAD_REQUEST);
        }

        List<DtoEnrollment> dtoList = new ArrayList<>();
        for (Enrollment enrollment : allEnrollments) {
            dtoList.add(convertToDto(enrollment));
        }
        return dtoList;
    }

    @Override
    public DtoEnrollment findEnrollmentById(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ENROLLMENT_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(enrollment);
    }

    // --- HELPER METHODS ---

    private DtoEnrollment convertToDto(Enrollment enrollment) {
        DtoEnrollment dto = new DtoEnrollment();
        BeanUtils.copyProperties(enrollment, dto);

        if (enrollment.getStudent() != null) {
            dto.setStudent(convertStudentToDto(enrollment.getStudent()));
        }

        if (enrollment.getCourseSection() != null) {
            dto.setCourseSection(convertCourseSectionToDto(enrollment.getCourseSection()));
        }

        return dto;
    }

    private DtoStudent convertStudentToDto(Student student) {
        DtoStudent dto = new DtoStudent();
        BeanUtils.copyProperties(student, dto);

        if (student.getDepartment() != null) {
            DtoDepartment dtoDepartment = new DtoDepartment();
            BeanUtils.copyProperties(student.getDepartment(), dtoDepartment);
            dto.setDepartment(dtoDepartment);
        }

        if (student.getUser() != null) {
            DtoUser dtoUser = new DtoUser();
            BeanUtils.copyProperties(student.getUser(), dtoUser);
            dto.setUser(dtoUser);
        }
        return dto;
    }

    private DtoCourseSection convertCourseSectionToDto(CourseSection courseSection) {
        DtoCourseSection dto = new DtoCourseSection();
        BeanUtils.copyProperties(courseSection, dto);

        if (courseSection.getCourse() != null) {
            DtoCourse dtoCourse = new DtoCourse();
            BeanUtils.copyProperties(courseSection.getCourse(), dtoCourse);
            dto.setCourse(dtoCourse);
        }

        if (courseSection.getTeacher() != null) {
            DtoTeacher dtoTeacher = new DtoTeacher();
            BeanUtils.copyProperties(courseSection.getTeacher(), dtoTeacher);
            if (courseSection.getTeacher().getDepartment() != null) {
                DtoDepartment dtoDepartment = new DtoDepartment();
                BeanUtils.copyProperties(courseSection.getTeacher().getDepartment(), dtoDepartment);
                dtoTeacher.setDepartment(dtoDepartment);
            }
            dto.setTeacher(dtoTeacher);
        }

        if (courseSection.getClassroom() != null) {
            DtoClassroom dtoClassroom = new DtoClassroom();
            BeanUtils.copyProperties(courseSection.getClassroom(), dtoClassroom);
            dto.setClassroom(dtoClassroom);
        }

        if (courseSection.getSemester() != null) {
            DtoSemester dtoSemester = new DtoSemester();
            BeanUtils.copyProperties(courseSection.getSemester(), dtoSemester);
            dto.setSemester(dtoSemester);
        }

        return dto;
    }
}