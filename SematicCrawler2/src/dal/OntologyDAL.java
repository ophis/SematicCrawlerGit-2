package dal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Set;


public class OntologyDAL extends AbstractDAL {
	private int insert2Ontology(String type, String word, Integer rights){
		try {
			PreparedStatement insertStatement = conn.prepareStatement("INSERT INTO Ontology(type,word,rights) VALUES(?,?,?)");
			insertStatement.setString(1, type);
			insertStatement.setString(2, word);
			insertStatement.setInt(3, rights);
			insertStatement.execute();
			insertStatement.close();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}	
	}
	
	public int updateOntologyRight(int id, Integer rights){
		try {
			PreparedStatement updateStatement = conn.prepareStatement("UPDATE Ontology SET rights=? WHERE id=?");
			updateStatement.setInt(1, rights);
			updateStatement.setInt(2, id);
			updateStatement.execute();
			updateStatement.close();
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		
	}
	
	public void add2Ontology(String type, String word, Integer rights){
		try {
//			String insert = "INSERT INTO Ontology(type,word,rights) VALUES()";
			PreparedStatement selectStatement = conn.prepareStatement("SELECT * FROM Ontology WHERE type=? AND word=?");
			selectStatement.setString(1, type);
			selectStatement.setString(2, word);
			ResultSet result = selectStatement.executeQuery();
			if(result.next()){
				int id = result.getInt("id");
				int originalRights = result.getInt("rights");
				Integer newRights = originalRights+rights;
				updateOntologyRight(id,newRights);
			}
			else{
				insert2Ontology(type,word,rights);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addAll2Ontology(String type, HashMap<String, Integer> keyWords){
		try {
			//update existed words
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT word FROM Ontology WHERE word IN(");
			for(int i=0; i<keyWords.size();i++){
				if(i == keyWords.size()-1){
					sql.append("?)");
				}
				else {
					sql.append("?,");
				}
			}
			PreparedStatement selectStatement = conn.prepareStatement(sql.toString());
			for(int i=0;i<keyWords.size();i++){
				selectStatement.setString(i+1, (String)keyWords.keySet().toArray()[i]);
			}
			ResultSet results = selectStatement.executeQuery();
			while(results.next()){
				String word = results.getString("word");
				int rights = keyWords.get(word);
				add2Ontology(type, word, rights);
				keyWords.remove(word);
			}
			selectStatement.close();
			
			//insert new words
			for(String word : keyWords.keySet()){
				insert2Ontology(type, word, keyWords.get(word));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteById(int id){
		try {
			PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM Ontology WHERE id=?");
			deleteStatement.setInt(1, id);
			deleteStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet getOntologiesByType(String type){
		try {
			PreparedStatement selectStatement = conn.prepareStatement("SELECT id,word, rights FROM Ontology WHERE type=?");
			selectStatement.setString(1, type);
			ResultSet resultSet = selectStatement.executeQuery();
			//if -> while
			return resultSet;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public HashMap<String, Integer> getRights(String type, Set<String> words){
		HashMap<String,Integer> wordRights = new HashMap<String,Integer>();
		for(String word : words){
			wordRights.put(word, 0);
		}
		if(words.size()==0) return wordRights;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT word,rights FROM Ontology WHERE type=? AND word IN(");
		for(int i=0; i<words.size();i++){
			if(i == words.size()-1){
				sql.append("?)");
			}
			else {
				sql.append("?,");
			}
		}
		try {
			PreparedStatement selectStatement = conn.prepareStatement(sql.toString());
			selectStatement.setString(1, type);
			for(int i=0;i<words.size();i++){
				selectStatement.setString(i+2, (String)words.toArray()[i]);
			}
			ResultSet results = selectStatement.executeQuery();
			while(results.next()){
				String keyWord = results.getString("word"); 
				int rights = results.getInt("rights");
				wordRights.put(keyWord,rights);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}			
		return wordRights;
	}
	
//	public String join(String join, Object[] strArray){
//		StringBuffer sb = new StringBuffer();
//		for(int i=0; i<strArray.length;i++){
//			if(i==(strArray.length-1)){
//				sb.append("'"+(String)strArray[i]+"'");
//			}
//			else {
//				sb.append("'"+(String)strArray[i]+"'").append(join);
//			}
//		}
//		return new String(sb);
//	}
	
	//test
	public static void main(String args[]){
		OntologyDAL ontologyDAL = new OntologyDAL();
		HashMap<String, Integer> keywords = new HashMap<String, Integer>();
		keywords.put("京东", 1);
		keywords.put("淘宝", 2);
		keywords.put("天猫", 3);
		ontologyDAL.addAll2Ontology("电子商务", keywords);
		ontologyDAL.add2Ontology("电子商务", "商城", 2);
		ontologyDAL.add2Ontology("电子商务", "商城", 4);
		ontologyDAL.closeConnection();
	}
}
