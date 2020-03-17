package application;

import model.dao.Dao;
import model.dao.DaoFactory;
import model.entities.Turma;

public class Program {

	public static void main(String[] args) {
		Dao dao = DaoFactory.createTurma();
		
		Turma turma = (Turma) dao.findById(111);
		
		System.out.println(turma);
	}
}