package de.michi.clashutils.clashofclans;

public class ClanPlayer {


    private String tag;
    private String name;
    private ClanRole clanRole;
    private int level;
    private int trophies;
    private int versusTrophies;
    private int clanRank;
    private int previousClanRank;
    private int donations;
    private int donationsReceived;


    protected ClanPlayer(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }


    public ClanRole getClanRole() {
        return clanRole;
    }

    public void setClanRole(ClanRole clanRole) {
        this.clanRole = clanRole;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTrophies() {
        return trophies;
    }

    public void setTrophies(int trophies) {
        this.trophies = trophies;
    }

    public int getVersusTrophies() {
        return versusTrophies;
    }

    public void setVersusTrophies(int versusTrophies) {
        this.versusTrophies = versusTrophies;
    }

    public int getClanRank() {
        return clanRank;
    }

    public void setClanRank(int clanRank) {
        this.clanRank = clanRank;
    }

    public int getPreviousClanRank() {
        return previousClanRank;
    }

    public void setPreviousClanRank(int previousClanRank) {
        this.previousClanRank = previousClanRank;
    }

    public int getDonations() {
        return donations;
    }

    public void setDonations(int donations) {
        this.donations = donations;
    }

    public int getDonationsReceived() {
        return donationsReceived;
    }

    public void setDonationsReceived(int donationsReceived) {
        this.donationsReceived = donationsReceived;
    }
}
