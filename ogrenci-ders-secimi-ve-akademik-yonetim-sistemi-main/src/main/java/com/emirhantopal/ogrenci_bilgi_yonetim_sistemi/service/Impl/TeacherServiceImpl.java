package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.enums.Role;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.*;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.ITeacherService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements ITeacherService {
    
    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseSectionRepository courseSectionRepository;
    private final EnrollmentRepository enrollmentRepository;

    public TeacherServiceImpl(TeacherRepository teacherRepository, DepartmentRepository departmentRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, CourseSectionRepository courseSectionRepository, EnrollmentRepository enrollmentRepository) {
        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseSectionRepository = courseSectionRepository;
        this.enrollmentRepository = enrollmentRepository;
    }
    
    @Override
    @Transactional
    public DtoTeacher teacherAdd(DtoTeacherIU dtoTeacherIU) {
        if (dtoTeacherIU.getRegistrationNumber() == null || dtoTeacherIU.getRegistrationNumber().trim().isEmpty()) {
            throw new RuntimeException("Sicil numarası boş bırakılamaz!");
        }

        // UNIQUE KONTROLÜ
        Optional<Teacher> existByRegNo = teacherRepository.findByRegistrationNumber(dtoTeacherIU.getRegistrationNumber());
        if (existByRegNo.isPresent()) {
            throw new RuntimeException("Bu sicil numarası (" + dtoTeacherIU.getRegistrationNumber() + ") başka bir öğretmene ait!");
        }

        Teacher teacher = new Teacher();
        teacher.setFirstName(dtoTeacherIU.getFirstName());
        teacher.setLastName(dtoTeacherIU.getLastName());
        teacher.setRegistrationNumber(dtoTeacherIU.getRegistrationNumber());

        if (dtoTeacherIU.getDepartmentId() != null && dtoTeacherIU.getDepartmentId() > 0) {
            Department department = departmentRepository.findById(dtoTeacherIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            teacher.setDepartment(department);
        }

        // KULLANICI OLUŞTURMA
        String username = dtoTeacherIU.getRegistrationNumber(); // Sicil no = username
        Optional<User> existingUser = userRepository.findByUsername(username);
        User user;
        if(existingUser.isPresent()){
            user = existingUser.get();
            final Long finalUserId = user.getId();
            Optional<Teacher> existingTeacherWithThisUser = teacherRepository.findAll().stream()
                .filter(t -> t.getUser() != null && t.getUser().getId().equals(finalUserId))
                .findFirst();
            if (existingTeacherWithThisUser.isPresent()) {
                throw new RuntimeException("Bu kullanıcı hesabı (" + username + ") zaten başka bir öğretmene tanımlı!");
            }
        } else {
            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(username)); // Sicil no = şifre
            user.setRole(Role.TEACHER);
            user = userRepository.save(user);
        }
        teacher.setUser(user);
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToDto(savedTeacher);
    }

    @Override
    @Transactional
    public DtoTeacher teacherUpdate(Long id, DtoTeacherIU dtoTeacherIU) {
        if (dtoTeacherIU.getRegistrationNumber() == null || dtoTeacherIU.getRegistrationNumber().trim().isEmpty()) {
            throw new RuntimeException("Sicil numarası boş bırakılamaz!");
        }

        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));

        // UNIQUE KONTROLÜ (Kendi ID'si hariç)
        Optional<Teacher> existByRegNo = teacherRepository.findByRegistrationNumber(dtoTeacherIU.getRegistrationNumber());
        if (existByRegNo.isPresent() && !existByRegNo.get().getId().equals(id)) {
            throw new RuntimeException("Bu sicil numarası başka bir öğretmene ait!");
        }

        teacher.setFirstName(dtoTeacherIU.getFirstName());
        teacher.setLastName(dtoTeacherIU.getLastName());
        teacher.setRegistrationNumber(dtoTeacherIU.getRegistrationNumber());

        if (dtoTeacherIU.getDepartmentId() != null && dtoTeacherIU.getDepartmentId() > 0) {
            Department department = departmentRepository.findById(dtoTeacherIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            teacher.setDepartment(department);
        } else {
            teacher.setDepartment(null);
        }

        // Kullanıcı Adı Güncellemesi
        String newUsername = dtoTeacherIU.getRegistrationNumber();
        if (teacher.getUser() == null || !teacher.getUser().getUsername().equals(newUsername)) {
             Optional<User> existingUser = userRepository.findByUsername(newUsername);
             User user;
             if(existingUser.isPresent()){
                 user = existingUser.get();
             } else {
                 user = new User();
                 user.setUsername(newUsername);
                 user.setPassword(passwordEncoder.encode(newUsername));
                 user.setRole(Role.TEACHER);
                 user = userRepository.save(user);
             }
             teacher.setUser(user);
        }
        
        Teacher savedTeacher = teacherRepository.save(teacher);
        return convertToDto(savedTeacher);
    }

    @Override
    @Transactional
    public void teacherDelete(Long id) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
        
        List<CourseSection> sections = courseSectionRepository.findByTeacher_Id(id);
        for (CourseSection section : sections) {
            section.setTeacher(null);
            courseSectionRepository.save(section);
        }

        User user = teacher.getUser();
        if (user != null) {
            teacher.setUser(null);
            teacherRepository.save(teacher);
            userRepository.delete(user);
        }
        teacherRepository.delete(teacher);
    }

    @Override
    public List<DtoTeacher> teacherAll() {
        List<Teacher> allTeacher = teacherRepository.findAll();
        List<DtoTeacher> dtoTeachers = new ArrayList<>();
        for (Teacher teacher :allTeacher){
            dtoTeachers.add(convertToDto(teacher));
        }
        return dtoTeachers;
    }

    @Override
    public DtoTeacher findByTeacherId(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(teacher);
    }

    @Override
    public List<DtoCourseSection> getTeacherCourseSections(Long teacherId) {
        List<CourseSection> sections = courseSectionRepository.findByTeacher_Id(teacherId);
        return sections.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<DtoStudent> getStudentsByCourseSection(Long courseSectionId) {
        List<Enrollment> enrollments = enrollmentRepository.findByCourseSection_Id(courseSectionId);
        return enrollments.stream().map(e -> convertToDto(e.getStudent())).collect(Collectors.toList());
    }

    @Override
    public DtoTeacher findByUsername(String username) {
        Teacher teacher = teacherRepository.findByUser_Username(username)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_TEACHER_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(teacher);
    }

    private DtoTeacher convertToDto(Teacher teacher) {
        DtoTeacher dto = new DtoTeacher();
        BeanUtils.copyProperties(teacher, dto);
        dto.setId(teacher.getId());
        if (teacher.getCreatedDate() != null) {
            dto.setCreatedDate(teacher.getCreatedDate().toString());
        }
        if (teacher.getDepartment() != null) {
            DtoDepartment dtoDepartment = new DtoDepartment();
            BeanUtils.copyProperties(teacher.getDepartment(), dtoDepartment);
            dtoDepartment.setId(teacher.getDepartment().getId());
            dto.setDepartment(dtoDepartment);
        }
        if (teacher.getUser() != null) {
            DtoUser dtoUser = new DtoUser();
            BeanUtils.copyProperties(teacher.getUser(), dtoUser);
            dtoUser.setId(teacher.getUser().getId());
            dto.setUser(dtoUser);
        }
        return dto;
    }

    private DtoCourseSection convertToDto(CourseSection section) {
        DtoCourseSection dto = new DtoCourseSection();
        BeanUtils.copyProperties(section, dto);
        dto.setId(section.getId());
        if (section.getCourse() != null) {
            DtoCourse dtoCourse = new DtoCourse();
            BeanUtils.copyProperties(section.getCourse(), dtoCourse);
            dtoCourse.setId(section.getCourse().getId());
            dto.setCourse(dtoCourse);
        }
        return dto;
    }

    private DtoStudent convertToDto(Student student) {
        DtoStudent dto = new DtoStudent();
        BeanUtils.copyProperties(student, dto);
        dto.setId(student.getId());
        return dto;
    }
}