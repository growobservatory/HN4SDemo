<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>HN4SDemo</h1>
        <p>This is a Java Demo app showing how to get data from GROW's HN4S alapha platform.</p>

        <p>The Main points, how to send a login and password, thorugh basic authentication as a Base64 encoded string. How to request data, including requesting data using JSON information in the main body of the POST See the Web.java class for most of this.</p>

    <p>Data is returned as is in JSON format</p>
    <p>Note: This project needs a username and password saved in a config.properties file <br>
        See the file uk.ac.dundee.computing.aec.growexample1.lib.Web.java for details of where to put this and its format</p>
    <p>Example endpoints:
    <ul>
        <li><a href="/GrowExample1/locations">Locations</li>
        <li><a href="/GrowExample1/timeseries">Timeseries</li>
        <li><a href="/GrowExample1/projections">Projections</li>
    </ul>
        
        
    </p>
    
    
    </body>
</html>
