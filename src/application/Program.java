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
		
//		try {
//		}
//		catch (SQLException e) {
//			try {
//				conn.rollback();
//				throw new DbException("Transaction rolled back! Caused by: "+e.getMessage());
//			}
//			catch (SQLException ex) {
//				throw new DbException("Error trying to rollback! Caused by: "+ex.getMessage());
//		
//			}
//		}
//		
//		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
			DB.closeStatement(ps);
			DB.closeConnection();
//		}
	}
}