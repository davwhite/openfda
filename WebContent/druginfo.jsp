<%@ page language="java" contentType="text/html; charset=US-ASCII" pageEncoding="US-ASCII"%>
<%@ page import="org.json.JSONObject" %>
<%@ page import="org.json.JSONArray" %>
<%@ page import="org.djw.tools.restlet.*" %>
<%@ page import="openfda.classes.ServerAuth" %>
<%

String DrugName = "";
if (request.getParameter("DrugName") !=null) DrugName = request.getParameter("DrugName");

String Message = "";
JSONArray images = new JSONArray();
JSONObject DrugInfo = new JSONObject();
String Images = "";
String ProductNDC = "";
String SubstanceName = "";
String ProductType = "";
String Route = "";
String GenericName = "";
String BrandName = "";
String ManufacturerName = "";

if (!DrugName.equals("")){
	try {
	    String ServerKey = "";
	    ServerAuth serverAuth = new ServerAuth();
	    ServerKey = serverAuth.getKey();

		int StatusCode = 0;
	    String JsonURL = "";
		String ServiceURI = "/fda/" + ServerKey + "/druginfo/" + DrugName;
	
		RestClient restClient = new RestClient();
		JSONObject jResponse = restClient.getService(ServiceURI);
 	 	JSONObject jBody = jResponse.getJSONObject("Body");
 	 	DrugInfo = jBody.getJSONObject("DrugInfo");
 	
 	 	if (!DrugInfo.isNull("product_ndc")) ProductNDC = DrugInfo.getString("product_ndc");
 	 	if (!DrugInfo.isNull("substance_name")) SubstanceName = DrugInfo.getString("substance_name");
 	 	if (!DrugInfo.isNull("product_type")) ProductType = DrugInfo.getString("product_type");
 	 	if (!DrugInfo.isNull("route")) Route = DrugInfo.getString("route");
 	 	if (!DrugInfo.isNull("generic_name")) GenericName = DrugInfo.getString("generic_name");
 	 	if (!DrugInfo.isNull("brand_name")) BrandName = DrugInfo.getString("brand_name");
 	 	if (!DrugInfo.isNull("manufacturer_name")) ManufacturerName = DrugInfo.getString("manufacturer_name");

	 	if (!DrugInfo.isNull("images")){
	 		images = DrugInfo.getJSONArray("images");
		 	for (int i=0; i<images.length(); i++){
		 		Images += "'<img class='img-responsive' src='" + images.getString(i) + "'><br>";
		 	}
	 	}
	 	
	} catch (Exception e) {
		out.println("An error has occured: " + e);
	}
} else {
	Images = "No images found";
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
	                <h3>Drug Info</h3>
	              </div>
	              <div>
					<dl class="dl-horizontal">
					  <dt>Product NDC</dt>
					  <dd><%=ProductNDC %></dd>
					  <dt>Substance Name</dt>
					  <dd><%=SubstanceName %></dd>
					  <dt>Product Type</dt>
					  <dd><%=ProductType%></dd>
					  <dt>Route</dt>
					  <dd><%=Route %></dd>
					  <dt>Generic Name</dt>
					  <dd><%=GenericName%></dd>
					  <dt>Brand Name</dt>
					  <dd><%=BrandName %></dd>
					  <dt>Manufacturer Name</dt>
					  <dd><%=ManufacturerName %></dd>
					</dl>						
				</div>
	              
                <div>
                <h4 style="padding-top:10px">Media associated with drug</h4> 
                	<%=Images%>
                </div>
	             </div>
	        </div>
		</div>
		<jsp:include page="inc/scriptrefs.jsp" />
	</body>
</html>

			
