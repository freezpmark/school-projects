package managers;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;

import tableModel.Teacher;

import java.text.ParseException;
import java.util.Date;

@SuppressWarnings("deprecation")
public class ManagerTeacher extends AllTablesManager {
	
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
	
	public ManagerTeacher(PGPoolingDataSource source){
		super(source);
	}
	
	protected Teacher processRow(ResultSet rs) throws SQLException{
		return(new Teacher(rs.getString("id"), rs.getString("classroom_id"), rs.getString("name"), 
				rs.getString("surname"), rs.getString("birthDate"), rs.getString("email"), rs.getString("salary")));
	}
	
	@SuppressWarnings("unchecked")
	public List<Teacher> getAllTeachers() throws SQLException {
		return(selectQuery("SELECT * FROM teacher LIMIT 25 OFFSET " + inkrement));
	}
	
	@SuppressWarnings("unchecked")
	public List<Teacher> getAllTeachers(Teacher t) throws SQLException {	
		return(selectQuery("SELECT * FROM teacher "
				+			"WHERE name LIKE '%" + t.getName() 		+ "%' AND surname LIKE '%" 	+ t.getSurname() 
				+ 			"%' AND birthdate " + t.getBirthDate() 	+ " AND email LIKE '%" 		+ t.getEmail() 
				+ 			"%' AND salary " 	+ t.getSalary() 	+ " AND classroom_id " 		+ t.getClassId()
				+			" LIMIT 25 OFFSET " + filterInkrement));
	}
	
	public String getDuration(Object data) throws SQLException {
		Connection conn2 = null;
		int yearCounter = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			String createStatementString = "SELECT max(current_date - dateFrom)/365 AS yearAmount "
					+						"FROM teaches "
					+						"GROUP BY teaches.teacher_id "
					+						"HAVING teaches.teacher_id = " + data;
			
			Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);

			if(rs.next())
				yearCounter = rs.getInt("yearAmount");

            conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		String yrStr = String.valueOf(yearCounter);
		return yrStr;
	}
	
	public boolean insertTeacher(Teacher s) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = super.source.getConnection();
			conn.setAutoCommit(false);
			String createStatementString = "INSERT INTO teacher(id, classroom_id, name, surname, birthdate, email, salary) " 
					+						"VALUES(nextval('teacher_id_seq'),?,?,?,?,?,?)";
			stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			int salara = Integer.parseInt(s.getSalary());
			int FK = Integer.parseInt(s.getClassId());
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getBirthDate());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
			
			stmt.setInt(1, FK);
			stmt.setString(2, s.getName());
			stmt.setString(3, s.getSurname());
			stmt.setDate(4, sql);
			stmt.setString(5, s.getEmail());
			stmt.setInt(6, salara);
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
            String createStatementString = "SELECT id FROM teacher LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM teacher WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean deleteFilterRow(int x, Teacher filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM teacher "
            		+						"WHERE name LIKE '%"	+ filter.getName() 		+ "%' AND surname LIKE '%" 	+ filter.getSurname() 
    				+ 						"%' AND birthdate " 	+ filter.getBirthDate() + " AND email LIKE '%" 		+ filter.getEmail() 
    				+ 						"%' AND salary " 		+ filter.getSalary() 	+ " AND classroom_id " 		+ filter.getClassId()
            		+						"LIMIT 1 OFFSET " 		+ (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "DELETE FROM teacher WHERE id = " + id;

            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.close();
        return true;
    }
	
	public boolean editRow(int x, Teacher s) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM teacher LIMIT 1 OFFSET " + (x+inkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next()) { 
				 id = rs.getInt("id"); 
			}
            createStatementString = "UPDATE teacher SET name = ?, surname = ?, email = ?, "
            		+				"birthdate = ?, salary = ?, classroom_id = ? WHERE id = " + id;
            int idClass = Integer.parseInt(s.getClassId());
            int salary  = Integer.parseInt(s.getSalary());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getBirthDate());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setString(1, s.getName());
			stmt.setString(2, s.getSurname());
			stmt.setString(3, s.getEmail());
			stmt.setDate(4, sql);
			stmt.setInt(5, salary);
			stmt.setInt(6, idClass);

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

	public boolean editFilterRow(int x, Teacher s, Teacher filter) throws SQLException {
		Connection conn = null;
        PreparedStatement stmt = null;
        if(x < 0)
        	return false;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
      
            String createStatementString = "SELECT id FROM teacher "
            		+						"WHERE name LIKE '%" 	+ filter.getName() 		+ "%' AND surname LIKE '%" 	+ filter.getSurname() 
            		+ 						"%' AND birthdate " 	+ filter.getBirthDate() + " AND email LIKE '%" 		+ filter.getEmail() 
            		+ 						"%' AND salary " 		+ filter.getSalary() 	+ " AND classroom_id " 		+ filter.getClassId()
            		+						"LIMIT 25 OFFSET " 		+ (x+filterInkrement);
            Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			int id = 0;
			if(rs.next())
				 id = rs.getInt("id");
			createStatementString = "UPDATE teacher SET name = ?, surname = ?, email = ?, "
					+				"birthdate = ?, salary = ?, classroom_id = ? WHERE id = " + id;
            int idClass = Integer.parseInt(s.getClassId());
            int salary  = Integer.parseInt(s.getSalary());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date parsed = format.parse(s.getBirthDate());
			java.sql.Date sql = new java.sql.Date(parsed.getTime());
            stmt = (PreparedStatement) conn.prepareStatement(createStatementString);
			stmt.setString(1, s.getName());
			stmt.setString(2, s.getSurname());
			stmt.setString(3, s.getEmail());
			stmt.setDate(4, sql);
			stmt.setInt(5, salary);
			stmt.setInt(6, idClass);

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
	
	public Integer getId(int x, Teacher filter) throws SQLException {
		Connection conn = null;
        int id = 0;
        if(x < 0)
        	return -1;

        try {
            conn = super.source.getConnection();
            conn.setAutoCommit(false);
            String createStatementString = "SELECT id FROM teacher "
            		+						"WHERE name LIKE '%"	+ filter.getName() 		+ "%' AND surname LIKE '%" 	+ filter.getSurname() 
    				+ 						"%' AND birthdate " 	+ filter.getBirthDate() + " AND email LIKE '%" 		+ filter.getEmail() 
    				+ 						"%' AND salary " 		+ filter.getSalary() 	+ " AND classroom_id " 		+ filter.getClassId()
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
		String className = "";
		String schoolName = "";
		int salary = 0;
		int teachingAmount = 0;
		int salaryCompare = 0;
		try {
			conn2 = super.source.getConnection();
			conn2.setAutoCommit(false);
			
            String createStatementString = "SELECT classroom.name AS className, school.name AS schoolName, teacher.salary AS salary "
            		+						"FROM teacher "
            		+						"JOIN classroom "
            		+						"ON teacher.classroom_id = classroom.id "
            		+						"JOIN school "
            		+						"ON classroom.school_id = school.id "
            		+						"WHERE teacher.id = " + id;
            
            Statement st = conn2.createStatement();
			ResultSet rs = st.executeQuery(createStatementString);
			if(rs.next()) { 
				className = rs.getString("className"); 
				schoolName = rs.getString("schoolName");
				salary = rs.getInt("salary");
			}
			createStatementString = "SELECT count(*) AS teachingAmount "
					+				"FROM teacher "
					+				"JOIN teaches "
					+				"ON teacher.id = teaches.teacher_id "
					+				"GROUP BY teacher.id "
					+				"HAVING teacher.id = " + id;
			st = conn2.createStatement();
			rs = st.executeQuery(createStatementString);
			if(rs.next()) 
				teachingAmount = rs.getInt("teachingAmount");

			createStatementString = "SELECT " + salary + " - avg(teacher.salary) AS aveg "
					+				"FROM teacher";
			st = conn2.createStatement();
			rs = st.executeQuery(createStatementString);
			if(rs.next())
				salaryCompare = rs.getInt("aveg");
				
            conn2.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		conn2.close();
		return "Classroom name: 						" + className 
			 + "\nSchool name: 							" + schoolName 
			 + "\nAmount of teaching subjects: 			" + teachingAmount
			 + "\nSalary compared to average amount: 	" + salaryCompare;
	}
}