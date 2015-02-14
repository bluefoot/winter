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
<title>Winter</title>
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
<script>
$(document).bind("mobileinit", function () {
    $.mobile.ajaxEnabled = false;
});
</script>
<script src="<c:url value="/resources/js/jquery.mobile.custom.min.js" />"></script>
</head>
<script>
  $(document).ready(function() {
    $('.open-playlist').bind( "taphold", function(event){
        openPopup(this);
    });
  });
  function openPopup(link) {
      $('#popupplaylist a[rel=openplaylist]').attr('href', $(link).attr('href'));
      playlistId = $(link).attr('data-playlist-id');
      $('#popupplaylist a[rel=deleteplaylist]').unbind().bind('click', function(event){
          removePlaylist(playlistId);
          $('#popupplaylist').popup("close");
          event.preventDefault();
      });
      $('#popupplaylist').popup("open");
  }
  function removePlaylist(playlistId) {
      $('a[data-playlist-id=' + playlistId + ']').fadeOut('fast');
      $.ajax({
          type: 'POST',
          dataType: 'json',
          url: '<c:url value="/playlist/delete" />',
          data: 'playlistId=' + playlistId,
          success: function(data) {
             siblings = $('a[data-playlist-id=' + playlistId + ']').parents('li').nextAll();
             $('a[data-playlist-id=' + playlistId + ']').parents('li').remove();
             siblings.toggleClass('ui-block-a').toggleClass('ui-block-b');
          },
          error: function(xhr, ajaxOptions, thrownError) {
              $('a[data-playlist-id=' + playlistId + ']').fadeIn('fast');
              alert(xhr.responseJSON.error);
          }
      });
  }
</script>
<body>
<div class="app-main wmobile">
  <div data-role="panel" id="menupanel" data-position="left" data-display="overlay" data-theme="a">
    <ul class="ui-alt-icon ui-nodisc-icon menu-wmobile" data-role="listview">
      <li data-filtertext="demos homepage" data-icon="home"><a href="<c:url value="/" />">Playlists</a></li>
      <li data-filtertext="buttons button markup buttonmarkup method anchor link button element" data-icon="plus"><a href=".././">Create New Playlist</a></li>
      <li data-filtertext="buttons button markup buttonmarkup method anchor link button element" data-icon="action"><a href="<c:url value="/faq" />">FAQ</a></li>
      <li data-filtertext="buttons button markup buttonmarkup method anchor link button element" data-icon="power"><a href="<c:url value="/logout" />">Logout</a></li>
    </ul>
  </div> <!--left panel-->
  <div data-role="header" class="header-wmobile">
    <h1><img src="<c:url value="/resources/images/winterlogomobile.svg" />" alt="Winter" height="40px" /></h1>
    <a href="#menupanel" class="ui-btn ui-btn-icon-notext ui-icon-bars ui-nodisc-icon wmobile-menu-icon">Menu</a>
  </div> <!--header-->
  <div role="main" class="ui-content playlists-wmobile">
      <ul class="ui-grid-a playlists-wmobile-grid">
        <c:forEach items="${playlists }" var="pl" varStatus="loop" >
          <li class="ui-block-${loop.index % 2 == 0 ? 'a' : 'b'}">
            <div class="ui-bar ui-bar-a cell-wmobile">
              <a class="open-playlist" data-playlist-id="${pl.playlistId}" data-role="none" href="<c:url value="/playlist/${pl.playlistId}" />">
                <img src="${pl.image }" /><br />${pl.name }</a>
            </div>
          </li>
        </c:forEach>
      </ul><!-- /grid-c -->
  </div> <!-- main -->
  <div id="popupplaylist" data-role="popup">
    <ul data-role="listview" data-inset="true" style="min-width:210px;">
      <li data-role="list-divider">Playlist Options</li>
      <li><a rel="openplaylist" href="#">Open</a></li>
      <li><a rel="deleteplaylist" href="#">Delete</a></li>
    </ul>
  </div>
</div>
</body>
</html>