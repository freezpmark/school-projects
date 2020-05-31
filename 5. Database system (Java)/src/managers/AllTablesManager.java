package managers;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import org.postgresql.ds.PGPoolingDataSource;

@SuppressWarnings("deprecation")
public abstract class AllTablesManager {
	protected PGPoolingDataSource source;
	
	public AllTablesManager(PGPoolingDataSource source){
		this.source = source;
	}
	
	@SuppressWarnings({ "finally", "rawtypes" })
	protected List selectQuery(String queryString) throws SQLException{
		List<Object> result = new LinkedList<Object>();
		Statement stmt = null;
		Connection conn = this.source.getConnection();
	    try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			while(rs.next()){
				result.add(processRow(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try { if (stmt != null) stmt.close(); } catch (final Exception e) {};
		    try { if (conn != null) conn.close(); } catch (final Exception e) {};
			return result;
		}
	}
	
	protected abstract Object processRow(ResultSet rs) throws SQLException;
}