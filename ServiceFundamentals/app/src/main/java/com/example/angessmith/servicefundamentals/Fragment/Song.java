package com.example.angessmith.servicefundamentals.Fragment;

// Created by AngeSSmith on 10/28/14.

import java.io.Serializable;

public class Song implements Serializable {

    private long mSongId;
    private String mSongTitle;
    private String mSongArtist;
    // create the id for the serializable object
    private static final long serialVersionUID = 264352789266376484L;

    // Build the factory
    public static Song newInstance (long _songId, String _songTitle, String _songArtist) {
        Song song = new Song();
        song.mSongId = _songId;
        song.mSongTitle = _songTitle;
        song.mSongArtist = _songArtist;
        return song;
    }

    // Build the getters and setters
    public long getSongId() {
        return mSongId;
    }

    public String getSongTitle() {
        return mSongTitle;
    }

    public String getSongArtist() {
        return mSongArtist;
    }

}
