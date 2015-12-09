<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.djw.tools.restlet.*" %>
<%@ page import="openfda.classes.ServerAuth" %>
<%
String Message = "";
JSONArray jResults = new JSONArray();
String ReactionList = "";
try {
    String ServerKey = "";
    ServerAuth serverAuth = new ServerAuth();
    ServerKey = serverAuth.getKey();

	int StatusCode = 0;
    String JsonURL = "";
	String ServiceURI = "/fda/" + ServerKey + "/lookup/reactions";

	RestClient restClient = new RestClient();
	JSONObject jResponse = restClient.getService(ServiceURI);
	JSONObject jBody = jResponse.getJSONObject("Body");
	jResults = jBody.getJSONArray("results");
	ReactionList = "<option value='0'>Select Reaction</option>";
	for (int i=0; i<jResults.length(); i++){
		ReactionList += "<option value='" + jResults.getString(i) + "'>" + jResults.getString(i) + "</option>";
	}
	
} catch (Exception e) {
	out.println("An error has occured: " + e);
}

%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<jsp:include page="inc/head.jsp" />
	</head>
	<body>
		<jsp:include page="inc/header.jsp" />
		<div id="wrapper">
			<jsp:include page="inc/sidebar.jsp" />
			        <div id="main-wrapper" class="col-md-11 pull-right">
	            <div id="main">
	              <div class="page-header">
	                <h3>Reaction Search</h3>
	                </div>
	                <div>
	                	<h4>Search for drugs involved with adverse reactions that have the following reactions:</h4>
	                </div>
					<div>
							<form  class="form-horizontal" role="form" name="subForm" method="post" action="reactionresults.jsp">
								<div class="form-group">
								  <label class="control-label col-sm-2" for="email">Enter a reaction:</label>
								  <div class="col-sm-10" style="width:40%">
								    <select class="form-control" name="reaction1">
									<%= ReactionList%>
									</select>
								  </div>
								</div>							
								<div class="form-group">
								  <label class="control-label col-sm-2" for="email">Enter a reaction:</label>
								  <div class="col-sm-10" style="width:40%">
								    <select class="form-control" name="reaction2">
									<%= ReactionList%>
									</select>
								  </div>
								</div>							
								<input type="submit">
							</form>
					</div>
	             </div>
	        </div>
		</div>
		<jsp:include page="inc/scriptrefs.jsp" />
	</body>
</html>