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
<link rel="stylesheet" href="<c:url value="/resources/css/style.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/vendor/trackpad/trackpad-scroll-emulator.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/vendor/magnific-popup.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/vendor/font-awesome.min.css" />" />
<link rel="stylesheet" href="<c:url value="/resources/css/hiiconeffect.css" />" />
<script src="<c:url value="/resources/js/vendor/jquery-1.11.2.min.js" />"></script>
<script src="<c:url value="/resources/js/vendor/jquery.trackpad-scroll-emulator.min.js" />"></script>
<script src="https://apis.google.com/js/client.js?onload=init"></script>
<script src="<c:url value="/resources/js/vendor/modernizr.custom.js" />"></script>
<script src="<c:url value="/resources/js/vendor/magnific-popup.min.js" />"></script>
<script src="http://ajax.googleapis.com/ajax/libs/swfobject/2.2/swfobject.js"></script>
<script src="<c:url value="/resources/js/player-global.js" />"></script>
<script src="<c:url value="/resources/js/player-youtube.js" />"></script>
<script src="<c:url value="/resources/js/player-twitch.js" />"></script>

<script>
    var playlistId = '${selectedPlaylist.playlistId }';
    var contextRoot = '<c:url value="/" />';
    var lastPlayedVideoId = '${selectedPlaylist.lastReproduced.videoId }';
    /**
     * If this var is set, means user wants to force playing this video. So 
     * last position of playlist will be ignored and this video will be played.
     */
    var videoToPlay = '${videoToPlay }';
    var isPlaylistOrVideoPage = (playlistId != '');
    var isAutoPlayEnabled = true;
    $(document).ready(function() {
        if(isPlaylistOrVideoPage) {
            $('.app-main').addClass('playlist-or-video-page');
        }
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
        
        $('.button-delete-playlist').each(function(){
            $(this).bind('click', function(e){
                if(e.altKey) {
                    var playlistIdToBeDeleted = $(this).attr('data-playlist-id');
                    $('#playlist' + playlistIdToBeDeleted).fadeOut('fast');
                    $.ajax({
                        type: 'POST',
                        dataType: 'json',
                        url: '<c:url value="/playlist/delete" />',
                        data: 'playlistId=' + playlistIdToBeDeleted,
                        success: function(data) {
                            if(playlistIdToBeDeleted == playlistId) {
                                window.location.href = contextRoot;
                            } else {
                              $('#playlist' + playlistIdToBeDeleted).remove();
                            }
                        },
                        error: function(xhr, ajaxOptions, thrownError) {
                            $('#playlist' + playlistIdToBeDeleted).fadeIn('fast')
                            warningMessage(xhr.responseJSON.error, true);
                        }
                    });
                } else {
                    warningMessage("hold ALT key or it won't work", true);
                }
                e.preventDefault();
            });
        });
    });
    
    function warningMessage(text, bounce) {
        if($('#msg-alt-key').length != 0) {
            $('#msg-alt-key').remove();
        }
        var div = '<div id="msg-alt-key" class="top-page-message"><span>' + text + '</span></div>';
        $('body').prepend(div);
        if(bounce) {
          for(var i = 0; i < 5; i++) {
              $('#msg-alt-key').animate({marginTop: '-=10'}, 50).animate({marginTop: '+=10'}, 50);
          }
        }
         setTimeout(function() {
            $("#msg-alt-key").fadeOut('fast', function() {
                $(this).remove();
            });
          }, 3000);
    }
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
              <span class="logged-user-name" title="<sec:authentication property="principal.displayName"/>"><sec:authentication property="principal.displayName"/></span>
              <a href="<c:url value="/logout" />" title="Logout" class="fa fa-sign-out icon-logout"></a>
             <div class="clear" ></div>
            </div>
            <div class="options-playlists">
              <div class="hi-icon-wrap hi-icon-effect-5 hi-icon-effect-5a">
                <a href="<c:url value="/playlist/add" />" class="hi-icon fa fa-file-o add-new-playlist">Add New Playlist or Video</a>
              </div>
              <%--
              <div class="float-right hi-icon-wrap hi-icon-effect-5 hi-icon-effect-5a">
                <a href="<c:url value="/editplaylists" />" class="hi-icon fa fa-pencil edit-playlist">Edit</a>
              </div>
              <div class="clear" ></div>
               --%>
            </div>
            <c:if test="${playlists != null && not empty playlists}">
            <ul class="playlists-images">
              <c:forEach items="${playlists }" var="pl" >
                <li id="playlist${pl.playlistId}" <c:if test="${selectedPlaylist.playlistId  == pl.playlistId}">class="selected-playlist"</c:if>>
                  <a class="link-playlist-thumb" href="<c:url value="/playlist/${pl.playlistId}" />">
                    <img src="${pl.image }" height="126px" width="224px" /><br />${pl.name }</a>
                  <a href="javascript:;" class="button-delete-playlist fa fa-trash-o" data-playlist-id="${pl.playlistId}" title="Delete this playlist (hold ALT key)"></a>
                </li>
              </c:forEach>
            </ul>
            </c:if>
            <c:if test="${playlists == null || empty playlists}">
            <div class="no-playlists">You have no playlists! <a href="<c:url value="/playlist/add" />" class="add-new-playlist-text">Click here to add!</a></div>
            <script>
              $(document).ready(function() {
                  $('.add-new-playlist').click();
              });
            </script>
            </c:if>
            <div class="divider"></div>
            <div id="footer">
              <div class="footer-line"><img src="<c:url value="/resources/images/wintersymbol.svg" />" height="24px" alt="Winter Symbol" /></div>
              <div class="footer-line">thanks for using Winter!</div>
              <div class="footer-line">
                <a href="<c:url value="/faq" />">FAQ</a>
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
             <ul class="videos-list" data-last-played-video="${selectedPlaylist.lastReproduced.videoId }">
              <c:forEach items="${videos }" var="v" >  
                <li>
                  <a href="<c:url value="/playlist/${selectedPlaylist.playlistId }/video/${v.videoId }" />" data-video-id="${v.videoId }" class="button-play-video" data-last-played="${v.currentPosition }">${v.url }</a>
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