<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <!--[if IE 9]><meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><![endif]-->
    <!--[if gt IE 8]><!--><meta http-equiv="X-UA-Compatible" content="IE=10" ><!--<![endif]-->
    <link rel="stylesheet" href="../../portal/themeResource?theme=portal&location=css/bootstrap.min.css">
    <link rel="stylesheet" href="../../portal/themeResource?theme=portal&location=applications-skin.css">
    <script src="../../portal/themeResource?theme=portal&location=scripts/jquery-2.1.1.min.js"></script>
    <script src="../../portal/themeResource?theme=portal&location=scripts/bootstrap.min.js"></script>
    <!--[if IE]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
</head>
<body class="application-body">
  <div class="wrapper">
  <nav class="header navbar navbar-static-top" role="navigation">
    <div class="navbar-header">
      <button class="navbar-toggle collapsed" type="button" data-toggle="collapse" data-target="#application-menu">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
    </div>
    <div class="collapse navbar-collapse" id="application-menu">
      <ul class="nav navbar-nav">
        <c:forEach var="menu" items="${application.menuList}">
          ${menu.html}
        </c:forEach>
      </ul>
    </div>
  </nav>
  <div class="content">
  <iframe src="${pageContext.request.contextPath}/portal/custom-page/${customPage.name}/index?applicationId=${application.id}"></iframe>
  </div>
</div>
</body>
</html>
