package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.Dao;
import model.entities.Disciplina;

public class DisciplinaDaoJDBC implements Dao{
private Connection conn;
	
	public DisciplinaDaoJDBC(Connection conn) {
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
					+ "FROM escola.disciplina "
					+ "WHERE disciplina.id_disciplina = ?");
			ps.setInt(1, (int) id);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Disciplina disciplina = instantiateDisciplina(rs);

				return disciplina;
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

	private Disciplina instantiateDisciplina(ResultSet rs) throws SQLException {
		Disciplina disciplina = new Disciplina();
		disciplina.setId(rs.getInt("id_disciplina"));
		disciplina.setNome(rs.getString("nome"));
		disciplina.setCreditos(rs.getDouble("creditos"));
		
		return disciplina;
	}
	@Override
	public List<Object> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
