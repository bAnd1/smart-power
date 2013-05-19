<#macro masterTemplate title="Smart Power">
<!DOCTYPE html>
<html>
<head>
    <title>${title}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" type="text/css"
          href="/resources/css/master.css"/>
    <script src="http://code.jquery.com/jquery-latest.min.js" type="text/javascript"></script>
</head>

<body>

<header>
    <h1><a href="/">Smart Power</a></h1>
</header>

    <#include 'sticky_header.ftl'>

<div id="main">
    <#include 'sticky_dashboard.ftl'>
    <div id="content">
        <#nested>
    </div>
</div>

<footer>
    <p>&copy; Copyright 2013 Ali, Angermeier, Barnsteiner, Bauer, Romanov</p>
</footer>

</body>
<script type="text/javascript"
        src="/resources/js/sticky.js"></script>
<script type="text/javascript"
        src="/resources/js/sticky_dashboard.js"></script>
</html>
</#macro>