<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="openfda.classes.*" %>

<%
	
	//String ServiceURI = "/event.json?count=patient.drug.medicinalproduct.exact&limit=100";
 	String ServiceURI = "/event.json?count=patient.reaction.reactionmeddrapt.exact";
 	
 	if (request.getParameter("serviceURI") != null) ServiceURI = request.getParameter("serviceURI");
	
	OpenFDAClient restClient = new OpenFDAClient();
	JSONObject json = restClient.getService(ServiceURI);

	JSONArray results = json.getJSONArray("results");
	
	ArrayList<String> ReactionList = new ArrayList<String>();
	for (int i=0; i<results.length(); i++){
		JSONObject result = results.getJSONObject(i);
		String reactionName = result.getString("term");
		int idx = ReactionList.indexOf(reactionName);
		if (idx == -1){
			ReactionList.add(reactionName);
		}
	}
	
	String SQL = "insert into reactions(reaction) values <br>";
	
	for (int i=0; i<ReactionList.size(); i++){
		SQL += "(\"" + ReactionList.get(i) + "\")";
		if (i < ReactionList.size() -1){
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
		<form name="formSubmit" method="post" action="reactiontable.jsp">
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