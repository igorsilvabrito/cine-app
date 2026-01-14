package com.app.cine.infra.security;

import com.app.cine.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HierarchyService  {
    @Autowired
    private RoleHierarchy roleHierarchy;

    public boolean usuarioNaoTemPermissoes(User logado, User autor, String perfilDesejado) {
        for(GrantedAuthority autoridade: logado.getAuthorities()){
            var autoridadesAlcancaveis =  roleHierarchy.getReachableGrantedAuthorities(List.of(autoridade));

            for(GrantedAuthority perfil: autoridadesAlcancaveis){
                if(perfil.getAuthority().equals(perfilDesejado) || logado.getId().equals(autor.getId()))
                    return false;
            }
        }
        return true;
    }
}