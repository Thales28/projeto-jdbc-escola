package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.impl.AlunoDaoJDBC;
import model.dao.impl.TurmaDaoJDBC;
import model.entities.Aluno;
import model.entities.Bolsista;
import model.entities.Turma;

public class Program {

	public static void main(String[] args) {
		TurmaDaoJDBC dao = DaoFactory.createTurma();
		AlunoDaoJDBC daoAluno = DaoFactory.createAluno();
		System.out.println("=== FIND TURMA BY ID (111) ===");
		Turma turma = (Turma) dao.findById(111);
		System.out.println(turma);
		System.out.println("______________________________");
		
		System.out.println("=== FIND ALUNO BY TURMA (111) ===");
		List<Bolsista> alunos = daoAluno.findByTurma(turma);
		for(Bolsista aluno : alunos) {
			System.out.println(aluno);
		}
		
		System.out.println("______________________________");
	}
}