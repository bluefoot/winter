
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
    if(currentTime != 0) {
        $.post(contextRoot + 'video/savetime', {
            'videoId' : currentVideoId,
            'playlistId' : playlistId,
            'currentTime' : currentTime
        });
        $('span[data-video-id=' + currentVideoId + ']').attr('data-last-played', currentTime);
    }
}

/**
 * Stops saving the current video time every 5 seconds
 */
function stopSavingCurrentVideoTime() {
    window.clearInterval(intervalSaveCurrentVideoTimeId);
}

/**
 * Plays the next video
 */
function playNextVideo() {
    var nextVideo = $('a', $('.video-title[data-video-id=' + currentVideoId + ']').parent('li'));
    if(nextVideo) {
        nextVideo.click();
    }
}
