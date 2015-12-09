<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.djw.tools.restlet.*" %>
<%@ page import="openfda.classes.ServerAuth" %>
<%

int StatusCode = 0;
String Message = "";
String Version = "";
String Build = "";
String ToolsVersion = "";
try {
    String ServerKey = "";
    ServerAuth serverAuth = new ServerAuth();
    ServerKey = serverAuth.getKey();

    String JsonURL = "";

	String ServiceURI = "/djwTools";

	RestClient restClient = new RestClient();
	JSONObject jResponse = restClient.getService(ServiceURI);
	StatusCode = jResponse.getInt("StatusCode");
	if (StatusCode == 0){
 		JSONObject jBody = jResponse.getJSONObject("Body");
 		Version = jBody.getString("Version");
 		Build = jBody.getString("Build");
 		JSONObject djwToolsInfo = jBody.getJSONObject("djwToolsInfo");
 		ToolsVersion = djwToolsInfo.getString("djwToolsVersion");
	} else {
		Message = jResponse.getString("Message");
	}
} catch (Exception e) {
	Message = "An error has occured: " + e;
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
	                <h3>Application Info</h3>
	               </div>
	                <div><%=Message%>
	                <dl class="dl-horizontal">
	                	<dt>Version</dt>
					  	<dd><%=Version %></dd>
					  	<dt>Tools Version</dt>
					  	<dd><%=ToolsVersion %></dd>
					  	<dt>Build</dt>
					  	<dd><%=Build %></dd>
					</dl>
	                </div>
	             </div>
	        </div>
		</div>
		<jsp:include page="inc/scriptrefs.jsp" />
	</body>
</html>