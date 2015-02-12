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
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
<title>${selectedPlaylist eq null ? 'Start Page' : selectedPlaylist.name } - Winter</title>
<link rel="apple-touch-icon" href="<c:url value="/resources/images/apple-touch-icon.png" />" />
<link rel="icon" href="<c:url value="/resources/images/favicon.png" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/reset.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/text.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/style-mobile.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/jquery.mobile.winter.theme.min.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/jquery.mobile.icons.min.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/jquery.mobile.custom.structure.min.css" />" />
<script src="<c:url value="/resources/js/jquery-1.11.2.min.js" />"></script>
<script src="https://apis.google.com/js/client.js?onload=init"></script>
<script src="<c:url value="/resources/js/jquery.mobile.custom.min.js" />"></script>
<script src="http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js"></script>
<script src="<c:url value="/resources/js/player-global.js" />"></script>
<script src="<c:url value="/resources/js/player-youtube.js" />"></script>
<script src="<c:url value="/resources/js/player-twitch.js" />"></script>

<script type="text/javascript">
    var contextRoot = '<c:url value="/" />';
    var csrfToken = '${_csrf.token}';
    var isPlaylistOrVideoPage = false;
    var isAutoPlayEnabled = false;
</script>
</head>
<body>
<div data-role="page" class="app-main wmobile">
  <div data-role="panel" id="menupanel">
    <ul class="jqm-list ui-alt-icon ui-nodisc-icon">
      <li data-filtertext="demos homepage" data-icon="home"><a href=".././">Playlists</a></li>
      <li data-filtertext="buttons button markup buttonmarkup method anchor link button element" data-icon="home"><a href=".././">Create New Playlist</a></li>
      <li data-filtertext="buttons button markup buttonmarkup method anchor link button element" data-icon="home"><a href=".././">FAQ</a></li>
      <li data-filtertext="buttons button markup buttonmarkup method anchor link button element" data-icon="home"><a href=".././">Logout</a></li>
    </ul>
  </div> <!--left panel-->
  
  <div data-role="header" class="header-wmobile">
    <h1><img src="<c:url value="/resources/images/winterlogomobile.svg" />" alt="Winter" height="40px" /></h1>
    <a href="#menupanel" class="jqm-navmenu-link ui-btn ui-btn-icon-notext ui-corner-all ui-icon-bars ui-nodisc-icon ui-alt-icon ui-btn-left">Menu</a>
  </div>
  <div role="main" class="ui-content playlists-wmobile">
      <ul class="ui-grid-a playlists-wmobile-grid">
        <c:forEach items="${playlists }" var="pl" varStatus="loop" >
          <li class="ui-block-${loop.index % 2 == 0 ? 'a' : 'b'}">
            <div class="ui-bar ui-bar-a cell-wmobile">
              <a data-role="none" href="<c:url value="/playlist/${pl.playlistId}" />">
                <img src="${pl.image }" /><br />${pl.name }</a>
            </div>
          </li>
        </c:forEach>
      </ul><!-- /grid-c -->
  </div>

</div>
</body>
</html>