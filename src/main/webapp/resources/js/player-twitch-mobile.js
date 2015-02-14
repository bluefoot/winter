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
 */
function getTwitchVideoID(url){
    var regExp = /^.*twitch.tv\/.*?\/(.*)/;
    var match = url.match(regExp);
    if (match){
        return match[1].replace("/", "");
    }
}