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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" %>
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
<title>F.A.Q. - Winter</title>
<link rel="stylesheet" href="<c:url value="/resources/css/reset.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/text.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />" />
</head>
<body>
<div class="static-main">
  <header id="about-header"><h1>F.A.Q.</h1></header>
  <div id="about-text" class="tse-scrollable">
    <div class="tse-content">
      <p><a href="<c:url value="/" />">back</a>
      <h2>Why "Winter"?</h2>
      <p>I give pointless aliases to the stuff I build.</p>
      <h2>What this thing does, really?</h2>
      <p>You create playlists then you start watching videos. When you're done, you can just close the page and when you come back later you'll start from where you left off.</p>
      <h2>Which video services does Winter supports?</h2>
      <p>Youtube videos and Twitch VODS (past broadcasts or highlights). If you would like to see it support another video service, <a href="mailto:bluefoot.dev@gmail.com">contact me</a>.</p>
      <h2>Why shouldn't I just use Youtube's playlist feature then?</h2>
      <p>If you only watch Youtube videos, and not Twitch VODs, and you don't care to resume from where you stopped, then there's really no reason to use this app.</p>
      <h2>Do you save any of my personal info such as email, password?</h2>
      <p>Only your email and sometimes your name is read from your authentication provider. No additional info is asked.</p>
      <h2>Will you share my name or email with any third party?</h2>
      <p>No. Never.</p>
      <p><a href="<c:url value="/" />">back</a>
    </div>
  </div>
  <footer id="footer-general">
    <div class="footer-line"><img src="<c:url value="/resources/images/wintersymbol.svg" />" height="24px" alt="Winter Symbol" /></div>
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