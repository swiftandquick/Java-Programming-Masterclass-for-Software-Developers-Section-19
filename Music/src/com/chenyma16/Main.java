package com.chenyma16;

import com.chenyma16.model.Artist;
import com.chenyma16.model.Datasource;
import com.chenyma16.model.Song;
import com.chenyma16.model.SongArtist;
import org.w3c.dom.ls.LSOutput;

import javax.sql.DataSource;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Datasource datasource = new Datasource();
        if(!datasource.open()) {
            System.out.println("Can't open datasource.  ");
            return;
        }

        List<Artist> artists = datasource.queryArtists(Datasource.ORDER_BY_ASC);
        if(artists == null) {
            System.out.println("No artists!  ");
            return;
        }

        for(Artist artist : artists) {
            System.out.println("ID = " + artist.getId() + ", Name = " + artist.getName());
        }

        /* Get a specific artist's albums in natural order.  */
        List<String> albumsForArtist = datasource.queryAlbumsForArtist("Carole King", Datasource.ORDER_BY_ASC);

        for(String album : albumsForArtist) {
            System.out.println(album);
        }

        /* Input is song name, output is artist name, album name, and track.  */
        List<SongArtist> songArtists = datasource.queryArtistsForSong("Go Your Own Way", Datasource.ORDER_BY_ASC);
        if(songArtists == null) {
            System.out.println("Couldn't find the artist for the song.  ");
            return;
        }

        for(SongArtist artist : songArtists) {
            System.out.println("Artist name = " + artist.getArtistName() +
                    " Album name = " + artist.getAlbumName() +
                    " Track = " + artist.getTrack());
        }

        /* Get the column names of the songs table.  */
        datasource.querySongsMetadata();

        int count = datasource.getCount(Datasource.TABLE_SONGS);
        System.out.println("Number of songs is:  " + count);

        /* Create "artist_list" view.  */
        datasource.createViewForSongArtists();

        /*
        // "Go Your Own Way" is one of the song.
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a song title:  ");
        String title = scanner.nextLine();

        // Retrieve items from the "artist_list" view base on song's title.
        songArtists = datasource.querySongInfoView(title);
        if(songArtists.isEmpty()) {
            System.out.println("Couldn't find the artist for the song.  ");
            return;
        }

        for(SongArtist artist : songArtists) {
            System.out.println("FROM VIEW - Artist name = " + artist.getArtistName() +
                                " Album name = " + artist.getAlbumName() +
                                " Track number = " + artist.getTrack());
        }
         */

        /* Insert a song into songs table, artists and albums tables also change.  */
        datasource.insertSong("Doge", "Everly Brothers", "All-Time Greatest Hits", 7);

        datasource.close();
    }
}
