package curso.springboot.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_nomerole", columnNames = { "nomeRole" }))
public class Role implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "roleID", sequenceName = "sequence_role", initialValue = 1, allocationSize = 1)
    @GeneratedValue(generator = "roleID")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String nomeRole;
    
    @Override
    public String getAuthority() { // ROLE_ADMIN, ROLE_USER, ROLE_GUEST, ROL_*** é um padrão do spring security
	return this.nomeRole;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getNomeRoler() {
	return nomeRole;
    }

    public void setNomeRoler(String nomeRole) {
	this.nomeRole = nomeRole;
    }

}
