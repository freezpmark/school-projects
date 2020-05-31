package managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;

import tableModel.teaches;

@SuppressWarnings("deprecation")
public class ManagerTeaches extends AllTablesManager{
	
	int inkrement = 0;
	public void setInkrement(int ink) {
		this.inkrement = inkrement + ink;
		if(inkrement < 0)
			inkrement = 0;
	}
	
	int filterInkrement = 0;
	public void setfilterInkrement(int ink) {
		this.filterInkrement = filterInkrement + ink;
		if(filterInkrement < 0)
			filterInkrement = 0;
	}

	public ManagerTeaches(PGPoolingDataSource source){
		super(source);
	}

	protected teaches processRow(ResultSet rs) throws SQLException{
		return(new teaches(rs.getString("teacher_id"), rs.getString("subject_id"), rs.getString("dateFrom")));
	}
	
	@SuppressWarnings("unchecked")
	public List<teaches> getAllTeaches() throws SQLException {
		return(selectQuery("SELECT * FROM teaches LIMIT 25 OFFSET " + inkrement));
	}
	
	@SuppressWarnings("unchecked")
	public List<teaches> getAllTeaches(teaches t) throws SQLException {
		return(selectQuery("SELECT * FROM teaches "
				+			"WHERE teacher_id " + t.getTeacher_id() + " AND subject_id " + t.getSubject_id()
				+ 			" AND datefrom " 	+ t.getDateFrom()
				+			"LIMIT 25 OFFSET " + filterInkrement));
	}
	
	
	public boolean insertTeaches(teaches s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);
			String createStatementString = "INSERT INTO teaches(teacher_id, subject_id, datefrom) " 
					+						"VALUES(?,?,?)";
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			
			int idT = Integer.parseInt(s.getTeacher_id());
			int idS = Integer.parseInt(s.getSubject_id());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getDateFrom());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
			stmt.setInt(1, idT);
			stmt.setInt(2, idS);
			stmt.setDate(3, sql);
			stmt.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
	            try {
	            	System.err.println(e.getMessage());
	            	System.err.print("Transaction is being rolled back");
	                conn.rollback();
	            } catch(SQLException excep) {
	                return false;
	            }
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if(stmt != null)
				stmt.close();
		}
		conn.close();
		return true;
	}
	
	
	
	public boolean deleteRow(int x) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);

            String createStatementString = "SELECT teacher_id, subject_id FROM teaches LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int teach_id = 0, sub_id = 0;
			if(rs.next()) {
				teach_id= rs.getInt("teacher_id"); 
				sub_id 	= rs.getInt("subject_id"); 
			}
            createStatementString = "DELETE FROM teaches WHERE teacher_id = " + teach_id + " AND subject_id = " + sub_id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean deleteFilterRow(int x, teaches filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            
            String createStatementString = "SELECT teacher_id, subject_id FROM teaches "
            		+						"WHERE teacher_id " + filter.getTeacher_id() + " AND subject_id " + filter.getSubject_id()
    				+ 						" AND datefrom " 	+ filter.getDateFrom()
            		+						"LIMIT 1 OFFSET " 	+ (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int teach_id = 0, sub_id = 0;
			if(rs.next()) {
				teach_id= rs.getInt("teacher_id"); 
				sub_id 	= rs.getInt("subject_id");
			}
			createStatementString = "DELETE FROM teaches WHERE teacher_id = " + teach_id + " AND subject_id = " + sub_id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	
	
	public boolean editRow(int x, teaches s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT teacher_id, subject_id FROM teaches LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int teach_id = 0;
			int sub_id = 0;
			if(rs.next()) { 
				teach_id = rs.getInt("teacher_id"); 
				sub_id 	 = rs.getInt("subject_id");
			}
            createStatementString = "UPDATE teaches SET teacher_id = ?, subject_id = ?, datefrom = ? WHERE teacher_id = " + teach_id + " AND subject_id = " + sub_id;
            int idTeacher 	= Integer.parseInt(s.getTeacher_id());
            int idSubject 	= Integer.parseInt(s.getSubject_id());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getDateFrom());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idTeacher);
			stmt.setInt(2, idSubject);
			stmt.setDate(3, sql);

            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
			if (conn != null) {
	            try {
	            	System.err.println(e.getMessage());
	            	System.err.print("Transaction is being rolled back");
	                conn.rollback();
	            } catch(SQLException excep) {
	                
	            }
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if(stmt != null){
				stmt.close();
			}
		}
        conn.close();
        return true;
    }

	public boolean editFilterRow(int x, teaches s, teaches filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT teacher_id, subject_id FROM teaches "
            		+						"WHERE teacher_id " + filter.getTeacher_id()	+ " AND subject_id " + filter.getSubject_id()
    				+ 						" AND datefrom " 	+ filter.getDateFrom()
            		+						"LIMIT 25 OFFSET " 	+ (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int teach_id = 0;
			int sub_id = 0;
			if(rs.next()) {
				teach_id 	= rs.getInt("teacher_id");
				sub_id 		= rs.getInt("subject_id");
			}
			createStatementString = "UPDATE teaches SET teacher_id = ?, subject_id = ?, datefrom = ? WHERE teacher_id = " + teach_id + " AND subject_id = " + sub_id;
            int idTeacher 	= Integer.parseInt(s.getTeacher_id());
            int idSubject 	= Integer.parseInt(s.getSubject_id());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getDateFrom());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idTeacher);
			stmt.setInt(2, idSubject);
			stmt.setDate(3, sql);
			
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
			if (conn != null) {
	            try {
	            	System.err.println(e.getMessage());
	            	System.err.print("Transaction is being rolled back");
	                conn.rollback();
	            } catch(SQLException excep) {
	                
	            }
	        }
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			if(stmt != null)
				stmt.close();
		}
        conn.close();
        return true;
    }
}