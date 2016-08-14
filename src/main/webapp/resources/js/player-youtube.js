/**Google api v3 key*/
var gkey = 'AIzaSyDB0_Z2fziu128Qci8coL6XfeAaSARM-YM';
/**
 * https://developers.google.com/api-client-library/javascript/
 */
function onLoadFn() {
    gapi.client.setApiKey(gkey);
}
gapi.load("client", onLoadFn);

function exportToYoutube() {
	alert("to be implemented");
}
/**
 * Converts ISO 8601 date returned by youtube to seconds.
 * http://stackoverflow.com/a/22762313
 */
function convertIso8601ToSeconds(input) {
    var reptms = /^PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?$/;
    var hours = 0, minutes = 0, seconds = 0, totalseconds;
    
    if (reptms.test(input)) {
      var matches = reptms.exec(input);
      if (matches[1]) hours = Number(matches[1]);
      if (matches[2]) minutes = Number(matches[2]);
      if (matches[3]) seconds = Number(matches[3]);
      return totalseconds = hours * 3600  + minutes * 60 + seconds;
    }
    return 0;
}

/**
 * Loads youtube api first, since api loads async and we need to wait for it first.
 * The callback, onYouTubeIframeAPIReady(), is called automatically by the API
 */
function loadYoutubeVideos() {
    var tag = document.createElement('script');
    tag.src = "https://www.youtube.com/iframe_api";
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
}

/**
 * Loads metadata for youtube videos and binds functions on youtube videos to
 * be played on click. Called as a callback of loadYoutubeVideos();
 */
