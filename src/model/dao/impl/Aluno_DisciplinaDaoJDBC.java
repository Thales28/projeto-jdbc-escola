package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.Dao;
import model.entities.Aluno;
import model.entities.Aluno_Disciplina;
import model.entities.Aluno_Disciplina_PK;
import model.entities.Disciplina;
import model.entities.Turma;

public class Aluno_DisciplinaDaoJDBC implements Dao {
	private Connection conn;

	public Aluno_DisciplinaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object findById(Object id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(
					"SELECT * "
					+ "FROM escola.aluno_disciplina INNER JOIN escola.aluno ON escola.aluno_disciplina.aluno = escola.aluno.cpf "
					+ "INNER JOIN escola.disciplina ON escola.aluno_disciplina.disciplina = escola.disciplina.id_disciplina "
					+ "WHERE (aluno_disciplina.aluno = ?) "
					+ "AND (aluno_disciplina.disciplina = ?)");
			
			Aluno_Disciplina_PK ADPK = (Aluno_Disciplina_PK) id;
			ps.setString(1, ADPK.getAluno().getCpf());
			ps.setInt(2, ADPK.getDisciplina().getId());
			rs = ps.executeQuery();
			
			if(rs.next()) {
				
				Aluno_Disciplina alunoDisciplina = new Aluno_Disciplina();
				Aluno aluno = new Aluno();
				Disciplina disciplina = new Disciplina();
				Turma turma = new Turma();
				
				turma.setId(rs.getInt("id_turma"));
				turma.setTurno(rs.getString("turno"));
				
				aluno.setCpf(rs.getString("cpf"));
				aluno.setCreditos(rs.getDouble(9));
				aluno.setEndereco(rs.getString("endereco"));
				aluno.setNome(rs.getString(5));
				aluno.setTurma(turma);
				aluno.setValorMensalidade(rs.getDouble("valorMensalidade"));
				
				disciplina.setCreditos(rs.getDouble(13));
				disciplina.setId(rs.getInt("id_disciplina"));
				disciplina.setNome(rs.getString(12));
				
				alunoDisciplina.setAluno(aluno);
				Boolean ativo = false;
				if(rs.getInt("ativo") == 1) {
					ativo = true;
				}
				alunoDisciplina.setAtivo(ativo);
				alunoDisciplina.setDisciplina(disciplina);

				return alunoDisciplina;
			}
			return null;
		}
		
		catch(SQLException e){
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Object> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
