package de.michi.clashutils.clashofclans;

public enum ClanRole {
    MEMBER, ELDER, CO_LEADER, LEADER;

    public static ClanRole getClanRoleFromString(String role) {
        switch (role) {
            case "coLeader":
                return ClanRole.CO_LEADER;
            case "admin":
                return ClanRole.ELDER;
            case "member":
                return ClanRole.MEMBER;
            case "leader":
                return ClanRole.LEADER;
            default:
                return null;
        }
    }
}