function onYouTubeIframeAPIReady() {
    $(".button-play-video:contains('youtube.com')").each(function(index) {
        var videoURL = $(this).text();
        var videoID = getYoutubeVideoID(videoURL);
        var internalVideoID = $(this).attr('data-video-id');
        var linkobj = this;
        // Calls youtube to get video info
        $.getJSON('https://www.googleapis.com/youtube/v3/videos?id=' + videoID + '&key=' + gkey + '&part=snippet,contentDetails&callback=?', function(data) {
            if (typeof (data.items[0]) != "undefined") {
                // Modifies HTML to have image, title, etc
                $(linkobj).text(data.items[0].snippet.title);
                var img = '<img src="' + data.items[0].snippet.thumbnails.medium.url + '" height="100px" width="188px" />';
                $(linkobj).prepend(img);
                // If last played size is almost finishing video, come back to start
                if($(linkobj).attr('data-last-played') >= convertIso8601ToSeconds(data.items[0].contentDetails.duration) - 5) {
                    $(linkobj).attr('data-last-played', '0');
                }
                $(linkobj).magnificPopup({
                    items : {
                        src : '<div class="player-popup"><div id="youtubepopup">Loading video...</div></div>',
                        type : 'inline'
                    },
                    mainClass: 'popup-player-video',
                    callbacks : {
                        open : function() {
                            currentVideoId = internalVideoID;
                            history.pushState(null, null, $(linkobj).attr('href'));
                            $('li.current-playing-video').removeClass('current-playing-video');
                            $(linkobj).parents('li').addClass('current-playing-video');
                            youtubePlayer = new YT.Player('youtubepopup', {
                                height: '100%',
                                width: '100%',
                                videoId: videoID,
                                playerVars: {
                                    'start': $(linkobj).attr('data-last-played')
                                },
                                events: {
                                  'onReady': onYoutubePlayerReady,
                                  'onStateChange': onYoutubePlayerStateChange
                                }
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
            } else {
                $(linkobj).text('video not exists');
            }
        });
    });
    
    // Mobile
    
    $(".video-title:contains('youtube.com')").each(function(index) {
    var videoURL = $(this).text();
    var videoID = getYoutubeVideoID(videoURL);
    var internalVideoID = $(this).attr('data-video-id');
    var spanVideoDesc = this;
    // Calls youtube to get video info
    $.getJSON('https://www.googleapis.com/youtube/v3/videos?id=' + videoID + '&key=' + gkey + '&part=snippet,contentDetails&callback=?', function(data) {
        if (typeof (data.items[0]) != "undefined") {
            // Modifies HTML to have image, title, etc
            $(spanVideoDesc).text(data.items[0].snippet.title);
            $('.img', $(spanVideoDesc).parent('a')).css('background-image', 'url(' + data.items[0].snippet.thumbnails.medium.url + ')').css('background-size', 'cover');
            // If last played size is almost finishing video, come back to start
            if($(spanVideoDesc).attr('data-last-played') >= convertIso8601ToSeconds(data.items[0].contentDetails.duration) - 5) {
                $(spanVideoDesc).attr('data-last-played', '0');
            }
            $(spanVideoDesc).parent('a').bind('click', function(e){
                if(youtubePlayer) {
                    youtubePlayer.destroy();
                }
                currentVideoId = $('span.video-title', this).attr('data-video-id');
                $('.current-playing').removeClass('current-playing');
                $(this).parent('div.cell-wmobile').addClass('current-playing');
                youtubePlayer = new YT.Player('video-placeholder', {
                    height: '150px',
                    width: '100%',
                    videoId: videoID,
                    playerVars: {
                        'start': $(spanVideoDesc).attr('data-last-played')
                    },
                    events: {
                      'onReady': onYoutubePlayerReady,
                      'onStateChange': onYoutubePlayerStateChange
                    }
                  });
                e.preventDefault();
            });
            if(isAutoPlayEnabled && (
                    internalVideoID==lastPlayedVideoId || // if this is the last played video from before, play it 
                    $('.playlists-wmobile-grid li').length==1 || // or if this is the only single video in the playlist
                    (lastPlayedVideoId=='' && $('div.cell-wmobile.current-playing').length==0) // or if there's no last played video, and nothing else is playing. in mobile, there should be always a video loaded
                  )
                ){
                $(spanVideoDesc).parent('a').click();
            }
        } else {
            $(spanVideoDesc).text('video not exist');
        }
    });
  });
}


/**
 * Called when youtube player is ready to be played. Starts playing and starts
 * saving the current state to the server every 5 seconds.
 * @param event
 */
function onYoutubePlayerReady(event) {
	if(!isMobile) {
		// Can't autoplay on mobile. https://developers.google.com/youtube/iframe_api_reference#Mobile_considerations
	    event.target.playVideo();
    } else {
        $.mobile.silentScroll($('span[data-video-id=' + currentVideoId + ']').parents('li').position().top-$('.header-wmobile').height());
    }
    triggerSaveCurrentVideoTime();
}

/**
 * Callback for when youtube player changes status. We have an ended status, so 
 * we can go ahead and play the next video. (Twitch doesn't have that so there's 
 * a function to keep checking manually if the video ended).
 */
function onYoutubePlayerStateChange(event) {
    if(event.data == YT.PlayerState.ENDED) {
        if(document.webkitExitFullscreen) {
            document.webkitExitFullscreen();
        }
        if(document.mozCancelFullscreen) {
            document.mozCancelFullscreen();
        }
        playNextVideo();
        if(!isMobile) {
            $('.wrapperscrollmain .tse-scroll-content').scrollTop($('li.current-playing-video').offset().top);
        }
     }
}

/**
 * Returns a youtube video id from a URL
 * http://lasnv.net/foro/839/Javascript_parsear_URL_de_YouTube
 */
function getYoutubeVideoID(url) {
    var regExp = /^.*((youtu.be\/)|(v\/)|(\/u\/\w\/)|(embed\/)|(watch\?))\??v?=?([^#\&\?]*).*/;
    var match = url.match(regExp);
    if (match && match[7].length == 11) {
        return match[7];
    } else {
        return "Invalid video URL: " + url;
    }
}
