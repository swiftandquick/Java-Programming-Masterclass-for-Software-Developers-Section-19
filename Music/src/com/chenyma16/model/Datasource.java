package com.chenyma16.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {

    public static final String DB_NAME = "music.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";
    public static final int INDEX_ALBUM_ID = 1;
    public static final int INDEX_ALBUM_NAME = 2;
    public static final int INDEX_ALBUM_ARTIST = 3;

    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";
    public static final int INDEX_SONG_ID = 1;
    public static final int INDEX_SONG_TRACK = 2;
    public static final int INDEX_SONG_TITLE = 3;
    public static final int INDEX_SONG_ALBUM = 4;

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_ALBUMS_BY_ARTIST_START =
            "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " FROM " + TABLE_ALBUMS + " INNER JOIN " +
                    TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS +
                    "." + COLUMN_ARTIST_ID + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + " = \"";

    public static final String QUERY_ALBUMS_BY_ARTIST_SORT =
            " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " COLLATE NOCASE ";

    public static final String QUERY_ARTIST_FOR_SONG_START =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME +
                    ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK + " FROM " + TABLE_SONGS + " INNER JOIN " +
                    TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." +
                    COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST
                    + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " WHERE " + TABLE_SONGS + "." +
                    COLUMN_SONG_TITLE + " = \"";

    public static final String QUERY_ARTIST_FOR_SONG_SORT =
            " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME
                    + " COLLATE NOCASE ";

    public static final String TABLE_ARTIST_SONG_VIEW = "artist_list";
    public static final String CREATE_ARTIST_FOR_SONG_VIEW =
            "CREATE VIEW IF NOT EXISTS " + TABLE_ARTIST_SONG_VIEW + " AS SELECT " + TABLE_ARTISTS + "." +
                    COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + " AS " + COLUMN_SONG_ALBUM +
                    ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE +
                    " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." +
                    COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " INNER JOIN " + TABLE_ARTISTS
                    + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." +
                    COLUMN_ARTIST_ID + " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS +
                    "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    public static final String QUERY_VIEW_SONG_INFO =
            "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " +
                    TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONG_TITLE + " = \"";

    /* When we run the query, we replace the placeholder “?” with an actual title.  */
    public static final String QUERY_VIEW_SONG_INFO_PREP =
            "SELECT " + COLUMN_ARTIST_NAME + ", " + COLUMN_SONG_ALBUM + ", " + COLUMN_SONG_TRACK + " FROM " +
                    TABLE_ARTIST_SONG_VIEW + " WHERE " + COLUMN_SONG_TITLE + " = ?";

    public static final String INSERT_ARTIST =
            "INSERT INTO " + TABLE_ARTISTS + '(' + COLUMN_ARTIST_NAME + ") VALUES(?)";

    public static final String INSERT_ALBUMS =
            "INSERT INTO " + TABLE_ALBUMS + '(' + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";

    public static final String INSERT_SONGS =
            "INSERT INTO " + TABLE_SONGS + '(' + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " +
                    COLUMN_SONG_ALBUM + ") VALUES(?, ?, ?)";

    public static final String QUERY_ARTIST =
            "SELECT " + COLUMN_ARTIST_ID + " FROM " + TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";

    public static final String QUERY_ALBUM =
            "SELECT " + COLUMN_ALBUM_ID + " FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";

    public static final String QUERY_SONG =
            "SELECT " + COLUMN_SONG_ID + " FROM " + TABLE_SONGS + " WHERE " + COLUMN_SONG_TITLE + " = ?";

    private Connection conn;

    /* Prepared Statement:  An object that represents a precompiled SQL statement.  */
    private PreparedStatement querySongInfoView;

    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;;
    private PreparedStatement insertIntoSongs;

    private PreparedStatement queryArtist;
    private PreparedStatement queryAlbum;
    private PreparedStatement querySong;


    /* Connect to the database.  */
    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            /* Prepare a statement or set a parameter.  */
            querySongInfoView = conn.prepareStatement(QUERY_VIEW_SONG_INFO_PREP);
            /* Prepare a statement and fetch the key after automatic generation.  */
            insertIntoArtists = conn.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = conn.prepareStatement(INSERT_ALBUMS, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = conn.prepareStatement(INSERT_SONGS);
            queryArtist = conn.prepareStatement(QUERY_ARTIST);
            queryAlbum = conn.prepareStatement(QUERY_ALBUM);
            querySong = conn.prepareStatement(QUERY_SONG);
            return true;
        }
        catch(SQLException e) {
            System.out.println("Couldn't connect to databse:  "  + e.getMessage());
            return false;
        }
    }


    /* Close the connection.  */
    public void close() {
        try {
            if(querySongInfoView != null) {
                querySongInfoView.close();
            }
            if(insertIntoArtists != null) {
                insertIntoArtists.close();
            }
            if(insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }
            if(insertIntoSongs != null) {
                insertIntoSongs.close();
            }
            if(queryArtist != null) {
                queryArtist.close();
            }
            if(queryAlbum != null) {
                queryAlbum.close();
            }
            if(querySong != null) {
                queryAlbum.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
        catch(SQLException e) {
            System.out.println("Couldn't close the connection:  " + e.getMessage());
        }
    }


    public List<Artist> queryArtists(int sortOrder) {

        /* Decides whether I should order by artists by the name in ascending, descending, or no order.  */
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        if(sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_ARTIST_NAME);
            sb.append(" COLLATE NOCASE ");
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            }
            else {
                sb.append("ASC");
            }
        }

        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sb.toString())) {
            /* Retrieve the list of artists from the database, and store it to artists (List).  */
            List<Artist> artists = new ArrayList<Artist>();
            while(results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }
            return artists;
        }
        catch(SQLException e) {
            System.out.println("Query failed:  " + e.getMessage());
            return null;
        }
    }


    public List<String> queryAlbumsForArtist(String artistName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ALBUMS_BY_ARTIST_START);
        sb.append(artistName);
        sb.append("\"");

        /* Order by albums in natural, reverse natural, or no order.  */
        if(sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ALBUMS_BY_ARTIST_SORT);
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            }
            else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL statement = " + sb.toString());

        try (Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sb.toString())) {
            List <String> albums = new ArrayList<String>();
            while(results.next()) {
                /* We are only get the album name.  */
                albums.add(results.getString(1));
            }
            return albums;
        }
        catch(SQLException e) {
            System.out.println("Query failed:  " + e.getMessage());
            return null;
        }

    }


    public List<SongArtist> queryArtistsForSong(String songName, int sortOrder) {
        StringBuilder sb = new StringBuilder(QUERY_ARTIST_FOR_SONG_START);
        sb.append(songName);
        sb.append("\"");

        if(sortOrder != ORDER_BY_NONE) {
            sb.append(QUERY_ARTIST_FOR_SONG_SORT);
            if(sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            }
            else {
                sb.append("ASC");
            }
        }

        System.out.println("SQL Statement:  " + sb.toString());

        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sb.toString())) {
            List<SongArtist> songArtists = new ArrayList<SongArtist>();

            while(results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }

            return songArtists;
        }
        catch(SQLException e) {
            System.out.println("Query failed:  " + e.getMessage());
            return null;
        }
    }


    public void querySongsMetadata() {
        String sql = "SELECT * FROM " + TABLE_SONGS;

        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql)) {
            /* ResultSetMetaData:  An object that can be used to get information about the types and properties of the
            columns in a ResultSet object.  */
            ResultSetMetaData meta = results.getMetaData();
            /* Returns the number of columns in this ResultSet object.  */
            int numColumns = meta.getColumnCount();
            for(int i = 1; i <= numColumns; i++) {
                /* getColumnName():  Gets the column name of a specified index.  */
                System.out.format("Column %d in the songs table is names %s\n", i, meta.getColumnName(i));
            }
        }
        catch(SQLException e) {
            System.out.println("Query failed:  " + e.getMessage());
        }
    }


    public int getCount(String table) {
        String sql = "SELECT COUNT(*) AS count FROM  " + table;
        try(Statement statement = conn.createStatement();
            ResultSet results = statement.executeQuery(sql)) {
            /* Get the number of items in the table.  */
            int count = results.getInt("count");

            System.out.format("Count = %d\n", count);
            return count;
        }
        catch(SQLException e) {
            System.out.println("Query failed:  " + e.getMessage());
            return -1;
        }
    }


    public boolean createViewForSongArtists() {
        try(Statement statement = conn.createStatement()) {
            /* Create a view called "artist_list".  */
            statement.execute(CREATE_ARTIST_FOR_SONG_VIEW);
            return true;
        }
        catch(SQLException e) {
            System.out.println("Create View failed:  " + e.getMessage());
            return false;
        }
    }


    public List<SongArtist> querySongInfoView(String title) {
        try {
            /* querySongInfoView is a PreparedStatement object.
            * setString():  Sets the first (1) placeholder (?) to title (String) in QUERY_VIEW_SONG_INFO_PREP (String).  */
            querySongInfoView.setString(1, title);
            /* Execute the PreparedStatement object and store the results in ResultSet.  */
            ResultSet results = querySongInfoView.executeQuery();
            List<SongArtist> songArtists = new ArrayList<SongArtist>();
            while(results.next()) {
                SongArtist songArtist = new SongArtist();
                songArtist.setArtistName(results.getString(1));
                songArtist.setAlbumName(results.getString(2));
                songArtist.setTrack(results.getInt(3));
                songArtists.add(songArtist);
            }
            return songArtists;
        }
        catch(SQLException e) {
            System.out.println("Query failed:  " + e.getMessage());
            return null;
        }
    }


    private int insertArtist(String name) throws SQLException {
        /* Check if the artist is in the artists based on name.  */
        queryArtist.setString(1, name);
        ResultSet results = queryArtist.executeQuery();
        /* If it does exist in the artists table, return the _id of the artist from artists table.  */
        if(results.next()) {
            return results.getInt(1);
        }
        else {
            /* Insert the artist.  */
            insertIntoArtists.setString(1, name);
            /* Executes the SQL statement in this PreparedStatement object, which must be an SQL Data Manipulation
            Language (DML) statement, such as INSERT, UPDATE or DELETE; or an SQL statement that returns nothing, such
            as a DDL statement.  Store the returned value as int (amount of rows updated).  Since we are inserting
            one item, only one row should be affected, so affectedRows should be 1.  */
            int affectedRows = insertIntoArtists.executeUpdate();

            /* Affected rows must be one to be able to insert artists.  */
            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert artist!  ");
            }

            /* Get the generated keys.  */
            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if(generatedKeys.next()) {
                /* Return the new _id for the artist.  */
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Couldn't get _id for artist.  ");
            }
        }
    }


    private int insertAlbum(String name, int artistId) throws SQLException {
        /* Check if the album is in the albums based on name.  */
        queryAlbum.setString(1, name);
        ResultSet results = queryAlbum.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        }
        else {
            /* Insert the album.  */
            insertIntoAlbums.setString(1, name);
            insertIntoAlbums.setInt(2, artistId);
            int affectedRows = insertIntoAlbums.executeUpdate();

            /* Affected rows must be one to be able to insert artists.  */
            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert album!  ");
            }

            /* Get the generated keys.  */
            ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
            if(generatedKeys.next()) {
                /* Return the new _id for the album.  */
                return generatedKeys.getInt(1);
            }
            else {
                throw new SQLException("Couldn't get _id for album.  ");
            }
        }
    }


    public void insertSong(String title, String artist, String album, int track) {
        try {
            /* Before insert anything, set auto commit to false so it doesn't permanently change the database.  */
            conn.setAutoCommit(false);

            /* Insert artist first.  */
            int artistId = insertArtist(artist);

            /* Then, insert the album.  */
            int albumId = insertAlbum(album, artistId);

            /* Check if the song is in the albums based on name.  */
            querySong.setString(1, title);
            ResultSet results = querySong.executeQuery();

            if(results.next()) {
                /* If song exists, exit out of the method.  */
                return;
            }
            else {
                /* Replace placeholders with values.  Set the first placeholder (?) to an int value (track).  */
                insertIntoSongs.setInt(1, track);
                insertIntoSongs.setString(2, title);
                insertIntoSongs.setInt(3, albumId);

                /* Insert into songs.  */
                int affectedRows = insertIntoSongs.executeUpdate();

                /* Affected rows must be one to be able to insert artists.  */
                if(affectedRows == 1) {
                /* Makes all changes made since the previous commit / rollback permanent and releases any database
                 locks currently held by this Connection object.  */
                    conn.commit();
                }
                else {
                    throw new SQLException("The song insert failed.  ");
                }
            }
        }
        catch(Exception e) {
            System.out.println("Insert song exception:  " + e.getMessage());
            try {
                /* Roll back the transaction if insertion doesn't go through.  Undoes all changes made in the current
                transaction and releases any database locks currently held by this Connection object.  */
                System.out.println("Performing rollback.  ");
                conn.rollback();
            }
            catch(SQLException e2) {
                System.out.println("Oh boy!  Things are really bad!  " + e2.getMessage());
            }
        }
        finally {
            try {
                System.out.println("Resetting default commit behavior.  ");
                /* Reset auto-commit to true.  */
                conn.setAutoCommit(true);
            }
            catch(SQLException e) {
                System.out.println("Couldn't reset auto-commit!  " + e.getMessage());
            }
        }
    }

}
