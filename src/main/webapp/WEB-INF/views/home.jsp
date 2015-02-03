<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="description" content="Winter" />
<meta name="keywords" content="playlist,resume,winter,watch,later,twitch,youtube,save" />
<meta name="author" content="bluefoot.dev@gmail.com" />
<title>${selectedPlaylist eq null ? 'Start Page' : selectedPlaylist.name } - Winter</title>

<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/reset.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/text.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/style.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/trackpad/trackpad-scroll-emulator.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/magnific-popup.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/font-awesome.min.css" />" />
<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/hiiconeffect.css" />" />

<script src="<c:url value="/resources/js/jquery-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/trackpad/jquery.trackpad-scroll-emulator.min.js" />"></script>
<script src="https://apis.google.com/js/client.js?onload=init"></script>
<script src="<c:url value="/resources/js/modernizr.custom.js" />"></script>
<script src="<c:url value="/resources/js/magnific-popup.js" />"></script>
<script src="http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js"></script>
<script src="<c:url value="/resources/js/player-global.js" />"></script>
<script src="<c:url value="/resources/js/player-youtube.js" />"></script>
<script src="<c:url value="/resources/js/player-twitch.js" />"></script>

<script type="text/javascript">
    var playlistId = '${selectedPlaylist.playlistId }';
    var contextRoot = '<c:url value="/" />';
    var lastPlayedVideoId = '${selectedPlaylist.lastReproduced.videoId }';
    $(document).ready(function() {
        // Scroll Emulator
        $('.wrapperscroll, .wrapperscrollmain').TrackpadScrollEmulator();

        // Load youtube and twitch videos
        loadTwitchVideos();
        loadYoutubeVideos();
        // HTML5 URL change support
        registerBrowseLocationChange();
        
        $('.add-new-playlist, .add-new-playlist-text').magnificPopup({
            type: 'ajax',
            closeOnContentClick: false,
            closeOnBgClick: false,
            showCloseBtn: true
        });
    });
</script>
</head>
<body>
<div class="app-main">
  <div class="column fixed open" id="left_col">
    <div class="column" id="large_nav">
      <div class="content">
        <div class="top">
          <a href="<c:url value="/" />" id="logo">
            <img src="<c:url value="/resources/images/winterlogo.svg" />" width="173px" height="86px" alt="Winter" />
          </a>
        </div>
        <div class="playlists tse-scrollable wrapperscroll stretch">
          <div class="tse-content">
            <div class="logged-user">
              <span class="logged-user-name" title="<sec:authentication property="principal.fullName"/>"><sec:authentication property="principal.fullName"/></span>
              <a href="<c:url value="/logout" />" title="Logout" class="fa fa-sign-out icon-logout"></a>
             <div class="clear" ></div>
            </div>
            <div class="options-playlists">
              <div class="float-left hi-icon-wrap hi-icon-effect-5 hi-icon-effect-5a">
                <a href="<c:url value="/playlist/add" />" class="hi-icon fa fa-file-o add-new-playlist">Add New Playlist or Video</a>
              </div>
              <div class="float-right hi-icon-wrap hi-icon-effect-5 hi-icon-effect-5a">
                <a href="<c:url value="/editplaylists" />" class="hi-icon fa fa-pencil edit-playlist">Edit</a>
              </div>
              <div class="clear" ></div>
            </div>
            <c:if test="${playlists != null && not empty playlists}">
            <ul class="playlists-images">
              <c:forEach items="${playlists }" var="pl" >
                <li <c:if test="${selectedPlaylist.playlistId  == pl.playlistId}">class="selected-playlist"</c:if>>
                  <a href="<c:url value="/playlist/${pl.playlistId}" />">
                    <img src="${pl.image }" height="126px" width="224px" />
                    ${pl.name }
                  </a>
                </li>
              </c:forEach>
            </ul>
            </c:if>
            <c:if test="${playlists == null || empty playlists}">
            <div class="no-playlists">You have no playlists! <a href="<c:url value="/playlist/add" />" class="add-new-playlist-text">Click here to add!</a></div>
            <script type="text/javascript">
              $(document).ready(function() {
                  $('.add-new-playlist').click();
              });
            </script>
            </c:if>
            <div class="divider"></div>
            <div id="footer">
              <div class="footer-line"><img src="logo" /></div>
              <div class="footer-line">thanks for using Winter!</div>
              <div class="footer-line">
                <a href="<c:url value="/about" />">About</a>
                <a href="http://bluefoot.info" target="_blank">Blog</a>
                <a href="http://careers.stackoverflow.com/bluefoot" target="_blank">Stack Careers</a>
                <a href="mailto:bluefoot.dev@gmail.com" target="_blank">Contact Me</a>
              </div>
            </div>
          </div>
        </div>
        <div class="bottom">
          <!-- fixed content at the bottom if needed -->
        </div>
      </div>
    </div>
    <div id="flyout">
      <div class="point"></div>
      <div class="content"></div>
    </div>
  </div>
  
  <div class="column tse-scrollable wrapperscrollmain stretch" id="main_col">
    <div class="tse-content">
      <div class="content">
        <div id="channel">
          <div id="someotherdiv">
            <h1>${selectedPlaylist eq null ? 'Winter' : selectedPlaylist.name }</h1>
             <c:if test="${videos != null}">
             <ul class="videos-list" last-played-video="${selectedPlaylist.lastReproduced.videoId }">
              <c:forEach items="${videos }" var="v" >  
                <li>
                  <a href="<c:url value="/playlist/${selectedPlaylist.playlistId }/video/${v.videoId }" />" video-id=${v.videoId } class="button-play-video" last-played="${v.currentPosition }">${v.url }</a>
                </li>
              </c:forEach>
              </ul>
              </c:if>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>