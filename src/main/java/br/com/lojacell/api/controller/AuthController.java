package br.com.lojacell.api.controller;

import br.com.lojacell.api.dto.LoginRequestDTO;
import br.com.lojacell.domain.model.User;
import br.com.lojacell.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*") // Libera o acesso do React
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        Optional<User> userOpt = userRepository.findByName(request.getName());

        if (userOpt.isPresent() && userOpt.get().getPassword().equals(request.getPassword())) {
            // Login com sucesso! Retorna o usuário (mas escondendo a senha por boa prática)
            User user = userOpt.get();
            user.setPassword(null);
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválidos!");
    }
}