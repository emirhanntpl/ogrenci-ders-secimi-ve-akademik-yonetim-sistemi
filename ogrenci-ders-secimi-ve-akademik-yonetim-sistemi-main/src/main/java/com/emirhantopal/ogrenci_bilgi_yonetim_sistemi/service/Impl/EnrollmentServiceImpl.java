package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Course;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.CourseSection;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Enrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.CourseSectionRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.EnrollmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.StudentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IEnrollmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private CourseRepository courseRepository;

    @Override
    @Transactional
    public DtoEnrollment enrollStudent(DtoEnrollmentIU dtoIU) {
        Student student = studentRepository.findById(dtoIU.getStudentId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));

        CourseSection courseSection = courseSectionRepository.findById(dtoIU.getCourseSectionId())
                .orElseThrow(() -> new BaseException(MessageType.INVALID_COURSE_SECTION_ID, HttpStatus.BAD_REQUEST));

        Course course = courseSection.getCourse();

        // 1. Aynı derse mükerrer kayıt kontrolü
        List<Enrollment> existingEnrollments = enrollmentRepository.findByStudent(student);
        for (Enrollment e : existingEnrollments) {
            if (e.getCourseSection().getCourse().getId().equals(course.getId())) {
                throw new RuntimeException("Öğrenci bu derse (farklı bir şube olsa bile) zaten kayıtlı!");
            }
        }

        // 2. Kota kontrolü ve eksiltme
        if (course.getQuota() != null) {
            if (course.getQuota() <= 0) {
                throw new RuntimeException("Bu dersin kontenjanı dolmuştur!");
            }
            course.setQuota(course.getQuota() - 1);
            courseRepository.save(course);
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourseSection(courseSection);
        enrollment.setEnrollmentDate(LocalDateTime.now());

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return convertToDto(savedEnrollment);
    }

    @Override
    @Transactional
    public void dropCourse(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_ENROLLMENT_ID, HttpStatus.BAD_REQUEST));
        
        // Kayıt silindiğinde kotayı 1 artır
        Course course = enrollment.getCourseSection().getCourse();
        if (course.getQuota() != null) {
            course.setQuota(course.getQuota() + 1);
            courseRepository.save(course);
        }

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
        dto.setId(enrollment.getId());
        if (enrollment.getCreatedDate() != null) {
            dto.setCreatedDate(enrollment.getCreatedDate().toString());
        }

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
        dto.setId(student.getId());

        if (student.getDepartment() != null) {
            DtoDepartment dtoDepartment = new DtoDepartment();
            BeanUtils.copyProperties(student.getDepartment(), dtoDepartment);
            dtoDepartment.setId(student.getDepartment().getId());
            dto.setDepartment(dtoDepartment);
        }

        if (student.getUser() != null) {
            DtoUser dtoUser = new DtoUser();
            BeanUtils.copyProperties(student.getUser(), dtoUser);
            dtoUser.setId(student.getUser().getId());
            dto.setUser(dtoUser);
        }
        return dto;
    }

    private DtoCourseSection convertCourseSectionToDto(CourseSection courseSection) {
        DtoCourseSection dto = new DtoCourseSection();
        BeanUtils.copyProperties(courseSection, dto);
        dto.setId(courseSection.getId());

        if (courseSection.getCourse() != null) {
            DtoCourse dtoCourse = new DtoCourse();
            BeanUtils.copyProperties(courseSection.getCourse(), dtoCourse);
            dtoCourse.setId(courseSection.getCourse().getId());
            dto.setCourse(dtoCourse);
        }

        if (courseSection.getTeacher() != null) {
            DtoTeacher dtoTeacher = new DtoTeacher();
            BeanUtils.copyProperties(courseSection.getTeacher(), dtoTeacher);
            dtoTeacher.setId(courseSection.getTeacher().getId());
            if (courseSection.getTeacher().getDepartment() != null) {
                DtoDepartment dtoDepartment = new DtoDepartment();
                BeanUtils.copyProperties(courseSection.getTeacher().getDepartment(), dtoDepartment);
                dtoDepartment.setId(courseSection.getTeacher().getDepartment().getId());
                dtoTeacher.setDepartment(dtoDepartment);
            }
            dto.setTeacher(dtoTeacher);
        }

        if (courseSection.getClassroom() != null) {
            DtoClassroom dtoClassroom = new DtoClassroom();
            BeanUtils.copyProperties(courseSection.getClassroom(), dtoClassroom);
            dtoClassroom.setId(courseSection.getClassroom().getId());
            dto.setClassroom(dtoClassroom);
        }

        if (courseSection.getSemester() != null) {
            DtoSemester dtoSemester = new DtoSemester();
            BeanUtils.copyProperties(courseSection.getSemester(), dtoSemester);
            dtoSemester.setId(courseSection.getSemester().getId());
            dto.setSemester(dtoSemester);
        }

        return dto;
    }
}