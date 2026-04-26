package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoUser;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.enums.Role;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthRequest;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthResponse;
import java.util.List;

public interface IAuthService {

    public DtoUser register(AuthRequest request, Role role);

    public DtoUser registerAdmin(AuthRequest request);

    public AuthResponse authenticate(AuthRequest request);
    
    public List<DtoUser> getAllUsers();
    
    public void deleteUser(Long id);

}
