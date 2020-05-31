package managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;

import tableModel.Result;

@SuppressWarnings("deprecation")
public class ManagerResult extends AllTablesManager{
	
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
	
	public ManagerResult(PGPoolingDataSource source){
		super(source);
	}
	
	protected Result processRow(ResultSet rs) throws SQLException{
		return(new Result(rs.getString("id"), rs.getString("exam_id"), rs.getString("mark")));
	}
	
	@SuppressWarnings("unchecked")
	public List<Result> getAllResults() throws SQLException {
		return(selectQuery("SELECT * FROM result LIMIT 25 OFFSET " + inkrement));
	}
	
	@SuppressWarnings("unchecked")
	public List<Result> getAllResults(Result s) throws SQLException {
		return(selectQuery("SELECT * FROM result "
				+			"WHERE mark " + s.getMark()	+ " AND exam_id " + s.getExamId()
				+			" LIMIT 25 OFFSET " + filterInkrement));
	}
	
	public boolean insertResult(Result s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);

			String createStatementString = "INSERT INTO result(id, exam_id, mark) VALUES(nextval('result_id_seq'),?,?)";
			int FK = Integer.parseInt(s.getExamId());
			int mark = Integer.parseInt(s.getMark());
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, FK);
			stmt.setInt(2, mark);
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
            String createStatementString = "SELECT id FROM result LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM result WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean deleteFilterRow(int x, Result filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM result "
            		+						"WHERE mark " + filter.getMark() + " AND exam_id " + filter.getExamId()
            		+						" LIMIT 1 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM result WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean editRow(int x, Result s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM result LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "UPDATE result SET mark = ?, exam_id = ? WHERE id = " + id;
            int mark = Integer.parseInt(s.getMark());
            int idExam = Integer.parseInt(s.getExamId());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, mark);
			stmt.setInt(2, idExam);

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
		} finally {
			if(stmt != null){
				stmt.close();
			}
		}
        conn.close();
        return true;
    }

	public boolean editFilterRow(int x, Result s, Result filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM result "
            		+						"WHERE mark " + filter.getMark()	+ " AND exam_id " + filter.getExamId()
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next())
				 id = rs.getInt("id");
            createStatementString = "UPDATE result SET mark = ?, exam_id = ? WHERE id = " + id;
            int mark = Integer.parseInt(s.getMark());
            int idExam = Integer.parseInt(s.getExamId());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, mark);
			stmt.setInt(2, idExam);

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
		} finally {
			if(stmt != null)
				stmt.close();
		}
        conn.close();
        return true;
    }
}