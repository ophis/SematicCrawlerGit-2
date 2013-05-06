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
	public void updateById(int id, String name, String type, String desc, String url) {
		PreparedStatement updateStatement;
		try {
			updateStatement = conn.prepareStatement(
					"UPDATE ServiceInfo SET name=?,type=?,description=?,url=? WHERE id=?");
			updateStatement.setString(1, name);
			updateStatement.setString(2, type);
			updateStatement.setString(3, desc);
			updateStatement.setString(4, url);
			updateStatement.setInt(5, id);
			updateStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteById(int id){
		try {
			PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM ServiceInfo WHERE id=?");
			deleteStatement.setInt(1, id);
			deleteStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteFromServiceInfoByUrl(String url) throws SQLException{
		PreparedStatement deleteStatement = conn.prepareStatement(
				"DELETE FROM ServiceInfo WHERE url=?");
		deleteStatement.setString(1, url);
		deleteStatement.execute();
	}
	
	public String getTypeById(int id){
		try {
			PreparedStatement selectStatement = conn.prepareStatement("SELECT type FROM ServiceInfo WHERE id=?");
			selectStatement.setInt(1, id);
			ResultSet result = selectStatement.executeQuery();
			if(result.next()){
				String type = result.getString("type");
				return type;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public ResultSet getServiceInfoBytype(String type){
		try {
			PreparedStatement selectStatement = conn.prepareStatement("SELECT * FROM ServiceInfo WHERE type=?");
			selectStatement.setString(1, type);
			return selectStatement.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
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
