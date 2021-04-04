package de.michi.clashutils.clashofclans;

import de.michi.clashutils.ClashUtils;
import de.michi.clashutils.exceptions.InvalidClanException;
import de.michi.clashutils.exceptions.InvalidPlayerException;
import org.joda.time.Hours;
import org.json.simple.JSONArray;
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
import java.util.ArrayList;

public class Clan {


    private String tag;
    private String name;
    private ClanType type;
    private String description;
    private String locationName;
    private int clanLevel;
    private int clanPoints;
    private int clanVersusPoints;
    private int requiredTrophies;
    private int warWinStreak;
    private int warWins;
    private int warTies;
    private int warLosses;
    private boolean publicWarLog;
    private int memberAmount;
    private String smallIconURL;
    private String mediumIconURL;
    private String largeIconURL;
    private ArrayList<ClanPlayer> players = new ArrayList<>();


    public Clan(String clanTag) throws InvalidClanException {
        this.tag = clanTag.replace("#", "%23");

        String url = "https://api.clashofclans.com/v1/clans/" + this.tag;
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
                throw new InvalidClanException();
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
            this.tag = (String) obj.get("tag");
            this.name = (String) obj.get("name");
            this.type = ClanType.getClanTypeFromString((String) obj.get("tag"));
            this.description = (String) obj.get("description");
            this.clanLevel = ((Long) obj.get("clanLevel")).intValue();
            this.clanPoints = ((Long) obj.get("clanPoints")).intValue();
            this.clanVersusPoints = ((Long) obj.get("clanVersusPoints")).intValue();
            this.requiredTrophies = ((Long) obj.get("requiredTrophies")).intValue();
            this.warWinStreak = ((Long) obj.get("warWinStreak")).intValue();
            this.warWins = ((Long) obj.get("warWins")).intValue();
            this.warTies = ((Long) obj.get("warTies")).intValue();
            this.warLosses = ((Long) obj.get("warLosses")).intValue();
            this.memberAmount = ((Long) obj.get("members")).intValue();
            this.publicWarLog = (boolean) obj.get("isWarLogPublic");

            JSONObject locationObj = (JSONObject) obj.get("location");
            this.locationName = (String) locationObj.get("name");

            JSONObject badges = (JSONObject) obj.get("badgeUrls");
            this.smallIconURL = (String) badges.get("small");
            this.mediumIconURL = (String) badges.get("medium");
            this.largeIconURL = (String) badges.get("large");

            JSONArray playerArray = (JSONArray) obj.get("memberList");
            for (int i = 0; i < playerArray.size(); i++) {
                JSONObject playerObj = (JSONObject) playerArray.get(i);
                ClanPlayer cp = new ClanPlayer((String) playerObj.get("tag"), (String) playerObj.get("name"));
                cp.setClanRole(ClanRole.getClanRoleFromString((String) playerObj.get("role")));
                cp.setLevel(((Long) playerObj.get("expLevel")).intValue());
                cp.setTrophies(((Long) playerObj.get("trophies")).intValue());
                cp.setVersusTrophies(((Long) playerObj.get("versusTrophies")).intValue());
                cp.setClanRank(((Long) playerObj.get("clanRank")).intValue());
                cp.setPreviousClanRank(((Long) playerObj.get("previousClanRank")).intValue());
                cp.setClanRank(((Long) playerObj.get("clanRank")).intValue());
                cp.setDonations(((Long) playerObj.get("donations")).intValue());
                cp.setDonationsReceived(((Long) playerObj.get("donationsReceived")).intValue());
                players.add(cp);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public ClanType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getLocationName() {
        return locationName;
    }

    public int getClanLevel() {
        return clanLevel;
    }

    public int getClanPoints() {
        return clanPoints;
    }

    public int getClanVersusPoints() {
        return clanVersusPoints;
    }

    public int getRequiredTrophies() {
        return requiredTrophies;
    }

    public int getWarWinStreak() {
        return warWinStreak;
    }

    public int getWarWins() {
        return warWins;
    }

    public int getWarTies() {
        return warTies;
    }

    public int getWarLosses() {
        return warLosses;
    }

    public boolean isPublicWarLog() {
        return publicWarLog;
    }

    public int getMemberAmount() {
        return memberAmount;
    }

    public ArrayList<ClanPlayer> getPlayers() {
        return players;
    }

    public String getSmallIconURL() {
        return smallIconURL;
    }

    public String getMediumIconURL() {
        return mediumIconURL;
    }

    public String getLargeIconURL() {
        return largeIconURL;
    }

}
