package com.autobots.automanager.selecionadores;

import java.util.List;
import org.springframework.stereotype.Component;
import com.autobots.automanager.entitades.Usuario;

@Component
public class UsuarioSelecionador implements Selecionador<Usuario, Long> {
    
    @Override
    public Usuario selecionar(List<Usuario> usuarios, Long id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }
}
