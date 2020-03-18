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
import db.DbBusinessRuleException;
import db.DbException;
import db.DbIntegrityException;
import model.dao.Dao;
import model.dao.DaoFactory;
import model.entities.Aluno;
import model.entities.Aluno_Disciplina;
import model.entities.Bolsista;
import model.entities.Turma;

public class AlunoDaoJDBC implements Dao {

	private Connection conn;

	public AlunoDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Object obj) {
		Bolsista bolsista = (Bolsista) obj;
		
		if(bolsista.getCpf().isBlank()) {
			throw new DbException("CPF não preenchido");
		}
		else if(bolsista.getCreditos()> 15) {
			throw new DbBusinessRuleException("Aluno só pode ter no máximo 15 créditos");
		}
		else if(bolsista.getCreditos() < 0) {
			throw new DbException("Créditos negativos");
		}
		else if(bolsista.getEndereco().isBlank()) {
			throw new DbException("Endereço não preenchido");
		}
		else if(bolsista.getNome().isBlank()) {
			throw new DbException("Nome não preenchido");
		}
		else if(bolsista.getTurma() == null) {
			throw new DbException("Turma não preenchida");
		}
		
		else if (bolsista.getTurma() != null) {
			TurmaDaoJDBC dao = DaoFactory.createTurma();
			Turma turma = (Turma) dao.findById(bolsista.getTurma().getId());
			if (turma == null) {
				throw new DbIntegrityException("Turma não encontrada");
			}
			else {
				List<Bolsista> alunos = findByTurma(turma);
				if(alunos.size()>4) {
					throw new DbBusinessRuleException("Turma lotada, favor tentar outra");
				}
			}
		}
		
		else if(bolsista.getValorMensalidade() < 0) {
			throw new DbException("Mensalidade negativa");
		}
		
		else if ((bolsista.getTipoBolsa().isBlank() || bolsista.getTipoBolsa() == "NÃO") && bolsista.getValorMensalidade() == 0) {
			bolsista.setTipoBolsa("SIM");
		}
		
		else if (bolsista.getCpf() != null) {
			AlunoDaoJDBC dao = DaoFactory.createAluno();
			Bolsista bolsista2 = (Bolsista) dao.findById(bolsista.getCpf());
			if (bolsista2 != null) {
				throw new DbIntegrityException("CPF já inserido");
			}
		} else {
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("INSERT INTO aluno " 
				+ "(cpf, nome, valorMensalidade, endereco, turma, creditos, bolsista) "
				+ "VALUES "
				+ "(?, ?, ?, ?, ?, ?, ?) ");
				ps.setString(1, bolsista.getCpf());
				ps.setString(2, bolsista.getNome());
				ps.setDouble(3, bolsista.getValorMensalidade());
				ps.setString(4, bolsista.getEndereco());
				ps.setInt(5, bolsista.getTurma().getId());
				ps.setDouble(6, bolsista.getCreditos());
				ps.setString(7, bolsista.getTipoBolsa());

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
	}
	
	public void updateCreditos(Bolsista bolsista, Double creditos) {
		PreparedStatement ps = null;
		
		if(bolsista.getCpf().isBlank()) {
			throw new DbException("CPF inválido");
		}
		
		else if(creditos < 0) {
			throw new DbException("quantidade de créditos inválida (o valor dos créditos deve ser entre 0 e 15");
		}
		
		else if (creditos > 15) {
			creditos = 15.00;
		}
		
		AlunoDaoJDBC dao = DaoFactory.createAluno();
		Bolsista aluno = (Bolsista) dao.findById(bolsista.getCpf());
		
		if(aluno == null) {
			throw new DbException("Aluno não encontrado");
		}
		
		try {
			ps = conn.prepareStatement(
					"UPDATE aluno "
					+ "SET creditos = ? "
					+ "WHERE cpf = ?");
			
			ps.setDouble(1, creditos);
			ps.setString(2, bolsista.getCpf());
			
			ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}
	
	@Override
	public void update(Object obj) {
		Bolsista bolsista = (Bolsista) obj;
		
		if(bolsista.getCpf().isBlank()) {
			throw new DbException("CPF inválido");
		}
		
		AlunoDaoJDBC dao = DaoFactory.createAluno();
		Bolsista aluno = (Bolsista) dao.findById(bolsista.getCpf());
		
		if(aluno == null) {
			throw new DbException("Aluno não encontrado");
		}
		
		else if(bolsista.getEndereco().isBlank()) {
			throw new DbException("Endereço não preenchido");
		}
		else if(bolsista.getNome().isBlank()) {
			throw new DbException("Nome não preenchido");
		}
		else if(bolsista.getTurma() == null || bolsista.getTurma().getId() == 0 ||bolsista.getTurma().getId() == null) {
			throw new DbException("Turma inválida");
		}
		
		TurmaDaoJDBC daoTurma = DaoFactory.createTurma();
		Turma turma = (Turma) daoTurma.findById(bolsista.getTurma().getId());
		if (turma == null) {
			throw new DbIntegrityException("Turma não encontrada");
		}
		else {
			List<Bolsista> alunos = findByTurma(turma);
			if(alunos.size()>4) {
				throw new DbBusinessRuleException("Turma lotada, favor tentar outra");
			}
		}
		
		if ((bolsista.getTipoBolsa().isBlank() || bolsista.getTipoBolsa() == "NÃO") && bolsista.getValorMensalidade() == 0) {
			bolsista.setTipoBolsa("SIM");
		}
		
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement(
					"UPDATE aluno "
					+ "SET nome = ?, valorMensalidade = ?, endereco = ?, turma = ?, bolsista = ? "
					+ "WHERE cpf = ?");
			
			ps.setString(1, bolsista.getNome());
			ps.setDouble(2, bolsista.getValorMensalidade());
			ps.setString(3, bolsista.getEndereco());
			ps.setInt(4, bolsista.getTurma().getId());
			ps.setString(5, bolsista.getTipoBolsa());
			ps.setString(6, bolsista.getCpf());
			
			ps.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void deleteById(Object id) {
		PreparedStatement ps = null;

		AlunoDaoJDBC daoAluno = DaoFactory.createAluno();
		Aluno aluno = (Aluno) daoAluno.findById(id);

		Aluno_DisciplinaDaoJDBC daoAlunoDisciplina = DaoFactory.createAluno_Disciplina();
		List<Aluno_Disciplina> lista = daoAlunoDisciplina.findByAluno(aluno.getCpf());
		for (Aluno_Disciplina alunoDisciplina : lista) {
			Aluno_Disciplina AL = new Aluno_Disciplina(alunoDisciplina.getAluno(), alunoDisciplina.getDisciplina(), false);
			daoAlunoDisciplina.update(AL);
		}
		try {
			ps = conn.prepareStatement("DELETE FROM aluno WHERE cpf = ? ");

			ps.setString(1, aluno.getCpf());
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
			ps = conn.prepareStatement("SELECT escola.aluno.*, escola.turma.* "
					+ "FROM escola.aluno INNER JOIN escola.turma ON escola.aluno.turma = escola.turma.id_turma "
					+ "WHERE aluno.cpf = ?");
			ps.setString(1, (String) id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Turma turma = instantiateTurma(rs);

				Bolsista aluno = instantiateBolsista(rs, turma);

				return aluno;
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

	private Turma instantiateTurma(ResultSet rs) throws SQLException {
		Turma turma = new Turma();
		turma.setId(rs.getInt("id_turma"));
		turma.setTurno(rs.getString("turno"));
		return turma;
	}

	private Bolsista instantiateBolsista(ResultSet rs, Turma turma) throws SQLException {
		Bolsista aluno = new Bolsista();
		aluno.setCpf(rs.getString("cpf"));
		aluno.setCreditos(rs.getDouble("creditos"));
		aluno.setEndereco(rs.getString("endereco"));
		aluno.setNome(rs.getString("nome"));
		aluno.setTurma(turma);
		aluno.setValorMensalidade(rs.getDouble("valorMensalidade"));
		aluno.setTipoBolsa(rs.getString("bolsista"));
		return aluno;
	}

	public List<Bolsista> findByTurma(Turma turma) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT escola.aluno.*, escola.turma.* "
					+ "FROM escola.aluno INNER JOIN escola.turma ON escola.aluno.turma = escola.turma.id_turma "
					+ "WHERE turma.id_turma = ?");
			ps.setInt(1, turma.getId());
			rs = ps.executeQuery();
			Map<Integer, Turma> map = new HashMap<>();
			List<Bolsista> alunos = new ArrayList<>();

			while (rs.next()) {

				Turma localturma = map.get(rs.getInt("id_turma"));

				if (localturma == null) {
					localturma = instantiateTurma(rs);
					map.put(rs.getInt("id_turma"), localturma);
				}

				Bolsista aluno = instantiateBolsista(rs, localturma);
				alunos.add(aluno);
			}
			return alunos;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Object> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT escola.aluno.*, escola.turma.* "
					+ "FROM escola.aluno INNER JOIN escola.turma ON escola.aluno.turma = escola.turma.id_turma ");

			rs = ps.executeQuery();
			Map<Integer, Turma> map = new HashMap<>();
			List<Object> alunos = new ArrayList<>();

			while (rs.next()) {

				Turma localturma = map.get(rs.getInt("id_turma"));

				if (localturma == null) {
					localturma = instantiateTurma(rs);
					map.put(rs.getInt("id_turma"), localturma);
				}

				Bolsista aluno = instantiateBolsista(rs, localturma);
				alunos.add(aluno);
			}
			return alunos;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

}
