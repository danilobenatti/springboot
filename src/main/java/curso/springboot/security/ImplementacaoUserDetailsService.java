package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Usuario;
import curso.springboot.repository.UsuarioRepository;

@Service
@Transactional
public class ImplementacaoUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	Usuario usuario = usuarioRepository.findUserByLogin(username);

	if (usuario == null) {
	    String msg = "Usuário não foi encontrado";
	    throw new UsernameNotFoundException(msg);
	}

//		return usuario;
//		return new User(usuario.getLogin(), usuario.getPassword(), usuario.isEnabled(), true, true, true,
//				usuario.getAuthorities());
	return new User(usuario.getLogin(), usuario.getPassword(), usuario.isEnabled(), usuario.isAccountNonExpired(),
		usuario.isCredentialsNonExpired(), usuario.isAccountNonLocked(), usuario.getAuthorities());
    }

}
