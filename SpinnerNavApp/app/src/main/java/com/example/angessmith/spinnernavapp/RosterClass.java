package com.example.angessmith.spinnernavapp;

import java.io.Serializable;

// Created by AngeSSmith on 10/21/14.

public class RosterClass  implements Serializable{
    private String mTeamName;
    private String[] mTeamPlayers;
    private static final long SERIAL_VERSION_UID = 264352789266376484L;

    // Build factory
    public static RosterClass newInstance (String teamName, String[] teamPlayers) {
        RosterClass roster = new RosterClass();
        // set the variables
        roster.mTeamName = teamName;
        roster.mTeamPlayers = teamPlayers;
        return roster;
    }

    public RosterClass() {
        mTeamPlayers = new String[9];
        mTeamName = "";
    }

    // Build the getters
    public String[] getTeamPlayers() {
        return mTeamPlayers;
    }

    public String getTeamName() {
        return mTeamName;
    }

}
