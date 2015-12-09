<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="openfda.classes.*" %>

<%
	
	String ServiceURI = "/label.json?count=openfda.substance_name.exact&limit=100";
 	
 	if (request.getParameter("serviceURI") != null) ServiceURI = request.getParameter("serviceURI");
	
	OpenFDAClient restClient = new OpenFDAClient();
	JSONObject json = restClient.getService(ServiceURI);

	JSONArray results = json.getJSONArray("results");
	
	ArrayList<String> DrugList = new ArrayList<String>();
	for (int i=0; i<results.length(); i++){
		JSONObject result = results.getJSONObject(i);
		String drugName = result.getString("term");
		int idx = DrugList.indexOf(drugName);
		if (idx == -1){
			DrugList.add(drugName);
		}
	}
	
	String SQL = "insert into drugs(drugname) values <br>";
	
	for (int i=0; i<DrugList.size(); i++){
		SQL += "(\"" + DrugList.get(i) + "\")";
		if (i < DrugList.size() -1){
			SQL += ",";
		}
		SQL += "<br>";
	}
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
	<div>
		<form name="formSubmit" method="post" action="drugtable.jsp">
			<p>
				URL for reactions: <input type="text" name="serviceURI" value="<%=ServiceURI%>" size="120"><br>
				<input type="submit">
			</p>
		</form>
	</div>
	<div>
		<%=SQL%>
	</div>
</body>
</html>