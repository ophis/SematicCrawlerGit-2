<%@page import="dal.ServiceInfoDAL"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet"%>

<%! ResultSet getOntology(String type){
		ServiceInfoDAL serviceInfoDAL = new ServiceInfoDAL();
		ResultSet serviceInfos = serviceInfoDAL.getServiceInfoBytype(type);
		return serviceInfos;
	}	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息服务信息</title>
<script>
	function modify(id){
		var formname = "infoservice"+id;
		var form = document.getElementById(formname);
		form.action = "InfoServiceModify";
		form.submit();
	}
	function deleteOnto(id){
		var formname= "infoservice"+id;
		var form = document.getElementById(formname);
		form.action = "InfoServiceDelete";
		form.submit();
	}
</script>
</head>
<body>
<form action="InfoService.jsp" method="post">
	输入信息服务的类别：<input type="text" name="type"/><input name="searchBtn" type="submit" value="查询">
</form>
<%
	request.setCharacterEncoding("UTF-8");
	String type = request.getParameter("type");
	if(type!=null){
		if(request.getParameter("searchBtn")==null)
			type = new String(request.getParameter("type").getBytes("ISO8859-1"),"utf-8");
		ResultSet serviceInfos = getOntology(type);
		while(serviceInfos.next()){
			int id = serviceInfos.getInt("id");
			String name = serviceInfos.getString("name");
			String desc = serviceInfos.getString("description");
			String url = serviceInfos.getString("url");
			out.println("<form name='form1' id='infoservice"+id+"' method='post'>"+
			"<input type='text' name='id' value='"+id+"' style='display:none;'/>"+
			"名称：<label name='keyword' style='display:inline-block;width:150px;'>"+name+"</label>"+
			"描述：<input type='text' name='rights' value='"+desc+"'/>"+
			"url: <a href='"+url+"'>"+url+"</a>"+
			"<input type='submit' value='修改' onclick='modify(\""+id+"\")'/>"+
			"<input type='submit' value='删除' onclick='deleteOnto(\""+id+"\")'/><br>"+
			"</form>");
		}
		serviceInfos.close();
	}
%>
</body>
</html>