<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.djw.tools.restlet.*" %>
<%@ page import="openfda.classes.ServerAuth" %>
<%
String ServerKey = "";
String Reaction = "";
String Reaction2 = "";
String ReactionList = "";
if (request.getParameter("reaction1") !=null) Reaction = request.getParameter("reaction1");
if (request.getParameter("reaction2") !=null) Reaction2 = request.getParameter("reaction2");
String sResponse = "";
String Message = "";
String Records = "";
if (!Reaction.equals("0") || !Reaction2.equals("0")){
	try {
	    
	    ServerAuth serverAuth = new ServerAuth();
	    ServerKey = serverAuth.getKey();

		int StatusCode = 0;
	    String JsonURL = "";
	    
	    if (!Reaction.equals("0")){
	    	ReactionList = Reaction;
	    }
	    if (!Reaction.equals("0") && Reaction.equals("0")){
	    	ReactionList = Reaction;
	    }
	    if (Reaction.equals("0") && !Reaction2.equals("0")){
	    	ReactionList = Reaction2;
	    }
	    if (!Reaction.equals("0") && !Reaction2.equals("0")){
	    	ReactionList =  "~" + Reaction2;;
	    }

	    if (!Reaction2.equals("0")) ReactionList += "~" + Reaction2;
		String ServiceURI = "/fda/" + ServerKey + "/search/reaction/" + ReactionList;
	
		RestClient restClient = new RestClient();
		JSONObject jResponse = restClient.getService(ServiceURI);
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
				String dat = row.getString(c);
				if (c == 0){
					dat = "<a href='druginfo.jsp?DrugName=" + dat + "'>" + dat + "</a>";
				}
				Records += "<td>" + dat + "</td>";
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
		String ServiceURI = "/fda/" + ServerKey + "/chart/reactions/" + ReactionList;
		RestClient restClient = new RestClient();
		JSONObject jResponse = restClient.getService(ServiceURI);
	 	JSONObject jBody = jResponse.getJSONObject("Body");
	 	JSONArray jRecords = new JSONArray();
		jRecords = jBody.getJSONArray("chartdata");
		sResponse = jRecords.toString();
	} catch (Exception e) {
		out.println("An error has occured: " + e);
	}

} else {
	Records = "No reaction selected";
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
	                <h3>Reaction Search</h3>
	                </div>
	                <div>
						<div id="searchResults" style="width:60%">
						<div><%=ReactionList.replace("~", " & ") %></div>
						<button type="button" class="btn btn-info btn-md pull-right" data-toggle="modal" data-target="#myModal">
	                		graph results
	                	</button>
						<%-- <div class="pull-right"><a href="chart-responsive.jsp?ThingType=reactions&ThingList=<%=ReactionList%>">graph results</a></div> --%>
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
