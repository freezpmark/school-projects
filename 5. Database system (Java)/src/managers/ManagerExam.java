package managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;
import tableModel.Exam;

@SuppressWarnings("deprecation")
public class ManagerExam extends AllTablesManager{
	
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
	
	public ManagerExam(PGPoolingDataSource source){
		super(source);
	}
	
	protected Exam processRow(ResultSet rs) throws SQLException{
		return(new Exam(rs.getString("id"), rs.getString("subject_id"), rs.getString("content")));
	}
	
	@SuppressWarnings("unchecked")
	public List<Exam> getAllExams() throws SQLException {
		return(selectQuery("SELECT * FROM exam LIMIT 25 OFFSET " + inkrement));
	}
	
	@SuppressWarnings("unchecked")
	public List<Exam> getAllExams(Exam exam) throws SQLException {
		return(selectQuery("SELECT * FROM exam "
				+			"WHERE content LIKE '%" + exam.getContent() + "%' AND subject_id " + exam.getSubjId()
				+			"LIMIT 25 OFFSET " + filterInkrement));
	}
	
	public int getResultAmount(Object data) throws SQLException {
		Connection conn2 = null;
		int pocet = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
			String createStatementString = "SELECT count(result.id) AS poc "
				+							"FROM exam "
				+							"JOIN result "
				+ 							"ON result.exam_id = exam.id "
				+							"GROUP BY exam.id "
				+							"HAVING exam.id = " + data;
		
			Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);

			if(rs.next())
				pocet = rs.getInt("poc");
			conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		return pocet;
	}
	
	public boolean insertExam(Exam s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);

			String createStatementString = "INSERT INTO exam(id, subject_id, content) VALUES(nextval('exam_id_seq'),?,?)";
			int FK = Integer.parseInt(s.getSubjId());
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, FK);
			stmt.setString(2, s.getContent());
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
            String createStatementString = "SELECT id FROM exam LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM exam WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean deleteFilterRow(int x, Exam filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM exam "
            		+						"WHERE content LIKE '%" + filter.getContent() + "%' AND subject_id " + filter.getSubjId()
            		+						"LIMIT 1 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM exam WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException es) {
            es.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	
	public boolean editRow(int x, Exam s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM exam LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "UPDATE exam SET subject_id = ?, content = ? WHERE id = " + id;
            int idSubj = Integer.parseInt(s.getSubjId());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idSubj);
			stmt.setString(2, s.getContent());

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
	

	public boolean editFilterRow(int x, Exam s, Exam filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM exam "
            		+						"WHERE content LIKE '%" + filter.getContent() + "%' AND subject_id " + filter.getSubjId()
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next())
				 id = rs.getInt("id");
            createStatementString = "UPDATE exam SET subject_id = ?, content = ? WHERE id = " + id;
            int idSubj = Integer.parseInt(s.getSubjId());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idSubj);
			stmt.setString(2, s.getContent());

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
	
	public Integer getId(int x) throws SQLException {
		Connection conn = null;
        int id = 0;
        if(x < 0)
        	return -1;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM teacher LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			if(rs.next())
				 id = rs.getInt("id"); 
            
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return id;
    }
	
	public Integer getId(int x, Exam filter) throws SQLException {
		Connection conn = null;
        int id = 0;
        if(x < 0)
        	return -1;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM exam "
            		+						"WHERE content LIKE '%" + filter.getContent() + "%' AND subject_id " + filter.getSubjId()
    				+						"LIMIT 1 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			if(rs.next())
				 id = rs.getInt("id"); 
            
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return id;
    }
	
	public String viewDetail(int id) throws SQLException {
		Connection conn2 = null;
		float avegMarks = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
            String createStatementString = "SELECT avg(result.mark) AS aveg "
            		+						"FROM exam "
            		+						"JOIN result "
            		+						"ON exam.id = result.exam_id "
            		+						"GROUP BY exam_id "
            		+						"HAVING exam_id = " + id;
            
            Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			if(rs.next())
				avegMarks = rs.getFloat("aveg"); 
			
            conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		return "Total mark average: " + avegMarks; 
	}
}