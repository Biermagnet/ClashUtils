package de.michi.clashutils.clashofclans;

public enum ClanWarState {
    NULL, PREPARATION, IN_WAR, ENDED;

    public static ClanWarState getClanWarStateFromString(String state) {
        switch (state) {
            case "preparation":
                return PREPARATION;
            case "inWar":
                return IN_WAR;
            case "warEnded":
                return ENDED;
            default:
                return NULL;
        }
    }

}
