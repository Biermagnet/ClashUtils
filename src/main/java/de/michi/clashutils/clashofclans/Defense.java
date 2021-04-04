package de.michi.clashutils.clashofclans;

public class Defense {

    private String attackerTag;
    private int stars;
    private int destruction;

    public Defense(String attackerTag, int stars, int destruction) {
        this.attackerTag = attackerTag;
        this.stars = stars;
        this.destruction = destruction;
    }

    public String getAttackerTag() {
        return attackerTag;
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
