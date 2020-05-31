package managers;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;

import tableModel.Classroom;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@SuppressWarnings("deprecation")
public class ManagerClassroom extends AllTablesManager{
	
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
	
	public ManagerClassroom(PGPoolingDataSource source){
		super(source);
	}
	
	protected Classroom processRow(ResultSet rs) throws SQLException{
		return(new Classroom(rs.getString("id"), rs.getString("school_id"), rs.getString("capacity"), rs.getString("name")));
	}
	
	@SuppressWarnings("unchecked")
	public List<Classroom> getAllClassrooms() throws SQLException {
		return(selectQuery("SELECT * FROM classroom LIMIT 25 OFFSET " + inkrement));
	}
	
	@SuppressWarnings("unchecked")
	public List<Classroom> getAllClassrooms(Classroom c) throws SQLException {
		return(selectQuery("SELECT * FROM classroom "
				+			"WHERE capacity " + c.getCapacity() + " AND name LIKE '%" + c.getName() + "%' AND school_id " + c.getSchoolId()
				+			"LIMIT 25 OFFSET " + filterInkrement));
	}
	
	public int getTeachersAmount(Object data) throws SQLException {
		Connection conn2 = null;
		int pocet = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
			String createStatementString = "SELECT count(teacher.id) AS poc "
				+							"FROM classroom "
				+							"JOIN teacher "
				+ 							"ON teacher.classroom_id = classroom.id "
				+							"GROUP BY classroom.id "
				+							"HAVING classroom.id = " + data;
			
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
	
	public boolean insertClassroom(Classroom s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);

			String createStatementString = "INSERT INTO classroom(id, school_id, capacity, name) VALUES(nextval('classroom_id_seq'),?,?,?)";
			int kap = Integer.parseInt(s.getCapacity());
			int FK = Integer.parseInt(s.getSchoolId());
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, FK);
			stmt.setInt(2, kap);
			stmt.setString(3, s.getName());
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
            String createStatementString = "SELECT id FROM classroom LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM classroom WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean deleteFilterRow(int x, Classroom filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM classroom "
            		+						"WHERE capacity " + filter.getCapacity() + " AND name LIKE '%" + filter.getName() + "%' AND school_id " + filter.getSchoolId()
            		+						" LIMIT 1 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM classroom WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean editRow(int x, Classroom s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM classroom LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "UPDATE classroom SET school_id = ?, capacity = ?, name = ? WHERE id = " + id;
            int idSchool = Integer.parseInt(s.getSchoolId());
            int capacity = Integer.parseInt(s.getCapacity());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idSchool);
			stmt.setInt(2, capacity);
			stmt.setString(3, s.getName());

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

	public boolean editFilterRow(int x, Classroom s, Classroom filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM classroom "
            		+						"WHERE capacity " + filter.getCapacity() + " AND name LIKE '%" 
            		+					 	filter.getName() + "%' AND school_id " + filter.getSchoolId()
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next())
				 id = rs.getInt("id");
			createStatementString = "UPDATE classroom SET school_id = ?, capacity = ?, name = ? WHERE id = " + id;
            int idSchool = Integer.parseInt(s.getSchoolId());
            int capacity = Integer.parseInt(s.getCapacity());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setInt(1, idSchool);
			stmt.setInt(2, capacity);
			stmt.setString(3, s.getName());

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
	
	public Integer getId(int x, Classroom filter) throws SQLException {
		Connection conn = null;
        int id = 0;
        if(x < 0)
        	return -1;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM classroom "
            		+						"WHERE capacity " + filter.getCapacity() + " AND name LIKE '%" 
            		+					 	filter.getName() + "%' AND school_id " + filter.getSchoolId()
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
		String schoolName = "";
		String startDate = "";
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
            String createStatementString = "SELECT school.name as schoolName "
            		+						"FROM classroom "
            		+						"JOIN school "
            		+						"ON school.id = classroom.school_id "
            		+						"WHERE classroom.id = " + id;
            Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			if(rs.next()) { 
				schoolName = rs.getString("schoolName");
			}
			createStatementString = "SELECT min(dateFrom) AS datum "
					+				"FROM classroom "
					+				"JOIN teacher "
					+				"ON teacher.classroom_id = classroom.id "
					+				"JOIN teaches "
					+				"ON teacher.id = teaches.teacher_id "
					+				"WHERE classroom.id = " + id;
			st = conn2.createStatement();
			rs = st.executeQuery(createStatementString);
			if(rs.next()) 
				startDate = rs.getString("datum");
				
            conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		return "School name: 			" + schoolName 
			 + "\nDate of study launch: " + startDate;
	}
}