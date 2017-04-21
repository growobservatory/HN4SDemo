# HN4SDemo
This is a Java Demo app showing how to get data from  GROW's HN4S alapha platform.  

The Main points, how to send a login and password, thorugh basic authentication as a Base64 encoded string.
How to request data, including requesting data using JSON information in the main body of the POST
See the Web.java class for most of this.

Data is returned as is in JSON format

Note:
This project needs a username and password saved in a config.properties file
See the file uk.ac.dundee.computing.aec.growexample1.lib.Web.java for details of where to put this and its format
