package curso.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private ImplementacaoUserDetailsService implementacaoUserDetailsService;

    @Override // Configura as solicitações de acesso por http
    protected void configure(HttpSecurity http) throws Exception {

	http.csrf()
	.disable() // Desabilita as configurações padrão de memória do spring.
	.authorizeRequests() // Permitir restringir acessos.
	.antMatchers(HttpMethod.GET, "/").permitAll() // Qualquer usuário acessa a página inicial.
	.antMatchers(HttpMethod.GET, "/cadastropessoa").hasAnyRole("ADMIN","USER","GUEST") // não precisa escrever ROLE_***
	.anyRequest().authenticated()
	.and().formLogin().permitAll() // permite qualquer usuário acessar página de login.
	// configura de tela de login personalizada
	.loginPage("/login")
	.defaultSuccessUrl("/cadastropessoa")
	.failureUrl("/login?error=true")
	.and().logout().logoutSuccessUrl("/login") // Mapeia URL de logout e invalida usuário autenticcado
	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

    }

    @Override // Cria autenticação do usuário com banco de dados ou em memória
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

	auth.userDetailsService(implementacaoUserDetailsService).passwordEncoder(new BCryptPasswordEncoder());

	// no lugar de "new BCryptPasswordEncoder()" estava utilizando
	// "NoOpPasswordEncoder.getInstance()"
	// para senha não criptografada.
	/*
	 * auth.inMemoryAuthentication().passwordEncoder(new BCryptPasswordEncoder())
	 * .withUser("teste")
	 * .password("$2a$10$obEALoJOrIRJR3f01cTYDuQgYqUE1jAwDT7.gejYlvHuXks3Mrsa2") //
	 * senha criptograda "123" .roles("ADMIN");
	 */
    }

    @Override // Ignora URL especificas
    public void configure(WebSecurity web) throws Exception {
	web.ignoring().antMatchers("/materialize/**");
    }

}
