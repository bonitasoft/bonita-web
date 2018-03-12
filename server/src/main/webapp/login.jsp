<%-- Copyright (C) 2009 BonitaSoft S.A.
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
<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>
<%@page import="org.bonitasoft.console.common.server.jsp.JSPUtils"%>
<%@page import="org.bonitasoft.console.common.server.jsp.JSPI18n"%>
<%
    JSPUtils JSP = new JSPUtils(request, session);
    JSPI18n i18n = new JSPI18n(JSP);

    // Build Action URL
    final String tenantId = StringEscapeUtils.escapeHtml4(JSP.getParameter("tenant"));
    String redirectUrl = JSP.getParameter("redirectUrl");

    StringBuffer actionUrl = new StringBuffer("loginservice?");
    StringBuffer styleUrl = new StringBuffer("portal/themeResource?theme=portal");
    if (tenantId != null) {
        actionUrl.append("tenant=").append(tenantId).append("&");
		styleUrl.append("&tenant=").append(tenantId);
    }

    if (redirectUrl != null) {
    	if (tenantId != null) {
    		redirectUrl = redirectUrl.replaceAll("[\\?&]tenant=\\d+$", "").replaceAll("tenant=\\d+&", "");
    		if (redirectUrl.contains("?")) {
    			redirectUrl += "&";
    		} else {
    			redirectUrl += "?";
    		}
    		redirectUrl += "tenant=" + tenantId;
    	}
        actionUrl.append("redirectUrl=" + URLEncoder.encode(redirectUrl, "UTF-8"));
    }

    // Error messages
    String errorMessage = "";
    boolean disableLogin = false;
    String noBonitaHomeMessage = request.getAttribute("noBonitaHomeMessage") + "";
	String noBonitaClientFileMessage = request.getAttribute("noBonitaClientFileMessage") + "";
	String loginFailMessage = request.getAttribute("loginFailMessage") + "";
	String tenantInMaintenanceMessage = request.getAttribute("tenantInMaintenanceMessage") + "";

    // Technical problems
    if (
        !JSP.getParameter("isPlatformCreated", true) ||
		!JSP.getParameter("isTenantCreated", true) ||
		"tenantNotActivated".equals(loginFailMessage) ||
		"noBonitaHomeMessage".equals(noBonitaHomeMessage) ||
		"noBonitaClientFileMessage".equals(noBonitaClientFileMessage)
	) {
        errorMessage = i18n._("The server is not available") + "<br />" + i18n._("Please, contact your administrator.");
        disableLogin = true;
    }
    // No profile for this user
    else if ("noProfileForUser".equals(loginFailMessage)) {
        errorMessage = i18n._("Login failed. No profile has been set up for this user. Contact your administrator.");
    }
 	// Tenant in Maintenance error
    else if ("tenantInMaintenanceMessage".equals(tenantInMaintenanceMessage)) {
        errorMessage = i18n._("This service is offline for maintenance. Please try later.");
    }
 	// Login or password error
    else if ("loginFailMessage".equals(loginFailMessage)) {
        errorMessage = i18n._("Unable to log in. Please check your username and password.");
    }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
<title>Bonita Portal</title>
<link rel="icon" type="image/png" href="images/favicon2.ico" />
<link rel="stylesheet" type="text/css" href="<%= styleUrl %>&location=css/login.css"/>

<script>
  document.addEventListener("DOMContentLoaded", function() {
		if (window != window.top) {
			try {
				if (window.frameElement.id == "bonitaframe") {
					/* if the login jsp is displayed inside a "bonitaframe" iframe it probably means the session is invalid so refresh the whole page */
					window.parent.location.reload();
					return;
				}
			} catch (e) {
				/* nothing to do (bonita is probably displayed inside an iframe of a different domain app) */
			}
		}
		/* Add url hash to form action url */
		var form = document.getElementById('LoginForm');
		form.setAttribute('action', form.getAttribute('action') + window.location.hash);
	});
</script>

</head>
<body id="LoginPage">

	<div id="LoginHeader">
		<h1><span><%= i18n._("Welcome to") %></span> <%= i18n._("Bonita Portal") %></h1>
	</div>

	<div id="floater"></div>

	<div class="LoginFormWrapper">
	    <div id="LoginFormContainer" >
		<div id="logo">
			<img src="<%= styleUrl %>&location=skin/images/login-logo.png"/>
		</div>

		<div class="body">
			<form id="LoginForm" action="<%=actionUrl%>" method="post" autocomplete="off">

				<div class="header">
					<h2><%=i18n._("Login form")%></h2>
				</div>

				<p class="error"><%=errorMessage.length() > 0 ? errorMessage  : ""%></p>

				<div class="formentries">

					<div class="formentry" title="<%=i18n._("Enter your login (username)")%>">
						<div class="label">
							<label for="username"><%=i18n._("User")%></label>
						</div>
						<div class="input">
							<input title="<%=i18n._("Username")%>"
                                   id="username"
                                   name="username"
                                   value="<%= StringEscapeUtils.escapeHtml4(JSP.getSessionOrCookie("username", "")) %>"
                                   placeholder="<%=i18n._("User")%>"
                                   type="text"
                                   autocomplete="off"
                                   tabindex="1"
                                   maxlength="255" <%=disableLogin ? "disabled=\"disabled\" " : ""%>
                            />
						</div>
					</div>

					<div class="formentry" title="<%=i18n._("Enter your password")%>">
						<div class="label">
							<label for="password"><%=i18n._("Password")%></label>
						</div>
						<div class="input">
							<input title="<%=i18n._("Password")%>"
                                   id="password"
                                   name="password"
                                   type="password"
                                   tabindex="2"
                                   autocomplete="off"
                                   maxlength="50"
                                   placeholder="<%=i18n._("Password")%>" <%=disableLogin ? "disabled=\"disabled\" " : ""%>
                            />
						</div>
						<input name="_l" type="hidden" value="<%=i18n.getLocale()%>" />
					</div>

				</div>
				<div class="formactions">
					<input type="submit"
                           value="<%=i18n._("Login")%>" <%=disableLogin ? "disabled=\"disabled\" " : ""%>
                    />
				</div>
			</form>
		</div>
	</div>
	</div>
</body>
</html>
