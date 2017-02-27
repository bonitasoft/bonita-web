<%-- Copyright (C) 2013 BonitaSoft S.A.
 BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 2.0 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>. --%>

<%@page language="java"%>
<%@page contentType="text/html; charset=UTF-8"%>
<%@page isErrorPage="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>403 Forbidden</title>
	<style>

	.wrap {
	    width: 1060px;
	    height:auto;
	    margin: auto;
	    text-align:center;
	    position:relative;
	}

	body
	{
		background-image:url('<%=getServletConfig().getServletContext().getContextPath()%>/images/error-lines-pattern.gif');
		background-repeat:repeat;
	}
	p#message
	{
		position: absolute;
		margin: auto;
		margin-top: -182px;
		left:0;
		right:0;
		color:#fff;
		font-weight:bold;
		color:black;
	}
	p#statuscode
	{
		position: absolute;
		margin: auto;
		margin-top: -300px;
		left:0;
		right:0;

		color:white;
		font-weight:bold;
		font-size:72px;
		text-shadow: 0.1em 0.1em 0.2em black;		
	}
	p#error
	{
		position: absolute;
		margin: auto;
		margin-top: -135px;
		left:0;
		right:0;
		color:#fff;
		font-weight:bold;
		color:black;
	}	
	</style>
</head>
<body>

	<div class="wrap">
		<div>
			<img src="<%=getServletConfig().getServletContext().getContextPath()%>/images/error-red-circle.png">
			<p id="statuscode">403</div>
			<p id="message">Forbidden</p>
			<p id="error">Oops. Error.</p>			
		</div>
	</div>
</body>
</html>
