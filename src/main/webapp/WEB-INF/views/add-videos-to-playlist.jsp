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
<!DOCTYPE html>
<html lang="en">
<head>
<script>
    var contextRoot = '<c:url value="/" />';
    $(document).ready(function() {
        $('#playlistform').submit(function() {
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
    });
</script>
</head>
<body>
<div class="form ajax-form-main">
<c:url value="/playlist/addvideos" var="submitaction" />
<h1>Add Videos To Playlist</h1>
<div class="error-add-playlist"></div>
<form:form action="${submitaction }" commandName="playlistform" >
  <form:input type="hidden" name="playlist_id" path="playlist.playlistId" />
  <form:textarea name="videos" placeholder="Videos (one line per video)" required="required" rows="10" path="videos"></form:textarea>
  <input type="submit" value="Save" />
</form:form>
</div>
</body>
</html>