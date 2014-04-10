<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>HDFS File Upload</title>
</head>
<body>
<form method="post" action="UploadServlet" enctype="multipart/form-data">
Select file to upload to HDFS:
<input type="file" name="dataFile" id="fileChooser"/><br/><br/>
<input type="submit" value="Upload" />
</form>
</body>
</html>