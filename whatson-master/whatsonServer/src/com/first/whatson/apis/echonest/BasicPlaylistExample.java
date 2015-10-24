/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.first.whatson.apis.echonest;
import com.echonest.api.v4.BasicPlaylistParams;
import com.echonest.api.v4.BasicPlaylistParams.PlaylistType;
import com.echonest.api.v4.EchoNestAPI;
import com.echonest.api.v4.EchoNestException;
import com.echonest.api.v4.Playlist;
import com.echonest.api.v4.Song;

/**
 *
 * @author plamere
 */
public class BasicPlaylistExample {

    public static void main(String[] args) throws EchoNestException {
        EchoNestAPI en = new EchoNestAPI(ApiKey.APIKEY);

        BasicPlaylistParams params = new BasicPlaylistParams();
//        params.addArtist("Bob Marley");
//        params.add("genre","rock");
//        PlaylistType type = PlaylistType.SONG_RADIO;
//        params.setType(type);
        params.setResults(10);
        params.set("genre", "rock");
        params.set("type", "genre-radio");
        Playlist playlist = en.createBasicPlaylist(params);

        for (Song song : playlist.getSongs()) {
            System.out.println(song.toString());
        }
    }
}
