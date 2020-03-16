package model.entities;

import java.io.Serializable;

public class Aluno_Disciplina implements Serializable{

	private static final long serialVersionUID = 1L;
	private String alunoId;
	private Integer disciplinaId;
	private Boolean ativo;
	
	public Aluno_Disciplina() {
		
	}

	public Aluno_Disciplina(String alunoId, Integer disciplinaId, Boolean ativo) {
		super();
		this.alunoId = alunoId;
		this.disciplinaId = disciplinaId;
		this.ativo = ativo;
	}

	public String getAlunoId() {
		return alunoId;
	}

	public void setAlunoId(String alunoId) {
		this.alunoId = alunoId;
	}

	public Integer getDisciplinaId() {
		return disciplinaId;
	}

	public void setDisciplinaId(Integer disciplinaId) {
		this.disciplinaId = disciplinaId;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alunoId == null) ? 0 : alunoId.hashCode());
		result = prime * result + ((disciplinaId == null) ? 0 : disciplinaId.hashCode());
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
		Aluno_Disciplina other = (Aluno_Disciplina) obj;
		if (alunoId == null) {
			if (other.alunoId != null)
				return false;
		} else if (!alunoId.equals(other.alunoId))
			return false;
		if (disciplinaId == null) {
			if (other.disciplinaId != null)
				return false;
		} else if (!disciplinaId.equals(other.disciplinaId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Aluno_Disciplina [alunoId=" + alunoId + ", disciplinaId=" + disciplinaId + ", ativo=" + ativo + "]";
	}	
}
