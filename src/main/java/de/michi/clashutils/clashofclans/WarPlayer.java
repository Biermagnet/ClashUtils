package de.michi.clashutils.clashofclans;


import java.util.ArrayList;

public class WarPlayer {

    private int townhall;
    private String name;
    private String tag;
    private int mapPos;
    private Attack firstAttack;
    private Attack secondAttack;
    private Defense bestDefense;
    private int defenses;


    protected WarPlayer(String tag, String name, int townhall, int mapPos, Attack firstAttack, Attack secondAttack, Defense bestDefense, int defenses) {
        this.name = name;
        this.tag = tag;
        this.townhall = townhall;
        this.mapPos = mapPos;
        this.bestDefense = bestDefense;
        this.firstAttack = firstAttack;
        this.secondAttack = secondAttack;
        this.defenses = defenses;
    }

    public String getName() {
        return this.name;
    }

    public String getTag() {
        return this.tag;
    }

    public int getMapPos() {
        return this.mapPos;
    }

    public int getTownhall() {
        return this.townhall;
    }

    public Attack getFirstAttack() {
        return firstAttack;
    }

    public Attack getSecondAttack() {
        return secondAttack;
    }

    public Defense getBestDefense() {
        return bestDefense;
    }

    public int defensives() {
        return defenses;
    }

    public boolean gotAttacked() {
        return bestDefense == null;
    }
}