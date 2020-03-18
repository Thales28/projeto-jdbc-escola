package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.impl.AlunoDaoJDBC;
import model.dao.impl.Aluno_DisciplinaDaoJDBC;
import model.dao.impl.DisciplinaDaoJDBC;
import model.dao.impl.TurmaDaoJDBC;
import model.entities.Aluno_Disciplina;
import model.entities.Bolsista;
import model.entities.Disciplina;
import model.entities.Turma;

public class Program {

	public static void main(String[] args) {
		TurmaDaoJDBC daoTurma = DaoFactory.createTurma();
		AlunoDaoJDBC daoAluno = DaoFactory.createAluno();
		DisciplinaDaoJDBC daoDisciplina = DaoFactory.createDisciplina();
		Aluno_DisciplinaDaoJDBC daoAlunoDisciplina = DaoFactory.createAluno_Disciplina();

		Turma turma = new Turma(111, "M");
		daoTurma.insert(turma);
		
		Disciplina disc1 = new Disciplina(1, "História", 4.00);
		Disciplina disc2 = new Disciplina(2, "Biologia", 6.00);
		Disciplina disc3 = new Disciplina(3, "Psicologia", 5.00);
		daoDisciplina.insert(disc1);
		daoDisciplina.insert(disc2);
		daoDisciplina.insert(disc3);
		
		Bolsista a1 = new Bolsista("12345", "João", 100.00, "Rua da Hora", 15.00, turma, null);
		Bolsista a2 = new Bolsista("23451", "Maria", 100.00, "Rua da Guia", 15.00, turma, null);
		Bolsista a3 = new Bolsista("34512", "José", 0.00, "Rua da Moeda", 15.00, turma, "ATLETA");
		Bolsista a4 = new Bolsista("45123", "Jesus", 0.00, "Rua Rio Branco", 15.00, turma, "SOCIAL");
		Bolsista a5 = new Bolsista("51234", "Jeová", 100.00, "Rua do Arsenal", 15.00, turma, null);
		daoAluno.insert(a1);
		daoAluno.insert(a2);
		daoAluno.insert(a3);
		daoAluno.insert(a4);
		daoAluno.insert(a5);
		
		Aluno_Disciplina AD1 = new Aluno_Disciplina(a1, disc1, true);
		Aluno_Disciplina AD2 = new Aluno_Disciplina(a1, disc2, true);
		Aluno_Disciplina AD3 = new Aluno_Disciplina(a1, disc3, true);
		Aluno_Disciplina AD4 = new Aluno_Disciplina(a2, disc2, true);
		Aluno_Disciplina AD5 = new Aluno_Disciplina(a2, disc3, true);
		Aluno_Disciplina AD6 = new Aluno_Disciplina(a3, disc1, true);
		daoAlunoDisciplina.insert(AD1);
		daoAlunoDisciplina.insert(AD2);
		daoAlunoDisciplina.insert(AD3);
		daoAlunoDisciplina.insert(AD4);
		daoAlunoDisciplina.insert(AD5);
		daoAlunoDisciplina.insert(AD6);
		
		List<Bolsista> alunos = daoAluno.findByTurma(turma);
		
		for(Bolsista aluno : alunos) {
			System.out.println(aluno);
		}
		
	}
}