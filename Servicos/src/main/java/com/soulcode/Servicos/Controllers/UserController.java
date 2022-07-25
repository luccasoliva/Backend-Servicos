package com.soulcode.Servicos.Controllers;

import com.soulcode.Servicos.Models.User;
import com.soulcode.Servicos.Repositories.UserRepository;
import com.soulcode.Servicos.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("servicos")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/usuarios")
    public List<User> usuarios() {
        return userService.listar();
    }

    @PostMapping("/usuarios")
    public ResponseEntity<User> inserirUsuario(@RequestBody User user) {
        String senhaCodificada = passwordEncoder.encode(user.getPassword());
        user.setPassword(senhaCodificada);
        user = userService.cadastrar(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/AlterarSenhaUser/{id}")
    public ResponseEntity<User> mudarSenha(@PathVariable Integer id, @RequestBody User user) {
        String senhaCodificada = passwordEncoder.encode(user.getPassword());
        if (user.getPassword().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }else {
            user.setId(id);
            user.setLogin(userRepository.findById(id).get().getLogin());
            user.setPassword(senhaCodificada);
            user = userService.mudarSenha(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }
    }

    @PutMapping("/desabilitarUsuario/{login}")
    public ResponseEntity<User> desabilitarConta(@PathVariable String login, User user, Principal principal) {
        //see if the user is logged
        if (principal.getName().equals(login)) {
            user.setId(userRepository.findByLogin(login).get().getId());
            user.setLogin(login);
            user.setPassword(userRepository.findByLogin(login).get().getPassword());

            user = userService.desabilitarConta(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }
}
