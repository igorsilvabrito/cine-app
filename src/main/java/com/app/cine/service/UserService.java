package com.app.cine.service;

import com.app.cine.dto.user.DataProfile;
import com.app.cine.dto.user.RegisterRequest;
import com.app.cine.dto.user.UserListingData;
import com.app.cine.entity.user.Profile;
import com.app.cine.entity.user.ProfileName;
import com.app.cine.entity.user.User;
import com.app.cine.infra.security.TokenService;
import com.app.cine.mapper.UserMapper;
import com.app.cine.repository.ProfileRepository;
import com.app.cine.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private ProfileRepository profileRepository;

    @Transactional
    public UserListingData registerUser(RegisterRequest dto) {

        Optional<User> existingUser = userRepository.findByEmailIgnoreCase(dto.email());
        if (existingUser.isPresent()) {
            throw new RuntimeException("E-mail já registrado.");
        }
        Profile profile = profileRepository.findByName(ProfileName.CUSTOMER);
        User newUser = userMapper.toEntity(dto, profile);

        userRepository.save(newUser);

        String token = tokenService.generateToken(newUser);
        System.out.println("Token gerado para novo usuário: " + token);

        return userMapper.toDto(newUser);
    }
    public List<UserListingData> getAllUser(){
        List<UserListingData> users = userRepository.findAll().stream().map(userMapper::toDto).toList();

        return users;
    }

    @Transactional
    public UserListingData addProfile(Long userId, DataProfile data) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Profile profile = profileRepository.findByName(data.profileName());
        user.addProfile(profile);

        return userMapper.toDto(user);
    }
    @Transactional
    public User deactivateUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!user.getActive()) {
            throw new IllegalStateException("Usuário já está desativado");
        }

        user.setActive(false);
        return userRepository.save(user);
    }@Transactional
    public User activateUser(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (user.getActive()) {
            throw new IllegalStateException("usuario já esta ativo");
        }

        user.setActive(true);
        return userRepository.save(user);
    }
}
