<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
	<title>Upload Done - Distributed Graph Visualisation</title>
	<script src="//code.jquery.com/jquery-1.11.0.min.js"></script>
	<script>
		$(document).ready(function() {
			$("#form").submit(function() {
				$("#preloader").show();
				$("#submit").attr('disabled', true);
			});
		});
	</script>
	
	<style type="text/css">
		#preloader {
			display: none;
			top: 5px;
			position: relative;
			margin-left: 5px;
		}
	</style>
</head>

<body>
	<h2>Your file has been successfully uploaded to the HDFS</h2>
	<p><em>Note all .graphML and .gml files have been pre-processed for entry at this point.</em></p>
	
	<form id="form" method="post" action="RunnerServlet" >
	<label for="workers">Please select the number of workers</label>
	<input type="text" size="2" value="1" id="workers" name="workers" />
	
	<label for="algorithm">Enter the algorithm to use</label>
	<select name="algoChoice" id="algorithm">
		<option value="graphvis.engine.FruchtermanReingoldGraphVis">Fruchterman-Reingold</option>
	</select>
	<input id="submit" type="submit" value="Start Computation" />
	<img src="preloader.GIF" id="preloader" alt="loading..." height="20" width="20" />
	</form>
	
</body>

</html>