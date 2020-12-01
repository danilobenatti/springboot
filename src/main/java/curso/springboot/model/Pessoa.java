package curso.springboot.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String MESSAGE1 = "Informe o nome!";

	private static final String MESSAGE2 = "Informe o sobrenome!";

	private static final String MESSAGE3 = "Informe a idade!";

	private static final String MESSAGE4 = "Informe o sexo!";

	@Id
	@SequenceGenerator(name = "pessoaID", sequenceName = "sequence_pessoa", initialValue = 1, allocationSize = 1)
	@GeneratedValue(generator = "pessoaID")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = MESSAGE1)
	@NotEmpty(message = MESSAGE1)
	@Column(nullable = false)
	private String nome;

	@NotNull(message = MESSAGE2)
	@NotEmpty(message = MESSAGE2)
	@Column(nullable = false)
	private String sobrenome;

	@NotNull(message = MESSAGE3)
	@Min(value = 18, message = "Idade mínima de 18 anos!")
	@Column(nullable = false)
	private int idade;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;

	@NotNull(message = MESSAGE4)
	@Column(nullable = false)
	private Character sexo;

	@OneToMany(mappedBy = "pessoa", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Telefone> telefones;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_profissao"))
	private Profissao profissao;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "CHAR(1)")
	private Cargo cargo;
	
	@Lob
	private byte[] curriculo;
	
	private String nomeFileCurriculo;
	
	private String tipoFileCurriculo;

	@Pattern(regexp = "\\d{5}-\\d{3}", message = "Formato de CEP inválido!")
	@Column(length = 9, nullable = true)
	private String cep;

	private String rua;

	private String bairro;

	private String cidade;

	@Column(length = 2)
	private String uf;

	@Column(length = 7)
	private String ibge;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Character getSexo() {
		return sexo;
	}

	public void setSexo(Character sexo) {
		this.sexo = sexo;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public Profissao getProfissao() {
		return profissao;
	}

	public void setProfissao(Profissao profissao) {
		this.profissao = profissao;
	}
	
	public Cargo getCargo() {
		return cargo;
	}
	
	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}
	
	public byte[] getCurriculo() {
		return curriculo;
	}

	public void setCurriculo(byte[] curriculo) {
		this.curriculo = curriculo;
	}

	public String getNomeFileCurriculo() {
	    return nomeFileCurriculo;
	}

	public void setNomeFileCurriculo(String nomeFileCurriculo) {
	    this.nomeFileCurriculo = nomeFileCurriculo;
	}

	public String getTipoFileCurriculo() {
	    return tipoFileCurriculo;
	}

	public void setTipoFileCurriculo(String tipoFileCurriculo) {
	    this.tipoFileCurriculo = tipoFileCurriculo;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getRua() {
		return rua;
	}

	public void setRua(String rua) {
		this.rua = rua;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

}
