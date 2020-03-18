package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.Dao;
import model.dao.DaoFactory;
import model.entities.Turma;

public class TurmaDaoJDBC implements Dao {
	private Connection conn;

	public TurmaDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Object obj) {
		Turma turma = (Turma) obj;

		if (turma.getTurno() == null || turma.getTurno().isBlank() || turma.getTurno().length() > 1) {
			throw new DbIntegrityException("Turno inválido");
		} else if (turma.getId() != null) {
			TurmaDaoJDBC dao = DaoFactory.createTurma();
			Turma turma2 = (Turma) dao.findById(turma.getId());
			if (turma2 != null) {
				throw new DbIntegrityException("Id já inserido");
			}
		} else {
			PreparedStatement ps = null;
			try {
				ps = conn.prepareStatement("INSERT INTO turma " + "(id_turma, turno) " + "VALUES " + "(?, ?) ",
						Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, turma.getId());
				ps.setString(2, turma.getTurno());

				int rowsAffected = ps.executeUpdate();

				if (rowsAffected > 0) {
					ResultSet rs = ps.getGeneratedKeys();
					if (rs.next()) {
						int id = rs.getInt(1);
						turma.setId(id);
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
		Turma turma = (Turma) obj;
		PreparedStatement ps = null;

		if (turma.getTurno() != "M" && turma.getTurno() != "T" && turma.getTurno() != "N" && turma.getTurno() != "I") {
			throw new DbException("Turno inválido (Apenas permitidos: M, T, N, I)");
		}

		TurmaDaoJDBC dao = DaoFactory.createTurma();
		Turma turma2 = (Turma) dao.findById(turma.getId());
		if (turma2 == null) {
			throw new DbIntegrityException("Turma não encontrada");
		} 
		else {
			try {
				ps = conn.prepareStatement("UPDATE turma " + "SET turno = ? " + "WHERE id_turma = ?");

				ps.setString(1, turma.getTurno());
				ps.setDouble(2, turma.getId());

				ps.executeUpdate();
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
			ps = conn.prepareStatement("SELECT * " + "FROM escola.turma " + "WHERE turma.id_turma = ?");
			ps.setInt(1, (int) id);
			rs = ps.executeQuery();

			if (rs.next()) {
				Turma turma = instantiateTurma(rs);
				return turma;
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

	@Override
	public List<Object> findAll() {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT * " + "FROM escola.turma ");

			rs = ps.executeQuery();
			List<Object> turmas = new ArrayList<>();

			while (rs.next()) {
				Turma turma = instantiateTurma(rs);
				turmas.add(turma);
			}
			return turmas;
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
}
