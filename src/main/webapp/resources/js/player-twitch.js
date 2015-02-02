/**Holds the id of the infinite interval that verifies if a video ended so popup
 * can be closed and next video opened*/
var intervalVerifyIfTwitchVideoEndedId;

/**Holds the lenght of the current video, since for twitch we have to monitor 
 * when video ends (no event)*/
var currentTwitchVideoLength;

/**Holds the start time that should be applied as soon as the video loads. This
 * variable is supposed to change everytime a new video starts playing */
var currentTwitchVideoStartPlayTime;

/**
 * Keeps checking if Twitch video has ended, so the next video can start. There's
 * no "ENDED" callback event for twitch player so we have to check manually here.
 */
function triggerVerifyIfTwitchVideoEnded() {
    intervalVerifyIfTwitchVideoEndedId = window.setInterval(function() {
        if(!twitchPlayer) {
            window.clearInterval(intervalVerifyIfTwitchVideoEndedId);
        }
        if(twitchPlayer && currentTwitchVideoLength <= twitchPlayer.getVideoTime()) {
            playNextVideo();
            window.clearInterval(intervalVerifyIfTwitchVideoEndedId);
        }
    }, 1000);
}

/**
 * Callback function for the twitch player. When video starts, jumps to the last
 * position, also calls triggerSaveCurrentVideoTime()
 */
window.onTwitchPlayerOpenedEvent = function(data) {
    data.forEach(function(event) {
//        console.log("EVENT: %s", event.event);
//        console.log("DATA:", event.data);
        if (event.event == "playerInit") {
            twitchPlayer = $("#twitchpopup")[0];
        }
        if (event.event == "videoPlaying") {
            //twitchPlayer.playVideo();
            //twitchPlayer.mute();
            twitchPlayer.videoSeek(currentTwitchVideoStartPlayTime);
            triggerSaveCurrentVideoTime();
            triggerVerifyIfTwitchVideoEnded();
        }
    });
}

/**
 * Loads metadata for twitch videos and binds functions on twitch videos to
 * be played on click.
 * For each video, calls via JSONP twitch api to get video metadata such as 
 * thumbnail and title. Then binds for each video link to open a magnificPopup 
 * with a swf object binding it to a javascript object to be used to read 
 * statistics. The callback when twitch player is loaded is onTwitchPlayerOpenedEvent();
 * Finally, if finds the last played video of the playlist, starts it.
 */
function loadTwitchVideos() {
    $(".button-play-video:contains('twitch.tv')").each(function(index) {
        var videoURL = $(this).text();
        var videoID = getTwitchVideoID(videoURL);
        var internalVideoID = $(this).attr('video-id');
        var linkobj = this;
        // Calls twitch to get video info
        $.getJSON('https://api.twitch.tv/kraken/videos/'+ videoID + '?callback=?', function(data) {
            var videoLength = data.length;
            // Modifies HTML to have image, title, etc
            $(linkobj).text(data.title);
            var img = '<img src="'+ data.preview + '" height="100px" width="188px" />';
            $(linkobj).prepend(img);
            // If last played size is almost finishing video, come back to start
            if($(linkobj).attr('last-played') >= videoLength - 5) {
                $(linkobj).attr('last-played', '0');
            }
            // Binds a click event to the video link to open a popup and load TwitchPlayer
            $(linkobj).magnificPopup({
                                items : {
                                    src : '<div class="player-popup"><div id="twitchpopup">Loading video...</div></div>',
                                    type : 'inline',
                                    alignTop: true
                                },
                                mainClass: 'popup-player-video',
                                callbacks : {
                                    open : function() {
                                        currentTwitchVideoStartPlayTime = $(linkobj).attr('last-played');
                                        currentVideoId = internalVideoID;
                                        currentTwitchVideoLength = videoLength;
                                        history.pushState(null, null, $(linkobj).attr('href'));
                                        swfobject.embedSWF("http://www-cdn.jtvnw.net/swflibs/TwitchPlayer.swf",
                                                        "twitchpopup",
                                                        "100%",
                                                        "100%",
                                                        "11",
                                                        null,
                                                        {
                                                            "eventsCallback" : "onTwitchPlayerOpenedEvent",
                                                            "embed" : 1,
                                                            "videoId" : videoID,
                                                            "auto_play" : "true"
                                                        },
                                                        {
                                                            "allowScriptAccess" : "always",
                                                            "allowFullScreen" : "true"
                                                        });
                                    },
                                    close : function() {
                                        closePlayer();
                                    }
                                }
                            });
            if($(linkobj).attr('video-id')==lastPlayedVideoId || $('.videos-list li').length==1) {
                $(linkobj).click();
            }
        });
    });
}

/**
 * Returns a twitch video id from a URL
 * http://www.twitch.tv/dragon/v/3633912, returns v3633912
 */
function getTwitchVideoID(url){
    var regExp = /^.*twitch.tv\/.*?\/(.*)/;
    var match = url.match(regExp);
    if (match){
        return match[1].replace("/", "");
    }
}