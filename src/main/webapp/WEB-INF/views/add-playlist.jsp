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
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/javascript">

    var contextRoot = '<c:url value="/" />';
    var regExpIsMaybeAFullVideoURL = new RegExp("https?:\/\/(www\.)?(youtube|twitch)\..+?\/.+");
    var timeoutId;
    var previousVideo;
    var nameWasChangedByUser = false;
    var imageWasChangedByUser = false;
    $(document).ready(function() {
        $('#addplaylistform .namefield').bind('input', function(){
            nameWasChangedByUser=true;
        });
        
        $('#addplaylistform .imageurlfield').bind('input', function() {
            imageWasChangedByUser=true;
        });
        

        $('#addplaylistform').submit(function() {
            var form = $(this);
            $('input[type="submit"]', form).addClass('loading').attr('disabled','disabled');
            $.ajax({
                type: 'POST',
                dataType: 'json',
                url: $(this).attr('action'),
                data: $(this).serialize(),
                success: function(data) {
                    window.location.href = contextRoot + "playlist/" + data.playlist_id;
                },
                error: function(xhr, ajaxOptions, thrownError) {
                    $('.error-add-playlist').addClass('error').text(xhr.responseJSON.error);
                    $('input[type="submit"]', form).removeClass('loading').removeAttr('disabled','disabled')
                }
            });
            return false;
        });
        
        $("#addplaylistform textarea[name='videos']").keyup(function(){
            if(!$('#addplaylistform .imageurlfield').val() || !imageWasChangedByUser) {
                $('#addplaylistform .imageurlfield').addClass('loading');
            }
            if(!$('#addplaylistform .namefield').val() || !nameWasChangedByUser) {
                $('#addplaylistform .namefield').addClass('loading');
            }
            if(timeoutId) {
                window.clearTimeout(timeoutId);
                timetouId = null;
            }
            timeoutId = window.setTimeout(function(){
                var text = $("#addplaylistform textarea[name='videos']").val();
                
                if(text.match(regExpIsMaybeAFullVideoURL)) {
                    var url = text.split('\n')[0];
                    if(url != previousVideo) {
                      previousVideo = url;
                      fetchYoutubeOrTwitchVideoImage(text.split('\n')[0]);
                    } else {
                        $('#addplaylistform .imageurlfield').removeClass('loading');
                        $('#addplaylistform .namefield').removeClass('loading');
                    }
                } else {
                    $('#addplaylistform .imageurlfield').removeClass('loading');
                    $('#addplaylistform .namefield').removeClass('loading');
                }
                timeoutId = null;
            }, 2000);
        });
    });
    
    function fetchYoutubeOrTwitchVideoImage(url) {
        if(url.indexOf('youtube') > -1) {
            var videoID = getYoutubeVideoID(url);
            $.getJSON('https://www.googleapis.com/youtube/v3/videos?id=' + videoID + '&key=' + gkey + '&part=snippet,contentDetails&callback=?', function(data) {
                if (typeof (data.items[0]) != "undefined") {
                    if(!$('#addplaylistform .imageurlfield').val() || !imageWasChangedByUser) {
                        $('#addplaylistform .imageurlfield').val(data.items[0].snippet.thumbnails.medium.url);
                    }
                    if(!$('#addplaylistform .namefield').val() || !nameWasChangedByUser) {
                        $('#addplaylistform .namefield').val(data.items[0].snippet.title);
                    }
                    $('#addplaylistform .imageurlfield').removeClass('loading');
                    $('#addplaylistform .namefield').removeClass('loading');
                } else {
                    $('#addplaylistform .imageurlfield').removeClass('loading');
                    $('#addplaylistform .namefield').removeClass('loading');                    
                }
            });
        } else {
            var videoID = getTwitchVideoID(url);
            console.log(videoID);
            $.getJSON('https://api.twitch.tv/kraken/videos/'+ videoID + '?callback=?', function(data) {
                console.log('got json ');
                console.log(data);
                if(!$('#addplaylistform .imageurlfield').val() || !imageWasChangedByUser) {
                    $('#addplaylistform .imageurlfield').val(data.preview);
                }
                if(!$('#addplaylistform .namefield').val() || !nameWasChangedByUser) {
                    $('#addplaylistform .namefield').val(data.title);
                }
                $('#addplaylistform .imageurlfield').removeClass('loading');
                $('#addplaylistform .namefield').removeClass('loading');
            });
        }
    }
</script>
</head>
<body>
<div class="form ajax-form-main">
<c:url value="/playlist/add" var="submitaction" />
<h1>Add New Playlist or Video</h1>
<div class="error-add-playlist"></div>
  <!-- <form:input type="hidden" name="_csrf" value="${_csrf.token}" path="_csrf" /> -->
<form:form action="${submitaction }" commandName="addplaylistform" >
<input type="hidden" name="_csrf" value="${_csrf.token}" />
  <form:input class="namefield" type="text" name="name" placeholder="Name" autofocus="autofocus" required="required" path="playlist.name"/>
  <form:input class="imageurlfield" type="url" name="image" placeholder="Image" required="required" path="playlist.image" />
  <form:textarea name="videos" placeholder="Videos (one line per video)" required="required" rows="10" path="videos"></form:textarea>
  <input type="submit" value="Save" />
</form:form>
</div>
</body>
</html>