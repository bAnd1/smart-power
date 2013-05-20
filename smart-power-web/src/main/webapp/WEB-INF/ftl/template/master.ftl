<#macro master title="Smart Power">
	<#import "/spring.ftl" as spring />
	<#assign sec=JspTaglibs["http://www.springframework.org/security/tags"] />
<!DOCTYPE html>
<html>
<head>
	<title>${title}</title>
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
	<link rel="stylesheet" type="text/css"
		  href="/resources/css/master.css"/>
	<script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
</head>

<body>

<header>
	<h1><a href="/">Smart Power</a></h1>

	<div id="user">
		<@sec.authorize access="isAuthenticated()">
			Hallo <@sec.authentication property="principal.username"/><br/>
			<a href="/logout">Logout</a>
		</@sec.authorize>
	</div>
</header>

<@sec.authorize access="isAuthenticated()">
<div id="stickyheader">
	<table>
		<tr>
			<td><a href="">Filter</a></td>
			<td><a href="">Notifications</a></td>
			<td><a href="">Forecast</a></td>
			<td><a href="">Settings</a></td>
		</tr>
	</table>
</div>
</@sec.authorize>

<div id="stickyalias"></div>

<div id="main">

	<#nested/>
</div>

<footer>
	<p>&copy; Copyright 2013 Ali, Angermeier, Barnsteiner, Bauer, Romanov</p>
</footer>

</body>
<script type="text/javascript" src="/resources/js/sticky.js"></script>
<script type="text/javascript" src="/resources/js/sticky_dashboard.js"></script>
</html>
</#macro>"