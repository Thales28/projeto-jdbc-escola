package model.entities;

public class Bolsista extends Aluno{
	private static final long serialVersionUID = 1L;
	private String tipoBolsa;
	
	public Bolsista() {
		
	}

	public Bolsista(String cpf, String nome, Double valorMensalidade, String endereco, Double creditos, Turma turma,
			String tipoBolsa) {
		super(cpf, nome, valorMensalidade, endereco, creditos, turma);
		this.tipoBolsa = tipoBolsa;
	}

	public String getTipoBolsa() {
		return tipoBolsa;
	}

	public void setTipoBolsa(String tipoBolsa) {
		this.tipoBolsa = tipoBolsa;
	}

	@Override
	public String toString() {
		return "Bolsista [tipoBolsa=" + tipoBolsa + "]";
	}
}
