package com.soulcode.Servicos.Security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

// Abstrai o User do banco para que o Security conheça seus dados
// Ou conhecer o usuário melhor =)
public class UserSecurityDetail implements UserDetails {
    private String login;
    private String password;

    private boolean isEnabled;

    public UserSecurityDetail(String login, String password, boolean isEnabled) {
        this.login = login;
        this.password = password;
        this.isEnabled = isEnabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() { // a conta não expirou
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // a conta não bloqueou
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // as credenciais não expiraram
        return true;
    }

    @Override
    public boolean isEnabled() { // o usuário está habilitado
        return isEnabled; // statusUsuario == 1;
    }// 0 -> desabilitado, 1 -> habilitado
}
/*
 * O Spring Security não se comunica diretamente com o nosso model User =(
 * Então devemos criar uma classe que ele conheça para fazer essa comunicação,
 * UserDetails = Guarda informações do contexto de autenticação do usuário (autorizações, habilitado, etc)
 * */