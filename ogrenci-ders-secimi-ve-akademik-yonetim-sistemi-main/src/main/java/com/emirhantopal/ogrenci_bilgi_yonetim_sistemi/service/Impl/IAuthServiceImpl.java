package com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.Impl;

import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.dto.DtoUser;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.enums.Role;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.BaseException;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.exception.MessageType;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthRequest;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.AuthResponse;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.jwt.JwtService;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.RefreshToken;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.model.User;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.RefreshTokenRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.repository.UserRepository;
import com.emirhantopal.ogrenci_bilgi_yonetim_sistemi.service.IAuthService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IAuthServiceImpl implements IAuthService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public DtoUser register(AuthRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER); // Varsayılan olarak her zaman USER atanır
        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    @Override
    @Transactional
    public DtoUser registerAdmin(AuthRequest request){
        User userAdmin = new User();
        userAdmin.setUsername(request.getUsername());
        userAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
        userAdmin.setRole(Role.ADMIN); // Admin kaydı için özel metod
        User savedAdmin = userRepository.save(userAdmin);
        return convertToDto(savedAdmin);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new BaseException(MessageType.INVALID_USER_ID, HttpStatus.BAD_REQUEST));

            String accessToken = jwtService.generateToken(user);
            RefreshToken refreshToken = createRefreshToken(user);
            refreshTokenRepository.save(refreshToken);

            return new AuthResponse(accessToken, refreshToken.getRefreshToken());

        } catch (Exception e) {
            throw new RuntimeException("Giriş başarısız: " + e.getMessage());
        }
    }

    @Override
    public List<DtoUser> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Yeni: Rol Güncelleme Metodu
    @Override
    @Transactional
    public DtoUser updateRole(Long id, Role role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        
        user.setRole(role);
        User updatedUser = userRepository.save(user);
        return convertToDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        
        List<RefreshToken> tokens = refreshTokenRepository.findByUser(user);
        if (tokens != null && !tokens.isEmpty()) {
            refreshTokenRepository.deleteAll(tokens);
        }
        
        userRepository.delete(user);
    }

    private RefreshToken createRefreshToken(User user){
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpireDate(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 12));
        refreshToken.setUser(user);
        return refreshToken;
    }

    private DtoUser convertToDto(User user) {
        DtoUser dto = new DtoUser();
        BeanUtils.copyProperties(user, dto);
        dto.setId(user.getId());
        if (user.getRole() != null) {
            dto.setRole(user.getRole().name());
        }
        if (user.getCreatedDate() != null) {
            dto.setCreatedDate(user.getCreatedDate().toString());
        }
        return dto;
    }
}
