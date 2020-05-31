package managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;
import tableModel.School;

@SuppressWarnings("deprecation")
public class ManagerSchool extends AllTablesManager {
	
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
	
	public ManagerSchool(PGPoolingDataSource source){
		super(source);
	}
	
	protected School processRow(ResultSet rs) throws SQLException{
		return(new School(rs.getString("id"), rs.getString("name"), rs.getString("address"), rs.getString("phone")));
	}
	
	@SuppressWarnings("unchecked")
	public List<School> getAllSchools() throws SQLException {
		return(selectQuery("SELECT * FROM school LIMIT 25 OFFSET " + inkrement));
	}
	
	@SuppressWarnings("unchecked")
	public List<School> getAllSchools(School s) throws SQLException {
		return(selectQuery("SELECT * FROM school "
				+			"WHERE name LIKE '%" + s.getName() + "%' AND address LIKE '%" + s.getAddress() + "%' AND phone LIKE '%" + s.getPhone() + "%'"
				+			"LIMIT 25 OFFSET " + filterInkrement));
	}

	public int getClassAmount(Object data) throws SQLException {
		Connection conn2 = null;
		int pocet = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
			String createStatementString = "SELECT count(classroom.school_id) AS poc "
				+							"FROM school "
				+							"JOIN classroom "
				+ 							"ON school.id = classroom.school_id "
				+							"GROUP BY school.id "
				+							"HAVING school.id = " + data;
		
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
	
	public boolean insertSchool(School s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);

			String createStatementString = "INSERT INTO school(id, name, address, phone) VALUES(nextval('school_id_seq'),?,?,?)";
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setString(1, s.getName());
			stmt.setString(2, s.getAddress());
			stmt.setString(3, s.getPhone());
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
            String createStatementString = "SELECT id FROM school LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM school WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean deleteFilterRow(int x, School filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM school "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND address LIKE '%" + filter.getAddress() + "%' AND phone LIKE '%" + filter.getPhone() + "%' "
            		+						"LIMIT 1 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM school WHERE id = " + id;
  

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean editRow(int x, School s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM school LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "UPDATE school SET name = ?, address = ?, phone = ? WHERE id = " + id;
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setString(1, s.getName());
			stmt.setString(2, s.getAddress());
			stmt.setString(3, s.getPhone());

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

	public boolean editFilterRow(int x, School s, School filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM school "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND address LIKE '%" + filter.getAddress() 
            		+ 						"%' AND phone LIKE '%" + filter.getPhone() + "%' "
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next())
				 id = rs.getInt("id");
            createStatementString = "UPDATE school SET name = ?, address = ?, phone = ? WHERE id = " + id;
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.setString(1, s.getName());
			stmt.setString(2, s.getAddress());
			stmt.setString(3, s.getPhone());
			
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
	
	public Integer getId(int x, School filter) throws SQLException {
		Connection conn = null;
        int id = 0;
        if(x < 0)
        	return -1;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM school "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND address LIKE '%" + filter.getAddress() 
            		+ 						"%' AND phone LIKE '%" + filter.getPhone() + "%' "
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
		int teacherAmount = 0;
		int capacity = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
            String createStatementString = "SELECT count(*) AS pocet "
            		+						"FROM school "
            		+						"JOIN classroom "
            		+						"ON school.id = classroom.school_id "
            		+						"JOIN teacher "
            		+						"ON teacher.classroom_id = classroom.id "
            		+						"GROUP BY school.id "
            		+						"HAVING school.id = " + id;
            Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			if(rs.next()) 
				teacherAmount = rs.getInt("pocet"); 
			
			createStatementString = "SELECT sum(capacity) as capa "
					+				"FROM school "
					+				"JOIN classroom "
					+				"ON school.id = classroom.school_id "
					+				"GROUP BY school.id "
					+				"HAVING school.id = " + id;
			st = conn2.createStatement();
			rs = st.executeQuery(createStatementString);
			if(rs.next()) 
				capacity = rs.getInt("capa");
				
            conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		return "Teachers amount: 				" + teacherAmount 
			 + "\nTotal capacity of classrooms: " + capacity;
	}
}