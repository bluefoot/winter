<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/openid-shadow.css" />" />

<script src="<c:url value="/resources/js/jquery-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/openid-jquery.js" />"></script>
<script src="<c:url value="/resources/js/openid-en.js" />"></script>
  <script type="text/javascript">
    $(document).ready(function() {
      openid.init('openid_identifier');
    });
  </script>
<title>index</title>
</head>
<body>
  <h1>Login</h1>
  <%--
  <h2>Efetue login com o formulário abaixo</h2>
  
  <form action="j_spring_security_check" method="post">
    <label for="j_username">Usuário</label>: <input id="j_username" name="j_username"
      size="20" maxlength="50" type="text" /> <br /> <label for="j_password">Senha</label>:
    <input id="j_password" name="j_password" size="20" maxlength="50" type="password" />
    <br /> <input type="submit" value="Login" />
  </form>
  <h2>Ou, utilize seu OpenID para efetuar login</h2>
  <form action="j_spring_openid_security_check" method="post">
    <label for="openid_identifier">Seu OpenID</label>: <input id="openid_identifier" name="openid_identifier" size="20" maxlength="100" type="text" />  <br /> 
    <input type="submit" value="Login" />
  </form>
  <h2>LOGIN WITH GOOGLE</h2>
  
  
<form action="<c:url value="/login" />" id="google-login-form" method="post">
    <input name="openid_identifier" size="50"
           maxlength="100" type="hidden"
           value="http://www.google.com/accounts/o8/id"/>
    <label class="fixed"><!-- intentionally left blank --></label>
    <div class="input">
       <input id="proceed-google" type="submit" value="Do it with Google" />
    </div>
</form>
 --%>
 
 
  <form action="<c:url value="/openidlogin" />" method="post" id="openid_form">
    <input type="hidden" name="rememberme" value="true" />
    <fieldset>
      <legend>Sign-in or Create New Account</legend>
      <div id="openid_choice">
        <p>Please click your account provider:</p>
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
    </fieldset>
  </form>
</body>
</html>