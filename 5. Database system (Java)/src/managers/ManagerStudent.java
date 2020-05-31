package managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;
import tableModel.Student;

@SuppressWarnings("deprecation")
public class ManagerStudent extends AllTablesManager {
	
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
	
	public ManagerStudent(PGPoolingDataSource source){
		super(source);
	}
	
	protected Student processRow(ResultSet rs) throws SQLException{
		return(new Student(rs.getString("id"), rs.getString("name"), rs.getString("surname"), rs.getString("email")));
	}
	
	@SuppressWarnings("unchecked")
	public List<Student> getAllStudents() throws SQLException {
		return(selectQuery("SELECT * FROM student LIMIT 25 OFFSET " + inkrement));
	}
	/*
	SELECT * FROM student
	WHERE name LIKE '%Pet%'
	LIMIT 25 OFFSET 0
	 */
	
	@SuppressWarnings("unchecked")
	public List<Student> getAllStudents(Student s) throws SQLException {
		return(selectQuery("SELECT * FROM student "
				+			"WHERE name LIKE '%" + s.getName() + "%' AND surname LIKE '%" + s.getSurname() + "%' AND email LIKE '%" + s.getEmail() + "%' "
				+			"LIMIT 25 OFFSET " + filterInkrement));
	}
	
	public boolean insertStudent(Student s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);

			String createStatementString = "INSERT INTO student(id, name, surname, email) VALUES(nextval('student_id_seq'),?,?,?)";
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setString(1, s.getName());
			stmt.setString(2, s.getSurname());
			stmt.setString(3, s.getEmail());
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
	
	public String getVsp(Object data) throws SQLException {
		Connection conn2 = null;
		double avg = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			String createStatementString = "SELECT * "
					+ 					   "FROM (Select id AS ide FROM Student Limit 25 OFFSET " + inkrement 
					+					   ") AS der WHERE der.ide = " + data;
			Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			@SuppressWarnings("unused")
			int id = 0;
			if(rs.next())
				id = rs.getInt("ide");
			createStatementString = "SELECT avg(studies.grade) as aveg "
					+ 				"FROM Student "
					+ 				"JOIN studies ON Student.id=studies.student_id "
					+ 				"GROUP BY Student.id "
					+ 				"HAVING Student.id = " + data;
			st = conn2.createStatement();
			rs = st.executeQuery(createStatementString);
			if(rs.next())
				avg = rs.getFloat("aveg");
            conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		String avgStr = String.valueOf(avg);
		return avgStr;
	}
	
	
	
	public boolean deleteRow(int x) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM Student LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM Student WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean deleteFilterRow(int x, Student filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM Student "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND surname LIKE '%" + filter.getSurname() + "%' AND email LIKE '%" + filter.getEmail() + "%' "
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
			System.out.println(id);
            createStatementString = "DELETE FROM Student WHERE id = " + id;
            
            
            

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean editRow(int x, Student s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM student LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "UPDATE student SET name = ?, surname = ?, email = ? WHERE id = " + id;
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setString(1, s.getName());
			stmt.setString(2, s.getSurname());
			stmt.setString(3, s.getEmail());

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
	
	public boolean editFilterRow(int x, Student s, Student filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM Student "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND surname LIKE '%" 
            		+ 						filter.getSurname() + "%' AND email LIKE '%" + filter.getEmail() + "%' "
            		+						"LIMIT 25 OFFSET " + (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next())
				 id = rs.getInt("id");

            createStatementString = "UPDATE student SET name = ?, surname = ?, email = ? WHERE id = " + id;
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setString(1, s.getName());
			stmt.setString(2, s.getSurname());
			stmt.setString(3, s.getEmail());

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
	
	public Integer getId(int x, Student filter) throws SQLException {
		Connection conn = null;
        int id = 0;
        if(x < 0)
        	return -1;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM student "
            		+						"WHERE name LIKE '%" + filter.getName() + "%' AND surname LIKE '%" 
            		+ 						filter.getSurname() + "%' AND email LIKE '%" + filter.getEmail() + "%' "
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
		int studyAmount = 0;
		String endDate = "";
		int examAmount = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
            String createStatementString = "SELECT count(*) AS studyingAmount "
            		+						"FROM student "
            		+						"JOIN studies "
            		+						"ON student.id = studies.student_id "
            		+						"GROUP BY student.id "
            		+						"HAVING student.id = " + id;
            Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			if(rs.next()) { 
				studyAmount = rs.getInt("studyingAmount"); 
			}
			createStatementString = "SELECT max(dateTo) AS datum "
					+				"FROM studies "
					+				"GROUP BY studies.student_id "
					+				"HAVING studies.student_id = " + id;
			st = conn2.createStatement();
			rs = st.executeQuery(createStatementString);
			if(rs.next()) 
				endDate = rs.getString("datum");

			createStatementString = "SELECT count(*) AS pocet "
					+				"FROM student "
					+				"JOIN studies " 
					+				"ON student.id = studies.student_id "
					+				"JOIN subject "
					+				"ON studies.subject_id = subject.id "
					+				"JOIN exam "
					+				"ON subject.id = exam.subject_id "
					+				"GROUP BY studies.student_id "
					+				"HAVING studies.student_id = " + id;
			st = conn2.createStatement();
			rs = st.executeQuery(createStatementString);
			if(rs.next())
				examAmount = rs.getInt("pocet");
				
            conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		conn2.close();
		return "Amount of studied subjects: 		" + studyAmount 
			 + "\nDate of last completed subject: 	" + endDate 
			 + "\nAmount of total written exams: 	" + examAmount;
	}
}