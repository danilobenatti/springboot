package curso.springboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import curso.springboot.model.Pessoa;

@Repository
@Transactional /* controla a persistencia de dados */
//public interface PessoaRepository extends CrudRepository<Pessoa, Long> {
public interface PessoaRepository extends JpaRepository<Pessoa, Long> { // "JpaRepository" Para implementar a paginação

    @Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER('%'||?1||'%')")
    List<Pessoa> findPessoaByName(String nome);

//    @Query("SELECT p FROM Pessoa p WHERE p.nome LIKE :nome")
//    List<Pessoa> findByNomeIgnoreCase(@Param("nome") String nome);

    @Query("SELECT p FROM Pessoa p WHERE p.sexo = ?1")
    List<Pessoa> findPessoaBySexo(Character sexo);

    @Query("SELECT p FROM Pessoa p WHERE LOWER(p.nome) LIKE LOWER('%'||?1||'%') AND p.sexo = ?2")
    List<Pessoa> findPessoaByNameSexo(String nome, Character sexo);

//    @Query("SELECT p FROM Pessoa p WHERE p.nome LIKE :nome AND p.sexo = :sexo")
//    List<Pessoa> findByNomeAndSexoIgnoreCase(@Param("nome") String nome, @Param("sexo") Character sexo);
    
    @Query("SELECT p FROM Pessoa p ORDER BY p.id ASC")
    List<Pessoa> findAllByOrderByIdAsc();
    
    default Page<Pessoa> findPessoaByNamePage(String nome, Pageable pageable) {
	Pessoa pessoa = new Pessoa();
	pessoa.setNome(nome);
	/**
	 * Configurando pesquisa para consultar por partes do atributo nome 
	 * no banco de dados, igual ao LIKE em SQL
	 */
	ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny().withMatcher("nome",
		ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
	/**
	 * Une o objeto com o valor e a configuração para consultar
	 */
	Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
	Page<Pessoa> pagePessoas = findAll(example, pageable);
	
	return pagePessoas;
    }
    
    default Page<Pessoa> findPessoaBySexoPage(Character sexo, Pageable pageable) {
	Pessoa pessoa = new Pessoa();
	pessoa.setSexo(sexo);
	/**
	 * Configurando pesquisa para consultar pelo atributo sexo 
	 * no banco de dados, igual ao LIKE em SQL
	 */
	ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
		.withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
	/**
	 * Une o objeto com o valor e a configuração para consultar
	 */
	Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
	Page<Pessoa> pagePessoas = findAll(example, pageable);
	
	return pagePessoas;
    }
    
    default Page<Pessoa> findPessoaByNameSexoPage(String nome, Character sexo, Pageable pageable) {
	Pessoa pessoa = new Pessoa();
	pessoa.setNome(nome);
	pessoa.setSexo(sexo);
	/**
	 * Configurando pesquisa para consultar por partes do atributo nome e por sexo
	 * no banco de dados, igual ao LIKE em SQL
	 */
	ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
		.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
		.withMatcher("sexo", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
	/**
	 * Une o objeto com o valor e a configuração para consultar
	 */
	Example<Pessoa> example = Example.of(pessoa, exampleMatcher);
	Page<Pessoa> pagePessoas = findAll(example, pageable);
	
	return pagePessoas;
    }

}
