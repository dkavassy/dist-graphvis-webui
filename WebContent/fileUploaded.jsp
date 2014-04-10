<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
   pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Done</title>
</head>
<body>
<h2>Your file has been successfully uploaded to the HDFS</h2>
<i> Note all .graphML and .gml files have been pre-processed for entry at this point.</i>
<br/>
<H3>Select number of workers </H3>
<form method="post" action="RunnerServlet" >
<input type="text" value="1" name="workers"></input>
<H3>Enter the algorithm to use </H3>
<input type="text" value="graphvis.engine.FruchtermanReingoldGraphVis" name="algoChoice"></input>
<H3>Click to start the Giraph computation.</H3>
<input type="submit" value="Start Computation" ></input>
</form>
</body>
</html>