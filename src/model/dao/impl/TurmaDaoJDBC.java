package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.Dao;
import model.entities.Turma;

public class TurmaDaoJDBC implements Dao{
	private Connection conn;
	
	public TurmaDaoJDBC(Connection conn) {
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
					+ "FROM escola.turma "
					+ "WHERE turma.id_turma = ?");
			ps.setInt(1, (int) id);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Turma turma = instantiateTurma(rs);
				return turma;
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

	private Turma instantiateTurma(ResultSet rs) throws SQLException {
		Turma turma = new Turma();
		turma.setId(rs.getInt("id_turma"));
		turma.setTurno(rs.getString("turno"));
		return turma;
	}
	@Override
	public List<Object> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
