<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" /> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="description" content="Winter" />
<meta name="keywords" content="playlist,resume,winter,watch,later,twitch,youtube,save" />
<meta name="author" content="gewton" />
<link rel="apple-touch-icon" href="<c:url value="/resources/images/apple-touch-icon.png" />" />
<link rel="icon" href="<c:url value="/resources/images/favicon.png" />" />
<title>Winter</title>
<link rel="stylesheet" href="<c:url value="/resources/css/reset.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/text.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/openid-shadow.css" />" />
<script src="<c:url value="/resources/js/jquery-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/openid-jquery.js" />"></script>
<script src="<c:url value="/resources/js/openid-en.js" />"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      openid.init('openid_identifier');
    });
  </script>
</head>
<body>
<div class="static-main">
  <header id="big-logo"><h1>Winter</h1></header>
  <div id="site-description"><h2>Winter lets you create and reproduce playlists and remembers where you left off!</h2></div>
  <div id="login-form-container">
    <p>Select your account provider to login into Winter using one of your existing accounts!</p>
    <form action="<c:url value="/openidlogin" />" method="post" id="openid_form">
      <input type="hidden" name="rememberme" value="true" />
        <div id="openid_choice">
          <div id="openid_btns"></div>
        </div>
        <div id="openid_input_area">
          <input id="openid_identifier" name="openid_identifier" type="text" value="http://" />
          <input id="openid_submit" type="submit" value="Sign-In"/>
        </div>
        <noscript>
          <p>OpenID is service that allows you to log-on to many different websites using a single indentity.
          Find out <a href="http://openid.net/what/">more about OpenID</a> and <a href="http://openid.net/get/">how to get an OpenID enabled account</a>.</p>
        </noscript>
    </form>
  </div> <!--#login-form-container-->
  <footer id="footer-general">
    <div class="footer-line"><img src="<c:url value="/resources/images/wintersymbol.svg"/>" height="24px" alt="Winter Symbol" /></div>
    <div class="footer-line">
      <a href="<c:url value="/faq" />">FAQ</a>
      <a href="http://bluefoot.info" target="_blank">Blog</a>
      <a href="http://careers.stackoverflow.com/bluefoot" target="_blank">Stack Careers</a>
      <a href="mailto:bluefoot.dev@gmail.com" target="_blank">Contact Me</a>
    </div>
  </footer>
</div>

</body>
</html>