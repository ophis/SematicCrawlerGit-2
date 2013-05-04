package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceInfoDAL extends AbstractDAL{
	private void insert2ServiceInfo (String name, String type, String desc, String url) throws SQLException{
		PreparedStatement insertStatement = conn.prepareStatement(
				"INSERT INTO ServiceInfo(name,type,description,url) " +
				"VALUES(?,?,?,?)");
		insertStatement.setString(1, name);
		insertStatement.setString(2, type);
		insertStatement.setString(3, desc);
		insertStatement.setString(4, url);
		insertStatement.execute();
	}
	
	public ResultSet selectServiceInfoByUrl(String url) throws SQLException{
		PreparedStatement selectStatement = conn.prepareStatement(
				"SELECT * FROM ServiceInfo WHERE url=?");
		selectStatement.setString(1, url);
		return selectStatement.executeQuery();
	}
	
	public void updateServiceInfoByUrl(String name, String type, String desc, String url) throws SQLException{
		PreparedStatement updateStatement = conn.prepareStatement(
				"UPDATE ServiceInfo SET name=?,type=?,description=? WHERE url=?");
		updateStatement.setString(1, name);
		updateStatement.setString(2, type);
		updateStatement.setString(3, desc);
		updateStatement.setString(4, url);
		updateStatement.executeUpdate();
	}
	
	public void deleteFromServiceInfoByUrl(String url) throws SQLException{
		PreparedStatement deleteStatement = conn.prepareStatement(
				"DELETE FROM ServiceInfo WHERE url=?");
		deleteStatement.setString(1, url);
		deleteStatement.execute();
	}
	
	public void add2ServiceInfo(String name, String type, String desc, String url){
		try {
			//update if existed
			ResultSet resultSet = selectServiceInfoByUrl(url);
			if(resultSet.next())
			{
				updateServiceInfoByUrl(name, type, desc, url);
			}
			//insert if not exist
			else{
				insert2ServiceInfo(name, type, desc, url);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]){
		ServiceInfoDAL serviceInfoDAL = new ServiceInfoDAL();
		serviceInfoDAL.add2ServiceInfo("name", "type", "desc3", "url");
		try {
			serviceInfoDAL.deleteFromServiceInfoByUrl("url");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
