package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class DtoTeacherIU {

    private String firstName;

    private String lastName;

    private Long departmentId;
    
    private String staffNumber; // userId yerine staffNumber ve tipi String olarak değiştirildi

    // Getter ve Setter metodları manuel olarak eklendi
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getStaffNumber() {
        return staffNumber;
    }

    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }
}
