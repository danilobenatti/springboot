package curso.springboot.model;

public enum Cargo {
	J("Júnior"),
	P("Pleno"),
	S("Sênior");
	
	private String nome;
	
	@SuppressWarnings("unused")
	private String valor;

	private Cargo(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getValor() {
		return valor = this.name();
	}
	
	public void setValor(String valor) {
		this.valor = valor;
	}
	
//	@Override
//	public String toString() {
//		return this.name();
//	}

}
