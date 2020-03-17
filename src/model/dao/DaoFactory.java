package model.dao;

import db.DB;
import model.dao.impl.AlunoDaoJDBC;
import model.dao.impl.Aluno_DisciplinaDaoJDBC;
import model.dao.impl.BolsistaDaoJDBC;
import model.dao.impl.DisciplinaDaoJDBC;
import model.dao.impl.TurmaDaoJDBC;

public class DaoFactory {
	public static Dao createTurma() {
		return new TurmaDaoJDBC(DB.getConnection());
	}
	
	public static Dao createAluno() {
		return new AlunoDaoJDBC(DB.getConnection());
	}
	
	public static Dao createBolsista() {
		return new BolsistaDaoJDBC(DB.getConnection());
	}
	
	public static Dao createDisciplina() {
		return new DisciplinaDaoJDBC(DB.getConnection());
	}
	
	public static Dao createAluno_Disciplina() {
		return new Aluno_DisciplinaDaoJDBC(DB.getConnection());
	}
}
