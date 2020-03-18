package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.Dao;
import model.dao.DaoFactory;
import model.entities.Aluno_Disciplina;
import model.entities.Bolsista;
import model.entities.Disciplina;

public class DisciplinaDaoJDBC implements Dao {
	private Connection conn;

	public DisciplinaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Object obj) {
		Disciplina disciplina = (Disciplina) obj;
		
		if(disciplina.getCreditos() < 0) {
			throw new DbException("Créditos não podem ser negativos");
		}
		else if (disciplina.getNome().isBlank()) {
			throw new DbException("A disciplina precisa de um nome");
		}
		else if(disciplina.getId() != null) {
			DisciplinaDaoJDBC dao = DaoFactory.createDisciplina();
			Disciplina disciplina2 = (Disciplina) dao.findById(disciplina.getId());
			if(disciplina2 != null) {
				throw new DbIntegrityException("Id já inserido");
			}
		}
		else {
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("INSERT INTO disciplina " + "(id_disciplina, nome, creditos) "
			+ "VALUES " + "(?, ?, ?) ",
						Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, disciplina.getId());
				ps.setString(2, disciplina.getNome());
				ps.setDouble(3, disciplina.getCreditos());

				int rowsAffected = ps.executeUpdate();

				if (rowsAffected > 0) {
					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
						int id = rs.getInt(1);
						disciplina.setId(id);
					}
					DB.closeResultSet(rs);
				} else {
					throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
				}

			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			} finally {
				DB.closeStatement(ps);
			}
		}

	}

	@Override
	public void update(Object obj) {
		Disciplina disciplina = (Disciplina) obj;
		PreparedStatement ps = null;

		if(disciplina.getCreditos()< 0 || disciplina.getCreditos() > 15) {
			throw new DbIntegrityException("Quantidade de créditos inválida (créditos devem ter valor entre 0 e 15)");
		}

		if(disciplina.getNome().isBlank()) {
			throw new DbIntegrityException("A disciplina deve possuir um nome");
		}
		
		DisciplinaDaoJDBC dao = DaoFactory.createDisciplina();
		Disciplina disciplina2 = (Disciplina) dao.findById(disciplina.getId());
		if (disciplina2 == null) {
			throw new DbIntegrityException("Disciplina não encontrada");
		} 
		else {
			try {
				ps = conn.prepareStatement("UPDATE disciplina "
						+ "SET creditos = ?, nome = ? "
						+ "WHERE id_disciplina = ?");

				ps.setDouble(1, disciplina.getCreditos());
				ps.setString(2, disciplina.getNome());
				ps.setInt(3, disciplina.getId());

				ps.executeUpdate();
				
				Aluno_DisciplinaDaoJDBC dao2 = DaoFactory.createAluno_Disciplina();
				List<Object> alunosDisciplinas = dao2.findAll();
				Iterator<Object> ite = alunosDisciplinas.iterator();
				while(ite.hasNext()) {
					Aluno_Disciplina aux = (Aluno_Disciplina) ite.next();
					if(aux.getAtivo()) {
						
						AlunoDaoJDBC daoAluno = DaoFactory.createAluno();
						Bolsista bol = (Bolsista) daoAluno.findById(aux.getAluno().getCpf());
						Double diferencaCreditos = (disciplina2.getCreditos() - disciplina.getCreditos());
						daoAluno.updateCreditos(bol, bol.getCreditos() + diferencaCreditos);
					}
				}
				
			} catch (SQLException e) {
				throw new DbException(e.getMessage());
			} finally {
				DB.closeStatement(ps);
			}
		}

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
			ps = conn.prepareStatement("SELECT * " + "FROM escola.disciplina " + "WHERE disciplina.id_disciplina = ?");
			ps.setInt(1, (int) id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Disciplina disciplina = instantiateDisciplina(rs);

				return disciplina;
			}
			return null;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	private Disciplina instantiateDisciplina(ResultSet rs) throws SQLException {
		Disciplina disciplina = new Disciplina();
		disciplina.setId(rs.getInt("id_disciplina"));
		disciplina.setNome(rs.getString("nome"));
		disciplina.setCreditos(rs.getDouble("creditos"));

		return disciplina;
	}

	@Override
	public List<Object> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * " + "FROM escola.disciplina ");

			rs = ps.executeQuery();
			List<Object> disciplinas = new ArrayList<>();

			while (rs.next()) {
				Disciplina disciplina = instantiateDisciplina(rs);
				disciplinas.add(disciplina);

			}
			return disciplinas;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

}
