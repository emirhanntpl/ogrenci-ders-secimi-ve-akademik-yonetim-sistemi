package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoDepartment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudent;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoStudentIU;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoUser;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.enums.Role;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Department;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Enrollment;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.RefreshToken;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.Student;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.User;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.DepartmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.EnrollmentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.RefreshTokenRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.StudentRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.UserRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IStudentService;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements IStudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository studentRepository, DepartmentRepository departmentRepository, UserRepository userRepository, EnrollmentRepository enrollmentRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public DtoStudent studentAdd(DtoStudentIU dtoIU) {
        
        if (studentRepository.findByStudentNumber(dtoIU.getStudentNumber()).isPresent()) {
            throw new RuntimeException("Bu öğrenci numarası (" + dtoIU.getStudentNumber() + ") ile kayıtlı başka bir öğrenci var!");
        }
        if (studentRepository.findByEmail(dtoIU.getEmail()).isPresent()) {
            throw new RuntimeException("Bu e-posta adresi (" + dtoIU.getEmail() + ") ile kayıtlı başka bir öğrenci var!");
        }
        if (dtoIU.getTelNumber() != null && !dtoIU.getTelNumber().trim().isEmpty()) {
            if (studentRepository.findByTelNumber(dtoIU.getTelNumber()).isPresent()) {
                throw new RuntimeException("Bu telefon numarası (" + dtoIU.getTelNumber() + ") ile kayıtlı başka bir öğrenci var!");
            }
        }

        Student student = new Student();
        student.setFirstName(dtoIU.getFirstName());
        student.setLastName(dtoIU.getLastName());
        student.setStudentNumber(dtoIU.getStudentNumber());
        student.setEmail(dtoIU.getEmail());
        student.setTelNumber(dtoIU.getTelNumber());

        if (dtoIU.getDepartmentId() != null && dtoIU.getDepartmentId() > 0) {
            Department department = departmentRepository.findById(dtoIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            student.setDepartment(department);
        }

        String username = dtoIU.getStudentNumber(); 
        
        Optional<User> existingUser = userRepository.findByUsername(username);
        User user;
        if(existingUser.isPresent()){
            user = existingUser.get();
            final Long userId = user.getId(); 
            Optional<Student> existingStudentWithThisUser = studentRepository.findAll().stream()
                .filter(s -> s.getUser() != null && s.getUser().getId().equals(userId))
                .findFirst();
            if (existingStudentWithThisUser.isPresent()) {
                throw new RuntimeException("Bu kullanıcı hesabı (" + username + ") zaten başka bir öğrenciye tanımlı!");
            }
        } else {
            user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(username)); 
            user.setRole(Role.STUDENT);
            user = userRepository.save(user);
        }
        student.setUser(user);

        Student savedStudent = studentRepository.save(student);
        return convertToDto(savedStudent);
    }

    @Override
    @Transactional
    public DtoStudent studentUpdate(Long id, DtoStudentIU dtoIU) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));

        Optional<Student> existByNumber = studentRepository.findByStudentNumber(dtoIU.getStudentNumber());
        if (existByNumber.isPresent() && !existByNumber.get().getId().equals(id)) {
            throw new RuntimeException("Bu öğrenci numarası başka bir öğrenci tarafından kullanılıyor!");
        }
        
        Optional<Student> existByEmail = studentRepository.findByEmail(dtoIU.getEmail());
        if (existByEmail.isPresent() && !existByEmail.get().getId().equals(id)) {
            throw new RuntimeException("Bu e-posta adresi başka bir öğrenci tarafından kullanılıyor!");
        }
        
        if (dtoIU.getTelNumber() != null && !dtoIU.getTelNumber().trim().isEmpty()) {
            Optional<Student> existByTel = studentRepository.findByTelNumber(dtoIU.getTelNumber());
            if (existByTel.isPresent() && !existByTel.get().getId().equals(id)) {
                throw new RuntimeException("Bu telefon numarası başka bir öğrenci tarafından kullanılıyor!");
            }
        }

        student.setFirstName(dtoIU.getFirstName());
        student.setLastName(dtoIU.getLastName());
        student.setStudentNumber(dtoIU.getStudentNumber());
        student.setEmail(dtoIU.getEmail());
        student.setTelNumber(dtoIU.getTelNumber());

        if (dtoIU.getDepartmentId() != null && dtoIU.getDepartmentId() > 0) {
            Department department = departmentRepository.findById(dtoIU.getDepartmentId())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_DEPARTMENT_ID, HttpStatus.BAD_REQUEST));
            student.setDepartment(department);
        } else {
            student.setDepartment(null);
        }

        String newUsername = dtoIU.getStudentNumber();
        if (student.getUser() == null || !student.getUser().getUsername().equals(newUsername)) {
             Optional<User> existingUser = userRepository.findByUsername(newUsername);
             User user;
             if(existingUser.isPresent()){
                 user = existingUser.get();
             } else {
                 user = new User();
                 user.setUsername(newUsername);
                 user.setPassword(passwordEncoder.encode(newUsername)); 
                 user.setRole(Role.STUDENT);
                 user = userRepository.save(user);
             }
             student.setUser(user);
        }

        Student updatedStudent = studentRepository.save(student);
        return convertToDto(updatedStudent);
    }

    @Override
    @Transactional
    public void studentDelete(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);
        if (enrollments != null && !enrollments.isEmpty()) {
            enrollmentRepository.deleteAll(enrollments);
        }

        User user = student.getUser();
        if (user != null) {
            List<RefreshToken> refreshTokens = refreshTokenRepository.findByUser(user);
            if (refreshTokens != null && !refreshTokens.isEmpty()) {
                refreshTokenRepository.deleteAll(refreshTokens);
            }
            
            student.setUser(null);
            studentRepository.save(student);
            
            userRepository.delete(user);
        }

        studentRepository.delete(student);
    }

    @Override
    public List<DtoStudent> getAllStudents() {
        List<Student> allStudents = studentRepository.findAll();
        List<DtoStudent> dtoList = new ArrayList<>();
        for (Student student : allStudents) {
            dtoList.add(convertToDto(student));
        }
        return dtoList;
    }

    @Override
    public DtoStudent findByStudentId(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(student);
    }

    @Override
    public DtoStudent findByUsername(String username) {
        Student student = studentRepository.findByUser_Username(username)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));
        return convertToDto(student);
    }

    @Override
    @Transactional
    public DtoStudent updateStudentProfile(Long id, String email, String telNumber, String newPassword) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new BaseException(MessageType.INVALID_STUDENT_ID, HttpStatus.BAD_REQUEST));

        if (email != null && !email.trim().isEmpty()) {
            Optional<Student> existByEmail = studentRepository.findByEmail(email);
            if (existByEmail.isPresent() && !existByEmail.get().getId().equals(id)) {
                throw new RuntimeException("Bu e-posta adresi başka bir öğrenci tarafından kullanılıyor!");
            }
            student.setEmail(email);
        }

        if (telNumber != null && !telNumber.trim().isEmpty()) {
            Optional<Student> existByTel = studentRepository.findByTelNumber(telNumber);
            if (existByTel.isPresent() && !existByTel.get().getId().equals(id)) {
                throw new RuntimeException("Bu telefon numarası başka bir öğrenci tarafından kullanılıyor!");
            }
            student.setTelNumber(telNumber);
        }

        if (newPassword != null && !newPassword.trim().isEmpty()) {
            if (student.getUser() != null) {
                student.getUser().setPassword(passwordEncoder.encode(newPassword));
            }
        }

        Student updatedStudent = studentRepository.save(student);
        return convertToDto(updatedStudent);
    }

    private DtoStudent convertToDto(Student student) {
        DtoStudent dto = new DtoStudent();
        BeanUtils.copyProperties(student, dto);
        
        dto.setId(student.getId());
        if (student.getCreatedDate() != null) {
            dto.setCreatedDate(student.getCreatedDate().toString());
        }

        if (student.getDepartment() != null) {
            DtoDepartment dtoDepartment = new DtoDepartment();
            BeanUtils.copyProperties(student.getDepartment(), dtoDepartment);
            dtoDepartment.setId(student.getDepartment().getId());
            if (student.getDepartment().getCreatedDate() != null) {
                dtoDepartment.setCreatedDate(student.getDepartment().getCreatedDate().toString());
            }
            dto.setDepartment(dtoDepartment);
        }

        if (student.getUser() != null) {
            DtoUser dtoUser = new DtoUser();
            BeanUtils.copyProperties(student.getUser(), dtoUser);
            dtoUser.setId(student.getUser().getId());
            if (student.getUser().getCreatedDate() != null) {
                dtoUser.setCreatedDate(student.getUser().getCreatedDate().toString());
            }
            dto.setUser(dtoUser);
        }

        return dto;
    }
}