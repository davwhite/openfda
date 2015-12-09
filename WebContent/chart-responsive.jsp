<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.djw.tools.restlet.*" %>
<%@ page import="openfda.classes.ServerAuth" %>
<%

String ThingType = "";
String ThingList = "";
if (request.getParameter("ThingList") !=null) ThingList = request.getParameter("ThingList");
if (request.getParameter("ThingType") !=null) ThingType = request.getParameter("ThingType");
 
 String ServerKey = "";
 ServerAuth serverAuth = new ServerAuth();
 ServerKey = serverAuth.getKey();


String Message = "";
 String sResponse = "";
	try {
		int StatusCode = 0;
	    String JsonURL = "";
		String ServiceURI = "/fda/" + ServerKey + "/chart/" + ThingType + "/" + ThingList;
		RestClient restClient = new RestClient();
		JSONObject jResponse = restClient.getService(ServiceURI);
	 	JSONObject jBody = jResponse.getJSONObject("Body");
	 	JSONArray jRecords = new JSONArray();
		jRecords = jBody.getJSONArray("chartdata");
		sResponse = jRecords.toString();
	} catch (Exception e) {
		out.println("An error has occured: " + e);
	}
%>
  
  
<!DOCTYPE html>
<html lang="en">
	<head>
		<jsp:include page="inc/head.jsp" />
		    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
  		var jsonData = <%=sResponse%>;
        var data = google.visualization.arrayToDataTable(jsonData);
        var options = {
          title: 'Adverse Reactions by Age',
          width: "100%"
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
      
      function resizeHandler () {
          chart.draw(data, options);
      }
      if (window.addEventListener) {
          window.addEventListener('resize', resizeHandler, false);
      }
      else if (window.attachEvent) {
          window.attachEvent('onresize', resizeHandler);
      }
      
      window.onload = resize();
      window.onresize = resize;
    </script>
	</head>
	<body>
		<jsp:include page="inc/header.jsp" />
		<div id="wrapper">
			<jsp:include page="inc/sidebar.jsp" />
			        <div id="main-wrapper" class="col-md-11 pull-right">
	            <div id="main">
	              <div class="page-header">
	                <h3>Adverse Reaction Chart</h3>
	               </div>
					<div>
						<div class="img-responsive" id="chart_div" style="width: 100%; height: 100%;"></div>	
					</div>
	             </div>
	        </div>
		</div>
		<jsp:include page="inc/scriptrefs.jsp" />
	</body>
</html>  
  
