package managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import org.postgresql.ds.PGPoolingDataSource;

import tableModel.studies;

@SuppressWarnings("deprecation")
public class ManagerStudies extends AllTablesManager{
	
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

	public ManagerStudies(PGPoolingDataSource source){
		super(source);
	}

	protected studies processRow(ResultSet rs) throws SQLException{
		return(new studies(rs.getString("student_id"), rs.getString("subject_id"), rs.getString("dateTo"), rs.getString("grade")));
	}
	
	@SuppressWarnings("unchecked")
	public List<studies> getAllStudies() throws SQLException {
		return(selectQuery("SELECT * FROM studies LIMIT 25 OFFSET " + inkrement));
	}
	
	@SuppressWarnings("unchecked")
	public List<studies> getAllStudies(studies s) throws SQLException {
		return(selectQuery("SELECT * FROM studies "
				+			"WHERE student_id " + s.getStudent_id() + " AND subject_id " + s.getSubject_id()
				+ 			" AND dateto " 		+ s.getDateTo() 	+ " AND grade " 	 + s.getGrade()
				+			"LIMIT 25 OFFSET " + filterInkrement));
	}
	
	public boolean insertStudies(studies s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);
			String createStatementString = "INSERT INTO studies(student_id, subject_id, dateto, grade) " 
					+						"VALUES(?,?,?,?)";
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			
			int idT = Integer.parseInt(s.getStudent_id());
			int idS = Integer.parseInt(s.getSubject_id());
			int grade = Integer.parseInt(s.getGrade());
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getDateTo());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
			stmt.setInt(1, idT);
			stmt.setInt(2, idS);
			stmt.setDate(3, sql);
			stmt.setInt(4, grade);
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
			if(stmt != null){
				stmt.close();
			}
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

            String createStatementString = "SELECT student_id, subject_id FROM studies LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int stud_id = 0, sub_id = 0;
			if(rs.next()) {
				stud_id = rs.getInt("student_id"); 
				sub_id 	= rs.getInt("subject_id"); 
			}
            createStatementString = "DELETE FROM studies WHERE student_id = " + stud_id + " AND subject_id = " + sub_id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }

	public boolean deleteFilterRow(int x, studies filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            
            String createStatementString = "SELECT student_id, subject_id FROM studies "
            		+						"WHERE student_id " + filter.getStudent_id() 	+ " AND subject_id " + filter.getSubject_id()
    				+ 						" AND dateto " 		+ filter.getDateTo() 		+ " AND grade " 	 + filter.getGrade()
            		+						"LIMIT 1 OFFSET " 	+ (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int stud_id = 0, sub_id = 0;
			if(rs.next()) {
				stud_id = rs.getInt("student_id"); 
				sub_id 	= rs.getInt("subject_id");
			}
			createStatementString = "DELETE FROM studies WHERE student_id = " + stud_id + " AND subject_id = " + sub_id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	
	public boolean editRow(int x, studies s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT student_id, subject_id FROM studies LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int stud_id = 0;
			int sub_id = 0;
			if(rs.next()) { 
				stud_id = rs.getInt("student_id"); 
				sub_id 	= rs.getInt("subject_id");
			}
            createStatementString = "UPDATE studies SET student_id = ?, subject_id = ?, dateto = ?, grade = ? WHERE student_id = " + stud_id + " AND subject_id = " + sub_id;
            int idStudent 	= Integer.parseInt(s.getStudent_id());
            int idSubject 	= Integer.parseInt(s.getSubject_id());
            int grade		= Integer.parseInt(s.getGrade());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getDateTo());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idStudent);
			stmt.setInt(2, idSubject);
			stmt.setDate(3, sql);
			stmt.setInt(4, grade);
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
	

	public boolean editFilterRow(int x, studies s, studies filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT student_id, subject_id FROM studies "
            		+						"WHERE student_id " + filter.getStudent_id()	+ " AND subject_id " + filter.getSubject_id()
    				+ 						" AND dateto " 		+ filter.getDateTo() 		+ " AND grade " 	 + filter.getGrade()
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int stud_id = 0;
			int sub_id = 0;
			if(rs.next()) {
				stud_id = rs.getInt("student_id");
				sub_id 	= rs.getInt("subject_id");
			}
			createStatementString = "UPDATE studies SET student_id = ?, subject_id = ?, dateto = ?, grade = ? WHERE student_id = " + stud_id + " AND subject_id = " + sub_id;
			int idStudent 	= Integer.parseInt(s.getStudent_id());
            int idSubject 	= Integer.parseInt(s.getSubject_id());
            int grade		= Integer.parseInt(s.getGrade());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getDateTo());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idStudent);
			stmt.setInt(2, idSubject);
			stmt.setDate(3, sql);
			stmt.setInt(4, grade);
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