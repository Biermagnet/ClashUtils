package de.michi.clashutils.clashofclans;

public class Attack {

    private String defenderTag;
    private int stars;
    private int destruction;

    public Attack(String defenderTag, int stars, int destruction) {
        this.defenderTag = defenderTag;
        this.stars = stars;
        this.destruction = destruction;
    }

    public String getDefenderTag() {
        return defenderTag;
    }

    public int getStars() {
        return stars;
    }

    public int getDestruction() {
        return destruction;
    }

    public boolean threeStar() {
        return stars == 3;
    }

    public boolean twoStar() {
        return stars == 2;
    }

    public boolean oneStar() {
        return stars == 1;
    }

    public boolean zeroStar() {
        return stars == 0;
    }
}
