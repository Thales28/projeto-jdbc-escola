package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import Masks.CPFMask;
import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try {
			conn = DB.getConnection();
			st = conn.createStatement();
			
			conn.setAutoCommit(false);
			
			rs = st.executeQuery("SELECT * FROM aluno");
			System.out.println("CPF            | NOME");
			while(rs.next()) {
				System.out.println(CPFMask.CPFFormat(rs.getString("cpf")) + ", " + rs.getString("nome"));
			}
//			ps = conn.prepareStatement("INSERT INTO aluno "
//					+ "(cpf, nome, valorMensalidade, endereco, turma, creditos) "
//					+ "VALUES "
//					+ "(?, ?, ?, ?, ?, ?), (?, ?, ?, ?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
//			ps.setString(1, "10101010101");
//			ps.setString(2, "Juremildo Ventura Garrison");
//			ps.setDouble(3, 100.00);
//			ps.setString(4, "Rua Jackson Five, bairro 5");
//			ps.setInt(5,  555);
//			ps.setDouble(6, 15.00);
//			ps.setString(7, "01010101010");
//			ps.setString(8, "Evelin Canvas Draco");
//			ps.setDouble(9, 100.00);
//			ps.setString(10, "Rua Black Smith, bairro 5");
//			ps.setInt(11,  555);
//			ps.setDouble(12, 15.00);
//			
//			int rowsAffected = ps.executeUpdate();
//			
//			if(rowsAffected > 0) {
//				rs = ps.getGeneratedKeys();
//				while(rs.next()) {
//					String cpf = rs.getString(1);
//					System.out.println("Done! Id: "+ cpf);
//				}
//			}
//			else {
//				System.out.println("No rows affected!");
//			}
//			
//			st = conn.createStatement();
//			rs = st.executeQuery("SELECT * FROM aluno");
//			System.out.println("CPF            | NOME");
//			while(rs.next()) {
//				System.out.println(CPFMask.CPFFormat(rs.getString("cpf")) + ", " + rs.getString("nome"));
//			}
//			
//		ps = conn.prepareStatement("UPDATE aluno "
//				+ "SET bolsista = ? "
//				+ "WHERE (cpf = ?) ", Statement.RETURN_GENERATED_KEYS);
//		ps.setString(1, "ATLETA");
//		ps.setString(2, "55555555555");
//		
//		int rowsAffected = ps.executeUpdate();
//		System.out.println("Rows affected: "+rowsAffected);
		
		
		}
		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: "+e.getMessage());
			}
			catch (SQLException ex) {
				throw new DbException("Error trying to rollback! Caused by: "+ex.getMessage());
		
			}
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
			DB.closeStatement(ps);
			DB.closeConnection();
		}
	}
}