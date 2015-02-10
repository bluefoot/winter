<%--
Copyright 2015 Gewton Jhames <bluefoot.dev@gmail.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
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
    var contextRoot = '<c:url value="/" />';
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
    
    <div class="providers-login">
      <form action="<c:url value="/auth/google" />" method="post" class="provider-login">
        <input type="hidden" name="remember-me" value="true" />
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="hidden" name="scope" value="email https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/tasks https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/latitude.all.best" />
        <input type="hidden" name="request_visible_actions" value="http://schemas.google.com/AddActivity http://schemas.google.com/BuyActivity http://schemas.google.com/CheckInActivity http://schemas.google.com/CommentActivity http://schemas.google.com/CreateActivity http://schemas.google.com/DiscoverActivity http://schemas.google.com/ListenActivity http://schemas.google.com/ReserveActivity http://schemas.google.com/ReviewActivity http://schemas.google.com/WantActivity"/>
        <input type="hidden" name="access_type" value="offline"/>
        <input type="submit" value="Login with Google!" class="login-with-provider login-with-google" />
      </form>
      <form action="<c:url value="/auth/twitter" />" method="post" class="provider-login">
        <input type="hidden" name="remember-me" value="true" />
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="submit" value="Login with Twitter!" class="login-with-provider login-with-twitter" />
      </form>
      <form action="<c:url value="/auth/facebook" />" method="post" class="provider-login">
        <input type="hidden" name="remember-me" value="true" />
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="hidden" name="scope" value="email public_profile" />
        <input type="submit" value="Login with Facebook!" class="login-with-provider login-with-facebook" />
      </form>
    </div>
    
    <form action="<c:url value="/openidlogin" />" method="post" id="openid_form" class="login-provider">
      <input type="hidden" name="remember-me" value="true" />
      <input type="hidden" name="_csrf" value="${_csrf.token}" />
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