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
        if(twitchPlayer && currentTwitchVideoLength <= twitchPlayer.getCurrentTime()) {
            playNextVideo();
            if(!isMobile) {
                $('.wrapperscrollmain .tse-scroll-content').scrollTop($('li.current-playing-video').offset().top);
            }
            window.clearInterval(intervalVerifyIfTwitchVideoEndedId);
        }
    }, 1000);
}

/**
 * Loads metadata for twitch videos and binds functions on twitch videos to
 * be played on click.
 * For each video, calls via JSONP twitch api to get video metadata such as 
 * thumbnail and title. Then binds for each video link to open a magnificPopup 
 * with a swf object binding it to a javascript object to be used to read 
 * statistics. The callback when twitch player is loaded is onTwitchPlayerOpenedEvent();
 * Finally, if finds the last played video of the playlist, starts it.
 * 
 * https://github.com/justintv/Twitch-API/blob/master/embed-video.md
 */

function loadTwitchVideos() {
    $(".button-play-video:contains('twitch.tv')").each(function(index) {
        var videoURL = $(this).text();
        var videoID = getTwitchVideoID(videoURL);
        var internalVideoID = $(this).attr('data-video-id');
        var linkobj = this;
        // Calls twitch to get video info
        $.getJSON('https://api.twitch.tv/kraken/videos/'+ videoID + '?callback=?', function(data) {
            var videoLength = data.length;
            // Modifies HTML to have image, title, etc
            $(linkobj).text(data.title);
            var img = '<img src="'+ data.preview + '" height="100px" width="188px" />';
            $(linkobj).prepend(img);
            // If last played size is almost finishing video, come back to start
            if($(linkobj).attr('data-last-played') >= videoLength - 5) {
                $(linkobj).attr('data-last-played', '0');
            }
            $(linkobj).click(function(event){
                closePlayer();
                event.stopPropagation();
                var width = $("#video-player-container").width();
                var height = width / 1.77777;
                // Height is calculated to force 16:9 aspect ratio, but won't go over 80% of the screen
                if(height > $(window).height() * .8) {
                    height = $(window).height() * .8;
                    width = height * 1.77777;
                }
                var options = {
                        width: "100%", // or width
                        height: height + "px",
                        video: videoID
                    };
                var player = new Twitch.Player("video-player-container", options);
                player.addEventListener(Twitch.Player.READY, function(){
                    currentTwitchVideoStartPlayTime = $(linkobj).attr('data-last-played');
                    currentVideoId = internalVideoID;
                    currentTwitchVideoLength = videoLength;
                    history.pushState(null, null, $(linkobj).attr('href'));
                    $('li.current-playing-video').removeClass('current-playing-video');
                    $(linkobj).parents('li').addClass('current-playing-video');
                    player.seek(currentTwitchVideoStartPlayTime);
                    player.play();
                    twitchPlayer = player;
                    triggerSaveCurrentVideoTime();
                    triggerVerifyIfTwitchVideoEnded();
                });
                player.addEventListener(Twitch.Player.ENDED, function(){
                    closePlayer();
                    playNextVideo();
                });
                return false;
            });
            if(isAutoPlayEnabled && 
                    ((videoToPlay == '' && $(linkobj).attr('data-video-id')==lastPlayedVideoId) || 
                    $('.videos-list li').length==1 ||
                    $(linkobj).attr('data-video-id')==videoToPlay)) {
                $(linkobj).click();
                $('.wrapperscrollmain .tse-scroll-content').scrollTop($('li.current-playing-video').offset().top);
            }
        });
    });
    
    // Mobile
        $(".video-title:contains('twitch.tv')").each(function(index) {
        var videoURL = $(this).text();
        var videoID = getTwitchVideoID(videoURL);
        var internalVideoID = $(this).attr('data-video-id');
        var spanVideoDesc = this;
        $(spanVideoDesc).parents('li').append('<a class="video-disabled"></a>');
        // Calls twitch to get video info
        $.getJSON('https://api.twitch.tv/kraken/videos/'+ videoID + '?callback=?', function(data) {
            var videoLength = data.length;
            // Modifies HTML to have image, title, etc
            $(spanVideoDesc).text(data.title);
            $('.img', $(spanVideoDesc).parent('a')).css('background-image', 'url(' + data.preview + ')').css('background-size', 'cover');
            // If last played size is almost finishing video, come back to start
            if($(spanVideoDesc).attr('data-last-played') >= videoLength - 5) {
                $(spanVideoDesc).attr('data-last-played', '0');
            }
            // Binds a click event to the video link to open a popup and load TwitchPlayer
            $('.video-disabled', $(spanVideoDesc).parents('li')).bind('click', function(e){
                $('#popupmsg').text('Twitch VODs not supported on mobile. Hopefully Twitch will support HTML5 VODs soon™!');
            }).attr('href', '#popupmsg').attr('data-rel', 'popup');
        });
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
 * @deprecated Now twitch supports HTML5 player for past broadcasts, this is no longer used
 */
function loadTwitchVideosFlash() {
    $(".button-play-video:contains('twitch.tv')").each(function(index) {
        var videoURL = $(this).text();
        var videoID = getTwitchVideoID(videoURL);
        var internalVideoID = $(this).attr('data-video-id');
        var linkobj = this;
        // Calls twitch to get video info
        $.getJSON('https://api.twitch.tv/kraken/videos/'+ videoID + '?callback=?', function(data) {
            var videoLength = data.length;
            // Modifies HTML to have image, title, etc
            $(linkobj).text(data.title);
            var img = '<img src="'+ data.preview + '" height="100px" width="188px" />';
            $(linkobj).prepend(img);
            // If last played size is almost finishing video, come back to start
            if($(linkobj).attr('data-last-played') >= videoLength - 5) {
                $(linkobj).attr('data-last-played', '0');
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
                                        currentTwitchVideoStartPlayTime = $(linkobj).attr('data-last-played');
                                        currentVideoId = internalVideoID;
                                        currentTwitchVideoLength = videoLength;
                                        history.pushState(null, null, $(linkobj).attr('href'));
                                        $('li.current-playing-video').removeClass('current-playing-video');
                                        $(linkobj).parents('li').addClass('current-playing-video');
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
            if(isAutoPlayEnabled && 
                    ((videoToPlay == '' && $(linkobj).attr('data-video-id')==lastPlayedVideoId) || 
                    $('.videos-list li').length==1 ||
                    $(linkobj).attr('data-video-id')==videoToPlay)) {
                $(linkobj).click();
                $('.wrapperscrollmain .tse-scroll-content').scrollTop($('li.current-playing-video').offset().top);
            }
        });
    });
    
    // Mobile
        $(".video-title:contains('twitch.tv')").each(function(index) {
        var videoURL = $(this).text();
        var videoID = getTwitchVideoID(videoURL);
        var internalVideoID = $(this).attr('data-video-id');
        var spanVideoDesc = this;
        $(spanVideoDesc).parents('li').append('<a class="video-disabled"></a>');
        // Calls twitch to get video info
        $.getJSON('https://api.twitch.tv/kraken/videos/'+ videoID + '?callback=?', function(data) {
            var videoLength = data.length;
            // Modifies HTML to have image, title, etc
            $(spanVideoDesc).text(data.title);
            $('.img', $(spanVideoDesc).parent('a')).css('background-image', 'url(' + data.preview + ')').css('background-size', 'cover');
            // If last played size is almost finishing video, come back to start
            if($(spanVideoDesc).attr('data-last-played') >= videoLength - 5) {
                $(spanVideoDesc).attr('data-last-played', '0');
            }
            // Binds a click event to the video link to open a popup and load TwitchPlayer
            $('.video-disabled', $(spanVideoDesc).parents('li')).bind('click', function(e){
                $('#popupmsg').text('Twitch VODs not supported on mobile. Hopefully Twitch will support HTML5 VODs soon™!');
            }).attr('href', '#popupmsg').attr('data-rel', 'popup');
        });
    });
}

/**
 * Returns a twitch video id from a URL
 * http://www.twitch.tv/dragon/v/3633912, returns v3633912
 * http://www.twitch.tv/wcs/b/625294920, returns a625294920 (b for some reason 
 * only works if replaced by a. go ask twitch about it, no documentation whatsoever)
 */
function getTwitchVideoID(url){
    var regExp = /^.*twitch.tv\/.*?\/(.*)/;
    var match = url.match(regExp);
    if (match){
        var videoId = match[1].replace("/", "");
        if(videoId.charAt(0)=='b') {
            videoId = 'a' + videoId.substr(1);
        }
        return videoId;
    }
}