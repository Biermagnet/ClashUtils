package de.michi.clashutils.clashofclans;


import de.michi.clashutils.ClashUtils;
import de.michi.clashutils.exceptions.InvalidPlayerException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class Player {

    private String playertag;
    private String playername;
    private int townhallLevel;
    private int expLevel;
    private int trophies;
    private int bestTrophies;
    private int warStars;
    private int attackWins;
    private int defenseWins;
    private int builderHallLevel;
    private int versusTrophies;
    private int bestVersusTrophies;
    private int versusBattleWins;
    private ClanRole clanRole;
    private int donations;
    private int donationsReceived;
    private String clanTag;
    private String clanName;
    private int clanLevel;
    private boolean hasClan;

    public Player(String playertag) throws InvalidPlayerException {
        this.playertag = playertag.replace("#", "%23");

        String url = "https://api.clashofclans.com/v1/players/" + this.playertag;
        StringBuffer response = null;

        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer " + ClashUtils.getDynamicSupercellToken().getToken());
            int responseCode = con.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response = new StringBuffer();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();
            } else if (responseCode == 404) {
                throw new InvalidPlayerException();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String responseString = response.toString();

        JSONParser parser = new JSONParser();

        try {
            JSONObject obj = (JSONObject) parser.parse(responseString);
            this.playertag = (String) obj.get("tag");
            playername = (String) obj.get("name");
            townhallLevel = ((Long) obj.get("townHallLevel")).intValue();
            expLevel = ((Long) obj.get("expLevel")).intValue();
            trophies = ((Long) obj.get("trophies")).intValue();
            bestTrophies = ((Long) obj.get("bestTrophies")).intValue();
            warStars = ((Long) obj.get("warStars")).intValue();
            attackWins = ((Long) obj.get("attackWins")).intValue();
            defenseWins = ((Long) obj.get("defenseWins")).intValue();
            builderHallLevel = ((Long) obj.get("builderHallLevel")).intValue();
            versusTrophies = ((Long) obj.get("versusTrophies")).intValue();
            versusBattleWins = ((Long) obj.get("versusBattleWins")).intValue();
            donations = ((Long) obj.get("townHallLevel")).intValue();
            townhallLevel = ((Long) obj.get("townHallLevel")).intValue();
            donations = ((Long) obj.get("donations")).intValue();
            donationsReceived = ((Long) obj.get("donationsReceived")).intValue();

            JSONObject clanObj = (JSONObject) obj.get("clan");
            try {
                clanName = (String) clanObj.get("name");
                clanTag = (String) clanObj.get("tag");
                clanLevel = ((Long) clanObj.get("clanLevel")).intValue();
                clanRole = ClanRole.getClanRoleFromString((String) obj.get("role"));
                hasClan = true;
            } catch (Exception e) {
                hasClan = false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getTag() {
        return playertag;
    }

    public String getName() {
        return playername;
    }

    public int getTownhallLevel() {
        return townhallLevel;
    }

    public int getExpLevel() {
        return expLevel;
    }

    public int getTrophies() {
        return trophies;
    }

    public int getBestTrophies() {
        return bestTrophies;
    }

    public int getWarStars() {
        return warStars;
    }

    public int getAttackWins() {
        return attackWins;
    }

    public int getDefenseWins() {
        return defenseWins;
    }

    public int getBuilderHallLevel() {
        return builderHallLevel;
    }

    public int getVersusTrophies() {
        return versusTrophies;
    }

    public int getBestVersusTrophies() {
        return bestVersusTrophies;
    }

    public int getVersusBattleWins() {
        return versusBattleWins;
    }

    public ClanRole getClanRole() {
        return clanRole;
    }

    public int getDonations() {
        return donations;
    }

    public int getDonationsReceived() {
        return donationsReceived;
    }

    public String getClanTag() {
        return clanTag;
    }

    public String getClanName() {
        return clanName;
    }

    public int getClanLevel() {
        return clanLevel;
    }

    public boolean hasClan() {
        return hasClan;
    }
}