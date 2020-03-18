package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.Dao;
import model.dao.DaoFactory;
import model.entities.Aluno;
import model.entities.Aluno_Disciplina;
import model.entities.Aluno_Disciplina_PK;
import model.entities.Bolsista;
import model.entities.Disciplina;
import model.entities.Turma;

public class Aluno_DisciplinaDaoJDBC implements Dao {
	private Connection conn;

	public Aluno_DisciplinaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Object obj) {
		Aluno_Disciplina alunoDisciplina = (Aluno_Disciplina) obj;

		if (alunoDisciplina.getAluno().getCpf().isBlank()) {
			throw new DbException("Aluno inválido");
		} else if (alunoDisciplina.getDisciplina().getId() == null || alunoDisciplina.getDisciplina().getId() < 0) {
			throw new DbException("Disciplina inválida");
		}

		AlunoDaoJDBC daoAluno = DaoFactory.createAluno();
		Bolsista aluno = (Bolsista) daoAluno.findById(alunoDisciplina.getAluno().getCpf());
		if (aluno == null) {
			throw new DbException("Aluno não encontrado");
		}

		DisciplinaDaoJDBC daoDisciplina = DaoFactory.createDisciplina();
		Disciplina disciplina = (Disciplina) daoDisciplina.findById(alunoDisciplina.getDisciplina().getId());
		if (disciplina == null) {
			throw new DbException("Disciplina não encontrada");
		}

		else if (aluno.getCreditos() < disciplina.getCreditos()) {
			throw new DbException("Aluno não possui créditos suficientes");
		}

		else {
			daoAluno.updateCreditos(aluno, aluno.getCreditos() - disciplina.getCreditos());
		}

		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"INSERT INTO aluno_disciplina " + "(aluno, disciplina, ativo) " + "VALUES " + "(?, ?, ?) ");
			ps.setString(1, alunoDisciplina.getAluno().getCpf());
			ps.setInt(2, alunoDisciplina.getDisciplina().getId());
			ps.setInt(3, 1);
			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
			} else {
				throw new DbException("Erro inesperado! Nenhuma linha foi afetada!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public void update(Object obj) {
		PreparedStatement ps = null;

		Aluno_Disciplina alunoDisciplina = (Aluno_Disciplina) obj;

		Aluno_Disciplina_PK ALPK = new Aluno_Disciplina_PK();
		ALPK.setAluno(alunoDisciplina.getAluno());
		ALPK.setDisciplina(alunoDisciplina.getDisciplina());

		Aluno_DisciplinaDaoJDBC daoAlunoDisciplina = DaoFactory.createAluno_Disciplina();
		Aluno_Disciplina alunoDisciplinaOLD = (Aluno_Disciplina) daoAlunoDisciplina.findById(ALPK);

		AlunoDaoJDBC daoAluno = DaoFactory.createAluno();
		Bolsista aluno = (Bolsista) daoAluno.findById(alunoDisciplina.getAluno().getCpf());
		if (aluno == null) {
			throw new DbException("Aluno não encontrado");
		}

		DisciplinaDaoJDBC daoDisciplina = DaoFactory.createDisciplina();
		Disciplina disciplina = (Disciplina) daoDisciplina.findById(alunoDisciplina.getDisciplina().getId());
		if (disciplina == null) {
			throw new DbException("Disciplina não encontrada");
		}

		if (alunoDisciplinaOLD.getAtivo() && !alunoDisciplina.getAtivo()) {
			daoAluno.updateCreditos(aluno, aluno.getCreditos() + disciplina.getCreditos());
		} else if (!alunoDisciplinaOLD.getAtivo() && alunoDisciplina.getAtivo()) {
			if (aluno.getCreditos() > disciplina.getCreditos()) {
				daoAluno.updateCreditos(aluno, aluno.getCreditos() - disciplina.getCreditos());
			} else
				throw new DbException("Aluno não possui créditos suficientes");
		}

		try {
			ps = conn.prepareStatement(
					"UPDATE aluno_disciplina " + "SET ativo = ? " + "WHERE aluno = ? AND disciplina = ?");

			int i = 0;
			if (alunoDisciplina.getAtivo()) {
				i = 1;
			}
			ps.setInt(1, i);
			ps.setString(2, alunoDisciplina.getAluno().getCpf());
			ps.setInt(3, alunoDisciplina.getDisciplina().getId());

			ps.executeUpdate();
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public void deleteById(Object id) {
		PreparedStatement ps = null;

		Aluno_DisciplinaDaoJDBC daoAlunoDisciplina = DaoFactory.createAluno_Disciplina();
		Aluno_Disciplina alunoDisciplina = (Aluno_Disciplina) daoAlunoDisciplina.findById(id);
		if(alunoDisciplina.getAtivo()) {
			AlunoDaoJDBC daoAluno = DaoFactory.createAluno();
			Bolsista aluno = (Bolsista) daoAluno.findById(alunoDisciplina.getAluno().getCpf());
			
			DisciplinaDaoJDBC daoDisciplina = DaoFactory.createDisciplina();
			Disciplina disciplina = (Disciplina) daoDisciplina.findById(alunoDisciplina.getDisciplina().getId());
			
			daoAluno.updateCreditos(aluno, aluno.getCreditos() + disciplina.getCreditos());
		}
		try {
			ps = conn.prepareStatement("DELETE FROM aluno_disciplina WHERE aluno = ? AND disciplina = ? ");

			ps.setString(1, alunoDisciplina.getAluno().getCpf());
			ps.setInt(2, alunoDisciplina.getDisciplina().getId());
			ps.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}

	}

	@Override
	public Object findById(Object id) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * "
					+ "FROM escola.aluno_disciplina INNER JOIN escola.aluno ON escola.aluno_disciplina.aluno = escola.aluno.cpf "
					+ "INNER JOIN escola.disciplina ON escola.aluno_disciplina.disciplina = escola.disciplina.id_disciplina "
					+ "INNER JOIN turma ON aluno.turma = turma.id_turma "
					+ "WHERE (aluno_disciplina.aluno = ?) " + "AND (aluno_disciplina.disciplina = ?)");

			Aluno_Disciplina_PK ADPK = (Aluno_Disciplina_PK) id;
			ps.setString(1, ADPK.getAluno().getCpf());
			ps.setInt(2, ADPK.getDisciplina().getId());
			rs = ps.executeQuery();

			if (rs.next()) {

				Turma turma = instantiateTurma(rs);

				Aluno aluno = instantiateAluno(rs, turma);

				Disciplina disciplina = instantiateDisciplina(rs);

				Aluno_Disciplina alunoDisciplina = instantiateAlunoDisciplina(rs, aluno, disciplina);

				return alunoDisciplina;
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

	private Aluno_Disciplina instantiateAlunoDisciplina(ResultSet rs, Aluno aluno, Disciplina disciplina)
			throws SQLException {
		Aluno_Disciplina alunoDisciplina = new Aluno_Disciplina();
		alunoDisciplina.setAluno(aluno);
		Boolean ativo = false;
		if (rs.getInt("ativo") == 1) {
			ativo = true;
		}
		alunoDisciplina.setAtivo(ativo);
		alunoDisciplina.setDisciplina(disciplina);
		return alunoDisciplina;
	}

	private Aluno instantiateAluno(ResultSet rs, Turma turma) throws SQLException {
		Aluno aluno = new Aluno();
		aluno.setCpf(rs.getString("cpf"));
		aluno.setCreditos(rs.getDouble(9));
		aluno.setEndereco(rs.getString("endereco"));
		aluno.setNome(rs.getString(5));
		aluno.setTurma(turma);
		aluno.setValorMensalidade(rs.getDouble("valorMensalidade"));
		return aluno;
	}

	private Disciplina instantiateDisciplina(ResultSet rs) throws SQLException {
		Disciplina disciplina = new Disciplina();
		disciplina.setCreditos(rs.getDouble(13));
		disciplina.setId(rs.getInt("id_disciplina"));
		disciplina.setNome(rs.getString(12));
		return disciplina;
	}

	private Turma instantiateTurma(ResultSet rs) throws SQLException {
		Turma turma = new Turma();
		turma.setId(rs.getInt("id_turma"));
		turma.setTurno(rs.getString("turno"));
		return turma;
	}

	@Override
	public List<Object> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * "
					+ "FROM escola.aluno_disciplina INNER JOIN escola.aluno ON escola.aluno_disciplina.aluno = escola.aluno.cpf "
					+ "INNER JOIN escola.disciplina ON escola.aluno_disciplina.disciplina = escola.disciplina.id_disciplina ");

			rs = ps.executeQuery();
			Map<Integer, Turma> mapTurma = new HashMap<>();
			Map<String, Aluno> mapAluno = new HashMap<>();
			Map<Integer, Disciplina> mapDisciplina = new HashMap<>();
			List<Object> alunosDisciplinas = new ArrayList<>();

			while (rs.next()) {

				Turma turma = mapTurma.get(rs.getInt("id_turma"));

				if (turma == null) {
					turma = instantiateTurma(rs);
					mapTurma.put(rs.getInt("id_turma"), turma);
				}

				Aluno aluno = mapAluno.get(rs.getString("cpf"));

				if (aluno == null) {
					aluno = instantiateAluno(rs, turma);
					mapAluno.put(rs.getString("cpf"), aluno);
				}

				Disciplina disciplina = mapDisciplina.get(rs.getInt("id_disciplina"));

				if (disciplina == null) {
					disciplina = instantiateDisciplina(rs);
					mapDisciplina.put(rs.getInt("id_disciplina"), disciplina);
				}

				Aluno_Disciplina alunoDisciplina = instantiateAlunoDisciplina(rs, aluno, disciplina);
				alunosDisciplinas.add(alunoDisciplina);

			}
			return alunosDisciplinas;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	public List<Aluno_Disciplina> findByAluno(String cpf) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * "
					+ "FROM escola.aluno_disciplina INNER JOIN escola.aluno ON escola.aluno_disciplina.aluno = escola.aluno.cpf "
					+ "INNER JOIN escola.disciplina ON escola.aluno_disciplina.disciplina = escola.disciplina.id_disciplina "
					+ "INNER JOIN turma ON aluno.turma = turma.id_turma "
					+ "WHERE (aluno_disciplina.aluno = ?) AND (aluno_disciplina.ativo = 1)");

			ps.setString(1, cpf);
			rs = ps.executeQuery();
			
			Map<Integer, Turma> mapTurma = new HashMap<>();
			Map<String, Aluno> mapAluno = new HashMap<>();
			Map<Integer, Disciplina> mapDisciplina = new HashMap<>();
			List<Aluno_Disciplina> alunosDisciplinas = new ArrayList<>();
			
			while (rs.next()) {

				Turma turma = mapTurma.get(rs.getInt("turma"));

				if (turma == null) {
					turma = instantiateTurma(rs);
					mapTurma.put(rs.getInt("turma"), turma);
				}

				Aluno aluno = mapAluno.get(rs.getString("cpf"));

				if (aluno == null) {
					aluno = instantiateAluno(rs, turma);
					mapAluno.put(rs.getString("cpf"), aluno);
				}

				Disciplina disciplina = mapDisciplina.get(rs.getInt("id_disciplina"));

				if (disciplina == null) {
					disciplina = instantiateDisciplina(rs);
					mapDisciplina.put(rs.getInt("id_disciplina"), disciplina);
				}

				Aluno_Disciplina alunoDisciplina = instantiateAlunoDisciplina(rs, aluno, disciplina);
				alunosDisciplinas.add(alunoDisciplina);

			}
			return alunosDisciplinas;

		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	public List<Aluno_Disciplina> findByDisciplina(Integer id_disciplina) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * "
					+ "FROM escola.aluno_disciplina INNER JOIN escola.aluno ON escola.aluno_disciplina.aluno = escola.aluno.cpf "
					+ "INNER JOIN escola.disciplina ON escola.aluno_disciplina.disciplina = escola.disciplina.id_disciplina "
					+ "WHERE (aluno_disciplina.disciplina = ?) AND (aluno_disciplina.ativo = 1)");

			ps.setInt(1, id_disciplina);
			rs = ps.executeQuery();
			
			Map<Integer, Turma> mapTurma = new HashMap<>();
			Map<String, Aluno> mapAluno = new HashMap<>();
			Map<Integer, Disciplina> mapDisciplina = new HashMap<>();
			List<Aluno_Disciplina> alunosDisciplinas = new ArrayList<>();
			
			while (rs.next()) {

				Turma turma = mapTurma.get(rs.getInt("id_turma"));

				if (turma == null) {
					turma = instantiateTurma(rs);
					mapTurma.put(rs.getInt("id_turma"), turma);
				}

				Aluno aluno = mapAluno.get(rs.getString("cpf"));

				if (aluno == null) {
					aluno = instantiateAluno(rs, turma);
					mapAluno.put(rs.getString("cpf"), aluno);
				}

				Disciplina disciplina = mapDisciplina.get(rs.getInt("id_disciplina"));

				if (disciplina == null) {
					disciplina = instantiateDisciplina(rs);
					mapDisciplina.put(rs.getInt("id_disciplina"), disciplina);
				}

				Aluno_Disciplina alunoDisciplina = instantiateAlunoDisciplina(rs, aluno, disciplina);
				alunosDisciplinas.add(alunoDisciplina);

			}
			return alunosDisciplinas;

		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
}
