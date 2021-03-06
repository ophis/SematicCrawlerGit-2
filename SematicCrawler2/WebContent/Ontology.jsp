<%@page import="java.sql.ResultSet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="dal.OntologyDAL"%>
<%! ResultSet getOntology(String type){
		OntologyDAL ontologyDAL = new OntologyDAL();
		ResultSet ontologies = ontologyDAL.getOntologiesByType(type);
		return ontologies;
	}	
%>
<html>
<head>
<script>
	function modify(id){
		var formname = "ontology"+id;
		var form = document.getElementById(formname);
		form.action = "OntologyModify";
		form.submit();
	}
	function deleteOnto(id){
		var formname= "ontology"+id;
		var form = document.getElementById(formname);
		form.action = "OntologyDelete";
		form.submit();
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>语义本体管理</title>
</head>
<body>
<form action="Ontology.jsp" method="post">
	输入语义本体的类别：<input type="text" name="type"/><input name="searchBtn" type="submit" value="查询">
</form>
<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	if(type!=null){
		if(request.getParameter("searchBtn")==null)
			type = new String(request.getParameter("type").getBytes("ISO8859-1"),"utf-8");
		ResultSet ontologies = getOntology(type);
		while(ontologies.next()){
			int id = ontologies.getInt("id");
			String word = ontologies.getString("word");
			int rights = ontologies.getInt("rights");
			out.println("<form name='form1' id='ontology"+id+"' method='post'>"+
			"<input type='text' name='id' value='"+id+"' style='display:none;'/>"+
			"<input type='text' name='type' value='"+type+"' style='display:none;'/>"+
			"关键词：<label name='keyword' style='display:inline-block;width:100px;'>"+word+"</label>"+
			"权值：<input type='text' name='rights' value='"+rights+"'/>"+
			"<input type='submit' value='修改' onclick='modify(\""+id+"\")'/>"+
			"<input type='submit' value='删除' onclick='deleteOnto(\""+id+"\")'/><br>"+
			"</form>");
		}
		ontologies.close();
	}
%>
</body>
</html>