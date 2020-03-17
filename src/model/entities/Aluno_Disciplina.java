package model.entities;

import java.io.Serializable;

public class Aluno_Disciplina implements Serializable{

	private static final long serialVersionUID = 1L;
	private Aluno_Disciplina_PK id = new Aluno_Disciplina_PK();
	private Boolean ativo;
	
	public Aluno_Disciplina() {
		
	}
	
	public Aluno_Disciplina(Aluno aluno, Disciplina disciplina, Boolean ativo) {
		super();
		id.setAluno(aluno);
		id.setDisciplina(disciplina);
		this.ativo = ativo;
	}
	
	public Aluno getAluno() {
		return id.getAluno();
	}
	
	public void setAluno(Aluno aluno) {
		id.setAluno(aluno);
	}
	
	public Disciplina getDisciplina() {
		return id.getDisciplina();
	}
	
	public void setDisciplina(Disciplina disciplina) {
		id.setDisciplina(disciplina);
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
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}