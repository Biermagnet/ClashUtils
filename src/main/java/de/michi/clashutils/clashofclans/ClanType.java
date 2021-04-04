package de.michi.clashutils.clashofclans;

public enum ClanType {
    OPEN, CLOSED, INVITE_ONLY;

    protected static ClanType getClanTypeFromString(String type) {
        switch (type) {
            case "inviteOnly": return INVITE_ONLY;
            case "closed": return CLOSED;
            case "open": return OPEN;
            default: return null;
        }
    }
}
