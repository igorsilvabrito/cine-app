package com.app.cine.controller;

import com.app.cine.dto.user.DataProfile;
import com.app.cine.dto.user.RegisterRequest;
import com.app.cine.dto.user.UserListingData;
import com.app.cine.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("auth")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserListingData>> listUsers() {
        List<UserListingData> users = userService.getAllUser();
        return ResponseEntity.ok(users);
    }
    @PostMapping("/register")
    public ResponseEntity<UserListingData> registerUser(@RequestBody @Valid RegisterRequest dto, UriComponentsBuilder uriBuilder) {
        UserListingData user = userService.registerUser(dto);
        var uri = uriBuilder.path("/{id}").buildAndExpand(user.id()).toUri();
        return ResponseEntity.created(uri).body(user);
    }

    @PatchMapping("/add-profile/{id}")
    public ResponseEntity<UserListingData> addProfile(@PathVariable Long id, @RequestBody @Valid DataProfile data) {
        UserListingData user = userService.addProfile(id, data);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateUser(@PathVariable Long id) {
        var user = userService.deactivateUser(id);
        return ResponseEntity.ok("Usuário " + user.getEmail() + " foi desativado com sucesso.");
    }

    @PutMapping("/{id}/active")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        var user = userService.activateUser(id);
        return ResponseEntity.ok("Usuário " + user.getEmail() + " foi ativado com sucesso.");
    }
}
