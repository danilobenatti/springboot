package curso.springboot.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "usuarioID", sequenceName = "sequence_usuario", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "usuarioID")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String login;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String senha;

    // JoinTable cria tabela de acesso do usuário.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_role", uniqueConstraints = @UniqueConstraint(name = "uk_usuarios_role", columnNames = { "usuario_id ","role_id" }),
    	joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id", table = "usuario", nullable = false, unique = false, foreignKey = @ForeignKey(name = "fk_usuario_id",
    		foreignKeyDefinition = "FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE")), 
    	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", table = "role", nullable = false, unique = false, foreignKey = @ForeignKey(name = "fk_role_id", 
    		foreignKeyDefinition = "FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE")))
    private List<Role> roles;

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getSenha() {
	return senha;
    }

    public void setSenha(String senha) {
	this.senha = senha;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
	return roles;
    }

    @Override
    public String getPassword() {
	return senha;
    }

    @Override
    public String getUsername() {
	return login;
    }

    @Override
    public boolean isAccountNonExpired() {
	return true;
    }

    @Override
    public boolean isAccountNonLocked() {
	return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
	return true;
    }

    @Override
    public boolean isEnabled() {
	return true;
    }

}
