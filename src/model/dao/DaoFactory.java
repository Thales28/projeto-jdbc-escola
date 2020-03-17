package model.dao;

import db.DB;
import model.dao.impl.AlunoDaoJDBC;
import model.dao.impl.Aluno_DisciplinaDaoJDBC;
import model.dao.impl.DisciplinaDaoJDBC;
import model.dao.impl.TurmaDaoJDBC;

public class DaoFactory {
	public static TurmaDaoJDBC createTurma() {
		return new TurmaDaoJDBC(DB.getConnection());
	}
	
	public static AlunoDaoJDBC createAluno() {
		return new AlunoDaoJDBC(DB.getConnection());
	}
	
	public static DisciplinaDaoJDBC createDisciplina() {
		return new DisciplinaDaoJDBC(DB.getConnection());
	}
	
	public static Aluno_DisciplinaDaoJDBC createAluno_Disciplina() {
		return new Aluno_DisciplinaDaoJDBC(DB.getConnection());
	}
}
