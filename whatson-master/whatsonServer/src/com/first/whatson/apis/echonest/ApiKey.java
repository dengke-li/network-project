package com.first.whatson.apis.echonest;

public class ApiKey {
	public static final String APIKEY = "NG6LOLINXBHFJY9J6";
}

//list of all the genres
/*
"http://developer.echonest.com/api/v4/genre/list?api_key=NG6LOLINXBHFJY9J6&format=json"
//*/

//list of n top artists in the given genre
/*
http://developer.echonest.com/api/v4/genre/artists?api_key=NG6LOLINXBHFJY9J6&format=json&results=10&bucket=hotttnesss&name=rock
//*/

//doc pour cette api: http://developer.echonest.com/docs/v4/genre.html

//doc pour générer une playlist selon un genre:
// http://developer.echonest.com/docs/v4/basic.html

// ex
// http://developer.echonest.com/api/v4/playlist/static?api_key=NG6LOLINXBHFJY9J6&genre=jazz&genre=blues&format=json&results=20&type=genre-radio
// http://developer.echonest.com/api/v4/playlist/basic?api_key=NG6LOLINXBHFJY9J6&artist=Weezer&format=json&results=20&type=artist-radio
// http://developer.echonest.com/api/v4/playlist/basic?api_key=NG6LOLINXBHFJY9J6&song_id=SOHTZUF12A8C13582B&format=json&results=20&type=song-radio

//Make the playlist with getting spotify track ids and other details
//http://developer.echonest.com/api/v4/playlist/static?api_key=NG6LOLINXBHFJY9J6&genre=acid+techno&format=json&bucket=id%3Aspotify-WW&bucket=tracks&limit=true&variety=0.2&dmca=true&results=12&type=genre-radio
//against 
// http://developer.echonest.com/api/v4/playlist/static?api_key=NG6LOLINXBHFJY9J6&genre=acid+techno&format=json&results=12&type=genre-radio
//embedded spotify playlist
//https://embed.spotify.com/?uri=spotify:trackset:GENRE%20playlist:idtrack1,idtrack2,...

