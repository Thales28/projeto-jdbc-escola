package model.entities;

import java.io.Serializable;

public class Aluno implements Serializable{
	private static final long serialVersionUID = 1L;
	private String cpf;
	private String nome;
	private Double valorMensalidade;
	private String endereco;
	private Double creditos;
	private Boolean bolsista;
	
	private Turma turma;
	
	public Aluno() {
		
	}

	public Aluno(String cpf, String nome, Double valorMensalidade, String endereco, Double creditos,
			Turma turma, Boolean bolsista) {

		this.cpf = cpf;
		this.nome = nome;
		this.valorMensalidade = valorMensalidade;
		this.endereco = endereco;
		this.creditos = creditos;
		this.bolsista = bolsista;
		this.turma = turma;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Double getValorMensalidade() {
		return valorMensalidade;
	}

	public void setValorMensalidade(Double valorMensalidade) {
		this.valorMensalidade = valorMensalidade;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public Double getCreditos() {
		return creditos;
	}

	public void setCreditos(Double creditos) {
		this.creditos = creditos;
	}

	public Turma getTurma() {
		return turma;
	}

	public void setTurma(Turma turma) {
		this.turma = turma;
	}
	
	public Boolean getBolsista() {
		return bolsista;
	}
	
	public void setBolsista(Boolean bolsista) {
		this.bolsista = bolsista;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Aluno other = (Aluno) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Aluno [cpf=" + cpf + ", nome=" + nome + ", valorMensalidade=" + valorMensalidade + ", endereco="
				+ endereco + ", creditos=" + creditos + ", bolsista=" + bolsista + ", turma=" + turma + "]";
	}
}
