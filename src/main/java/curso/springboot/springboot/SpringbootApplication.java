package curso.springboot.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "curso.springboot.model")
@ComponentScan(basePackages = { "curso.*" }) /* força o mapeamento de todos os pacotes */
@EnableJpaRepositories(basePackages = { "curso.springboot.repository" })
@EnableTransactionManagement
@EnableWebMvc
public class SpringbootApplication implements WebMvcConfigurer {

	public static void main(String[] args) {

		SpringApplication.run(SpringbootApplication.class, args);

		// Rotina abaixo para gerar senha criptografada.
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String result = passwordEncoder.encode("123");
		System.out.println(result);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// método que redireciona a tela de login personalizada.
		registry.addViewController("/login").setViewName("/login");
		// registry.setOrder(Ordered.LOWEST_PRECEDENCE);

	}

}
