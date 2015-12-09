<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.djw.tools.restlet.*" %>
<%@ page import="openfda.classes.ServerAuth" %>
<%

String Drug = "";
String Drug2 = "";
String Drug1a = "";
String Drug2a = "";
String DrugList = "";
String Limit = "";
if (request.getParameter("drug1") !=null) Drug = request.getParameter("drug1");
if (request.getParameter("drug2") !=null) Drug2 = request.getParameter("drug2");
if (request.getParameter("drug1a") !=null) Drug1a = request.getParameter("drug1a");
if (request.getParameter("drug2a") !=null) Drug2a = request.getParameter("drug2a");
if (request.getParameter("limit") !=null) Limit = request.getParameter("limit");

String Message = "";
String Records = "";
String ServerKey = "";
String sResponse = "";
if (!Drug.equals("0") || !Drug2.equals("0")){
	try {
	    
	    ServerAuth serverAuth = new ServerAuth();
	    ServerKey = serverAuth.getKey();

		int StatusCode = 0;
	    String JsonURL = "";
	    if (!Drug.equals("0") && Drug2.equals("0")){
	    	DrugList = Drug;
	    }
	    if (Drug.equals("0") && !Drug2.equals("0")){
	    	DrugList = Drug2;
	    }
	    if (!Drug.equals("0") && !Drug2.equals("0")){
	    	DrugList =  "~" + Drug2;;
	    }
System.out.println("Drug1: " + Drug + "  Drug2: " + Drug2);
	    
/* 	    if (!Drug2.equals("0")) DrugList += "~" + Drug2;
	    if (!Drug1a.equals("")) DrugList += "~" + Drug1a;
	    if (!Drug2a.equals("")) DrugList += "~" + Drug2a;
 */	
		String ServiceURI = "/fda/" + ServerKey + "/search/drug/" + DrugList;
	
		RestClient restClient = new RestClient();
		JSONObject jResponse = restClient.getService(ServiceURI);
//System.out.println(jResponse.toString(1));
	 	JSONObject jBody = jResponse.getJSONObject("Body");
		JSONArray cols = jBody.getJSONObject("ReportOutput").getJSONArray("cols");
		JSONArray rows = jBody.getJSONObject("ReportOutput").getJSONArray("rows");
	
		Records = "<table  class='table table-striped'>";
		Records += "<tr>";
		for (int i=0; i<cols.length(); i++){
			Records += "<th>" + cols.getString(i) + "</th>";
		}
		Records += "</tr>";
		for (int i=0; i<rows.length(); i++){
			Records += "<tr>";
			for (int c=0; c<cols.length(); c++){
				JSONArray row = rows.getJSONArray(i);
				Records += "<td>" + row.getString(c) + "</td>";
			}
			Records += "</tr>";
		}
		Records += "</table>";
		
	} catch (Exception e) {
		out.println("An error has occured: " + e);
	}
	try {
		int StatusCode = 0;
	    String JsonURL = "";
		String ServiceURI = "/fda/" + ServerKey + "/chart/drugs/" + DrugList;
		RestClient restClient = new RestClient();
		JSONObject jResponse = restClient.getService(ServiceURI);
	 	JSONObject jBody = jResponse.getJSONObject("Body");
	 	if (!jBody.isNull("chartdata")){
		 	JSONArray jRecords = new JSONArray();
			jRecords = jBody.getJSONArray("chartdata");
			sResponse = jRecords.toString();
	 	}
	} catch (Exception e) {
		out.println("An error has occured: " + e);
	}

} else {
	Records = "No drug selected";
}

%>

<!DOCTYPE html>
<html lang="en">
	<head>
		<jsp:include page="inc/head.jsp" />
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
		<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
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
	                <h3>Drug Search</h3>
	              </div>
	                <div>
	                	<div id="searchResults" style="width:60%">
	                	<div><%=DrugList.replace("~", " & ") %></div>
	                	<button type="button" class="btn btn-info btn-md pull-right" data-toggle="modal" data-target="#myModal">
	                		graph results
	                	</button>
	                	<%-- <div class="pull-right"><a href="chart-responsive.jsp?ThingType=drugs&ThingList=<%=DrugList%>">graph results</a></div> --%>
							<%=Records %>
						</div>
	                </div>
	                
  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog modal-md">
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Adverse Reactions Chart</h4>
        </div>
        <div class="modal-body">
          <div id="chart_div"></div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>	                
	                
	                
	             </div>
	        </div>
		</div>
		<jsp:include page="inc/scriptrefs.jsp" />
	</body>
</html>

