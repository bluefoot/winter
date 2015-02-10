
/**Holds the id of the infinite interval that saves the current video time. Used
 * to stop the flow afterwards*/
var intervalSaveCurrentVideoTimeId;

/**Holds the twitch player object when available. Used to call functions, get
 * current time, etc */
var twitchPlayer;

/**Holds the youtube player object when available. Used to call functions, get
 * current time, etc */
var youtubePlayer;

/**Holds the current video id that it's playing now. Used to save current time*/
var currentVideoId;

/**
 * Starts saving the current video time every 5 seconds until stopped.
 * To stop it, call stopSavingCurrentVideoTime()
 */
function triggerSaveCurrentVideoTime() {
    intervalSaveCurrentVideoTimeId = window.setInterval(function() {
        saveCurrentVideoTime();
    }, 5000);
}

/**
 * POSTs the /video/savetime url to save the current position of a video
 */
function saveCurrentVideoTime() {
    if(!twitchPlayer && !youtubePlayer) {
        return;
    }
    var currentTime;
    if(twitchPlayer) {
        currentTime = twitchPlayer.getVideoTime();
    }
    if(youtubePlayer) {
        currentTime = Math.round(youtubePlayer.getCurrentTime());
    }
    $.post(contextRoot + 'video/savetime', {
        'videoId' : currentVideoId,
        'playlistId' : playlistId,
        'currentTime' : currentTime,
        '_csrf' : csrfToken
    });
    $('a[video-id=' + currentVideoId + ']').attr('last-played', currentTime);
}

/**
 * Stops saving the current video time every 5 seconds
 */
function stopSavingCurrentVideoTime() {
    window.clearInterval(intervalSaveCurrentVideoTimeId);
}

/**
 * Plays the next video, closing the popup for the current video and opening the next
 */
function playNextVideo() {
    var nextVideo = $('a', $('.videos-list li a[video-id=' + currentVideoId + ']').parent().next());
    if(nextVideo) {
        $.magnificPopup.close()
        nextVideo.click();
    }
}

/**
 * The closePlayer() function is called by the popup plugin automatically. 
 * The function will also ask the plugin to close the popup, so there's a bit
 * of a loop there. So isClosingPlayer is used as a flag to prevent that.
 * Why the function also ask the plugin to close the popup? Because this function
 * is reused somewhere else (not only called by the popup plugin): when the user
 * hits the back button of the browse and the url goes back to list the playlists.
 */
var isClosingPlayer = false;
function closePlayer() {
    if(isClosingPlayer) {
        return;
    }
    isClosingPlayer = true;
    saveCurrentVideoTime(); //forces one last save
    stopSavingCurrentVideoTime();
    twitchPlayer = null;
    youtubePlayer = null;
    $.magnificPopup.close();
    history.pushState(null, null, contextRoot + 'playlist/' + playlistId);
    isClosingPlayer = false;
}

/**
 * Registers HTML5's popstate functions to auto open or close video when browser
 * url is changed.
 * When a video is opened or closed , the browser URL will be changed by other
 * functions (see loadTwitchVideos() and loadYoutubeVideos())
 */
function registerBrowseLocationChange() {
    window.setTimeout(function() {
        window.addEventListener("popstate", function(e) {
            // This checks if new url is to open a video
            var regExpPlayVideoUrl = new RegExp(contextRoot + "playlist/([0-9]+)/video/([0-9]+)");
            var matchPlayVideoUrl = location.pathname.match(regExpPlayVideoUrl);
            if (matchPlayVideoUrl){
                closePlayer();
                $('a[video-id=' + matchPlayVideoUrl[2] + ']').click();
            }
            // This checks if new url is to close a video (show playlist videos)
            var regExpShowPlaylist = new RegExp(contextRoot + "playlist/([0-9]+)$");
            var matchShowPlaylist = location.pathname.match(regExpShowPlaylist);
            if (matchShowPlaylist){
                // Note, when url is already winter/playlist/x, and user
                // clicks back button and goes back to same winter/playlist/x,
                // this method will get called anyway
                closePlayer();
            }
            
        }, false);
      }, 1);
}