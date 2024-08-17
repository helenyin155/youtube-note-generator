
function loadYouTubeAPI() {
    var tag = document.createElement('script');
    tag.src = 'https://www.youtube.com/iframe_api';
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
}

var player;

function onYouTubeIframeAPIReady() {
player = new YT.Player('videoFrame', {
    events: {
    'onReady': onPlayerReady
    }
});
}

function goToTime(time) {
player.seekTo(time);
player.playVideo();
}

function currentTimePercentage(){
    return player.getCurrentTime()/player.getDuration();
}
function calculateTimePercentage(time){
    return time/player.getDuration();
}

function returnTime(){
    return player.getCurrentTime();
}

// Function called by the YouTube iframe API when it's ready

// Function called when the YouTube player is ready
function onPlayerReady(event) {
    event.target.playVideo();
}

function getTitle(){
   return player.getVideoData().title;
}