package managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;
import tableModel.Subject;

@SuppressWarnings("deprecation")
public class ManagerSubject extends AllTablesManager {
	
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
	
	public ManagerSubject(PGPoolingDataSource source){
		super(source);
	}
	
	protected Subject processRow(ResultSet rs) throws SQLException{
		return(new Subject(rs.getString("id"), rs.getString("type_id"), rs.getString("name"), rs.getString("description")));
	}
	
	@SuppressWarnings("unchecked")
	public List<Subject> getAllSubjects() throws SQLException {
		return(selectQuery("SELECT * FROM subject LIMIT 25 OFFSET " + inkrement));
	}
	
	@SuppressWarnings("unchecked")
	public List<Subject> getAllSubjects(Subject s) throws SQLException {
		return(selectQuery("SELECT * FROM subject "
				+			"WHERE name LIKE '%" + s.getName() + "%' AND type_id " + s.getType_id()
				+			"LIMIT 25 OFFSET " + filterInkrement));
	}


	public String getType(Object data) throws SQLException {
		Connection conn2 = null;
		String typ = "";
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			String createStatementString = "SELECT type_of_discipline.disc "
				+						"FROM subject "
				+						"JOIN type_of_discipline "
				+ 						"ON subject.type_id = type_of_discipline.id "
				+						"WHERE subject.id = " + data;
		
			Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);

			if(rs.next())
				typ = rs.getString("disc");
			conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		return typ;
	}
	
	public int getStudyAmount(Object data) throws SQLException {
		Connection conn2 = null;
		int pocet = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
			String createStatementString = "SELECT count(studies.subject_id) AS poc "
				+						"FROM subject "
				+						"JOIN studies "
				+ 						"ON subject.id = studies.subject_id "
				+						"GROUP BY subject.id "
				+						"HAVING subject.id = " + data;
				
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
	
	public int getTeachAmount(Object data) throws SQLException {
		Connection conn2 = null;
		int pocet = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
			String createStatementString = "SELECT count(teaches.subject_id) AS poc "
				+							"FROM subject "
				+							"JOIN teaches "
				+ 							"ON subject.id = teaches.subject_id "
				+							"GROUP BY subject.id "
				+							"HAVING subject.id = " + data;
		
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
	
	public int getTypeAmount(Object data) throws SQLException {
		Connection conn2 = null;
		int pocet = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
			String createStatementString = "SELECT count(subject.id) AS poc "
				+							"FROM type_of_discipline "
				+							"JOIN subject "
				+ 							"ON subject.type_id = type_of_discipline.id "
				+							"GROUP BY type_of_discipline.id "
				+							"HAVING type_of_discipline.id = " + data;
		
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
	
	public boolean insertSubject(Subject s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);

			String createStatementString = "INSERT INTO subject(id, type_id, name, description) VALUES(nextval('subject_id_seq'),?,?,?)";
			int FK = Integer.parseInt(s.getType_id());
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, FK);
			stmt.setString(2, s.getName());
			stmt.setString(3, s.getDescription());
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
            
            String createStatementString = "SELECT id FROM subject LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM subject WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean deleteFilterRow(int x, Subject filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM subject "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND type_id " + filter.getType_id()
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM subject WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean editRow(int x, Subject s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM subject LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "UPDATE subject SET type_id = ?, name = ? WHERE id = " + id;
            int idType = Integer.parseInt(s.getType_id());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idType);
			stmt.setString(2, s.getName());

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
	
	public boolean editFilterRow(int x, Subject s, Subject filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM subject "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND type_id " + filter.getType_id()
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next())
				 id = rs.getInt("id");
            createStatementString = "UPDATE subject SET type_id = ?, name = ? WHERE id = " + id;
            int idType = Integer.parseInt(s.getType_id());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idType);
			stmt.setString(2, s.getName());

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
	
	public Integer getId(int x, Subject filter) throws SQLException {
		Connection conn = null;
        int id = 0;
        if(x < 0)
        	return -1;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM subject "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND type_id " + filter.getType_id()
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
		int examAmount = 0;
		String descr = "";
		
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
            String createStatementString = "SELECT count(*) AS pocet "
            		+						"FROM subject "
            		+						"JOIN exam "
            		+						"ON subject.id = exam.subject_id "
            		+						"GROUP BY subject.id "
            		+						"HAVING subject.id = " + id;
            Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			if(rs.next()) { 
				examAmount = rs.getInt("pocet");
			}
			createStatementString = "SELECT description "
					+				"FROM subject "
					+				"WHERE subject.id = " + id;
			st = conn2.createStatement();
			rs = st.executeQuery(createStatementString);
			if(rs.next()) 
				descr = rs.getString("description");
				
            conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		return "Exam amount: 	" + examAmount 
			 + "\nDescription: 	" + descr;
	}
}