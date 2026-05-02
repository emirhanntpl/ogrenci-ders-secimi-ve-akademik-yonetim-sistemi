package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception;

public enum MessageType {
    INVALID_STUDENT_ID("1001", "ÖĞRENCİ ID BULUNAMADI"),
    STUDENT_LIST_IS_EMPTY("1002","ÖĞRENCİ LİSTESİ BOŞ."),
    INVALID_DEPARTMENT_ID("1003","BÖLÜM ID BULUNAMADI"),
    DEPARTMENT_LIST_IS_EMPTY("1004","BÖLÜM LİSTESİ BOŞ."),
    INVALID_TEACHER_ID("1005","ÖĞRETMEN ID BULUNAMADI"),
    TEACHER_LIST_IS_EMPTY("1006","ÖĞRETMEN LİSTESİ BOŞ."),
    INVALID_COURSE_ID("1007","DERS ID BULUNAMADI"),
    COURSE_LIST_IS_EMPTY("1008","DERS LİSTESİ BOŞ."),
    INVALID_FACULTY_ID("1009","FAKÜLTE ID BULUNAMADI"),
    FACULTY_LIST_IS_EMPTY("1010","FAKÜLTE LİSTESİ BOŞ."),
    INVALID_USER_ID("1011","KULLANICI ID BULUNAMADI"),
    USER_LIST_IS_EMPTY("1012","KULLANICI LİSTESİ BOŞ."),
    CLASSROOM_IS_EMPTY("1013","CLASSROOM BULUNAMADI"),

    // Grade related messages
    INVALID_GRADE_ID("1014", "NOT (GRADE) ID BULUNAMADI"),
    GRADE_LIST_IS_EMPTY("1015", "NOT LİSTESİ BOŞ."),

    // Semester related messages
    INVALID_SEMESTER_ID("1016", "DÖNEM (SEMESTER) ID BULUNAMADI"),
    SEMESTER_LIST_IS_EMPTY("1017", "DÖNEM LİSTESİ BOŞ."),

    // CourseSection related messages
    INVALID_COURSE_SECTION_ID("1018", "DERS ŞUBESİ (COURSE SECTION) ID BULUNAMADI"),
    COURSE_SECTION_LIST_IS_EMPTY("1019", "DERS ŞUBESİ LİSTESİ BOŞ."),

    // Enrollment related messages
    INVALID_ENROLLMENT_ID("1020", "DERS KAYDI (ENROLLMENT) ID BULUNAMADI"),
    ENROLLMENT_LIST_IS_EMPTY("1021", "DERS KAYDI LİSTESİ BOŞ."),

    // Announcement related messages
    INVALID_ANNOUNCEMENT_ID("1022", "DUYURU ID BULUNAMADI"),
    ANNOUNCEMENT_LIST_IS_EMPTY("1023", "DUYURU LİSTESİ BOŞ."),

    // Exam related messages
    INVALID_EXAM_ID("1024", "SINAV (EXAM) ID BULUNAMADI"),
    EXAM_LIST_IS_EMPTY("1025", "SINAV LİSTESİ BOŞ."),

    // Assignment related messages
    INVALID_ASSIGNMENT_ID("1026", "ÖDEV (ASSIGNMENT) ID BULUNAMADI"),
    ASSIGNMENT_LIST_IS_EMPTY("1027", "ÖDEV LİSTESİ BOŞ."),

    INVALID_DATE_FORMAT("1028", "Tarih formatı hatalı.");


    private final String code;
    private final String message;

    MessageType(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
