package com.gmail.vanyadubik.gitsearch.common;

public final class Consts {

    // Synchronization
    public final static int STATUS_STARTED_SYNC = 0;
    public final static int STATUS_FINISHED_SYNC = 1;
    public final static int STATUS_ERROR_SYNC = -1;

    // Settings
    public final static int CONNECT_TIMEOUT_SECONDS_RETROFIT = 180;
    public final static String CONNECT_SERVER_URL = "https://api.github.com/";
    public final static String CONNECT_PATTERN_URL = "search/repositories";

    // DB
    public final static String NAME_DATA_BASE_ROOM = "gitsearch-db";


}
