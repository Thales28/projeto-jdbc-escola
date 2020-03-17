package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.Dao;
import model.entities.Bolsista;
import model.entities.Turma;

public class BolsistaDaoJDBC implements Dao{
	
private Connection conn;
	
	public BolsistaDaoJDBC(Connection conn) {
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
					"SELECT escola.aluno.*, escola.turma.* "
					+ "FROM escola.aluno INNER JOIN escola.turma ON escola.aluno.turma = escola.turma.id_turma "
					+ "WHERE aluno.cpf = ?");
			ps.setString(1, (String) id);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				Turma turma = new Turma();
				turma.setId(rs.getInt("id_turma"));
				turma.setTurno(rs.getString("turno"));
				
				Bolsista aluno = new Bolsista();
				
				aluno.setCpf(rs.getString("cpf"));
				aluno.setNome(rs.getString("nome"));
				aluno.setCreditos(rs.getDouble("creditos"));
				aluno.setEndereco(rs.getString("endereco"));
				aluno.setTurma(turma);
				aluno.setValorMensalidade(rs.getDouble("valorMensalidade"));
				aluno.setTipoBolsa(rs.getString("bolsista"));

				return aluno;
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
