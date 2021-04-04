package de.michi.clashutils.clashofclans;

import de.michi.clashutils.ClashUtils;
import de.michi.clashutils.exceptions.InvalidClanException;
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
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class ClanWar {

    private String tag;
    private String name;
    private int clanLevel;
    private String smallIconURL;
    private String mediumIconURL;
    private String largeIconURL;
    private int attacks;
    private int stars;
    private double destruction;
    private ZoneId zoneID;
    private ArrayList<WarPlayer> players;
    private HashMap<Integer, Integer> breakdown;
    private HashMap<Integer, Integer> amountAttacks;
    private HashMap<Integer, Integer> amountTripples;


    private String opponentTag;
    private String opponentName;
    private int opponentClanLevel;
    private String opponentSmallIconURL;
    private String opponentMediumIconURL;
    private String opponentLargeIconURL;
    private int opponentAttacks;
    private int opponentStars;
    private double opponentDestruction;
    private ArrayList<WarPlayer> opponentPlayers;
    private HashMap<Integer, Integer> opponentBreakdown;
    private HashMap<Integer, Integer> opponentAmountAttacks;
    private HashMap<Integer, Integer> opponentAmountTripples;


    private ClanWarState state;
    private int teamSize;
    private LocalDateTime prepStart;
    private LocalDateTime warStart;
    private LocalDateTime warEnd;


    public ClanWar(String clanTag) throws InvalidClanException {
        this.tag = clanTag.replace("#", "%23");

        String url = "https://api.clashofclans.com/v1/clans/" + this.tag + "/currentwar";
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss.SSS'Z'");
        players = new ArrayList<>();
        opponentPlayers = new ArrayList<>();

        breakdown = new HashMap<>();
        opponentBreakdown = new HashMap<>();
        amountAttacks = new HashMap<>();
        amountTripples = new HashMap<>();
        opponentAmountAttacks = new HashMap<>();
        opponentAmountTripples = new HashMap<>();
        for (int i = 3; i <= 13; i++) {
            breakdown.put(i, 0);
            opponentBreakdown.put(i, 0);
            amountAttacks.put(i, 0);
            amountTripples.put(i, 0);
            opponentAmountAttacks.put(i, 0);
            opponentAmountTripples.put(i, 0);
        }

        try {
            JSONObject obj = (JSONObject) parser.parse(responseString);
            this.state = ClanWarState.getClanWarStateFromString((String) obj.get("state"));
            this.teamSize = ((Long) obj.get("teamSize")).intValue();
            zoneID = ZoneId.of("UTC");
            this.prepStart = LocalDateTime.parse((String) obj.get("preparationStartTime"), dtf).atZone(zoneID).toLocalDateTime();
            this.warStart = LocalDateTime.parse((String) obj.get("startTime"), dtf).atZone(zoneID).toLocalDateTime();
            this.warEnd = LocalDateTime.parse((String) obj.get("endTime"), dtf).atZone(zoneID).toLocalDateTime();
            JSONObject clanObj = (JSONObject) obj.get("clan");
            this.tag = (String) clanObj.get("tag");
            this.name = (String) clanObj.get("name");
            this.clanLevel = ((Long) clanObj.get("clanLevel")).intValue();
            this.attacks = ((Long) clanObj.get("attacks")).intValue();
            this.stars = ((Long) clanObj.get("stars")).intValue();
            this.destruction = Math.round((Double) clanObj.get("destructionPercentage") * Math.pow(10, 2)) / Math.pow(10, 2);

            JSONObject badges = (JSONObject) clanObj.get("badgeUrls");
            this.smallIconURL = (String) badges.get("small");
            this.mediumIconURL = (String) badges.get("medium");
            this.largeIconURL = (String) badges.get("large");

            JSONArray players = (JSONArray) clanObj.get("members");

            for (int i = 0; i < teamSize; i++) {
                JSONObject player = (JSONObject) players.get(i);
                String playertag = (String) player.get("tag");
                String playername = (String) player.get("name");
                int townhall = ((Long) player.get("townhallLevel")).intValue();
                int mapPos = ((Long) player.get("mapPosition")).intValue();
                int defenses = ((Long) player.get("opponentAttacks")).intValue();
                JSONArray attacks = (JSONArray) player.get("attacks");
                int attackAmount = attacks == null ? 0 : attacks.size();

                addTownhall(townhall);

                Attack firstAttack = null;
                Attack secondAttack = null;
                if (attackAmount != 0) {
                    JSONObject firstAttackObject = (JSONObject) attacks.get(0);
                    String defenderTag = (String) firstAttackObject.get("defenderTag");
                    int stars = ((Long) firstAttackObject.get("stars")).intValue();
                    int destruction = ((Long) firstAttackObject.get("destructionPercentage")).intValue();
                    firstAttack = new Attack(defenderTag, stars, destruction);
                    addAttack(townhall);
                    if (stars == 3) addTripple(townhall);

                }
                if (attackAmount == 2) {
                    JSONObject secondAttackObject = (JSONObject) attacks.get(1);
                    String defenderTag = (String) secondAttackObject.get("defenderTag");
                    int stars = ((Long) secondAttackObject.get("stars")).intValue();
                    int destruction = ((Long) secondAttackObject.get("destructionPercentage")).intValue();
                    secondAttack = new Attack(defenderTag, stars, destruction);
                    addAttack(townhall);
                    if (stars == 3) addTripple(townhall);
                }

                JSONObject bestDefenseObject = (JSONObject) player.get("bestOpponentAttack");
                if (bestDefenseObject != null) {
                    String attackerTag = (String) bestDefenseObject.get("attackerTag");
                    int stars = ((Long) bestDefenseObject.get("stars")).intValue();
                    int destruction = ((Long) bestDefenseObject.get("destructionPercentage")).intValue();
                    Defense bestDefense = new Defense(attackerTag, stars, destruction);

                    WarPlayer warPlayer = new WarPlayer(playertag, playername, townhall, mapPos, firstAttack, secondAttack, bestDefense, defenses);
                    this.players.add(warPlayer);
                } else {
                    WarPlayer warPlayer = new WarPlayer(playertag, playername, townhall, mapPos, firstAttack, secondAttack, null, defenses);
                    this.players.add(warPlayer);
                }
            }


            JSONObject opponentClanObj = (JSONObject) obj.get("opponent");
            this.opponentTag = (String) opponentClanObj.get("tag");
            this.opponentName = (String) opponentClanObj.get("name");
            this.opponentClanLevel = ((Long) opponentClanObj.get("clanLevel")).intValue();
            this.opponentAttacks = ((Long) opponentClanObj.get("attacks")).intValue();
            this.opponentStars = ((Long) opponentClanObj.get("stars")).intValue();
            this.opponentDestruction = Math.round((Double) opponentClanObj.get("destructionPercentage") * Math.pow(10, 2)) / Math.pow(10, 2);

            JSONObject opponentBadges = (JSONObject) opponentClanObj.get("badgeUrls");
            this.opponentSmallIconURL = (String) opponentBadges.get("small");
            this.opponentMediumIconURL = (String) opponentBadges.get("medium");
            this.opponentLargeIconURL = (String) opponentBadges.get("large");

            JSONArray opponentPlayers = (JSONArray) opponentClanObj.get("members");

            for (int i = 0; i < teamSize; i++) {
                JSONObject player = (JSONObject) opponentPlayers.get(i);
                String playertag = (String) player.get("tag");
                String playername = (String) player.get("name");
                int townhall = ((Long) player.get("townhallLevel")).intValue();
                int mapPos = ((Long) player.get("mapPosition")).intValue();
                int defenses = ((Long) player.get("opponentAttacks")).intValue();
                JSONArray attacks = (JSONArray) player.get("attacks");
                int attackAmount = attacks == null ? 0 : attacks.size();
                addOpponentTownhall(townhall);

                Attack firstAttack = null;
                Attack secondAttack = null;
                if (attackAmount != 0) {
                    JSONObject firstAttackObject = (JSONObject) attacks.get(0);
                    String defenderTag = (String) firstAttackObject.get("defenderTag");
                    int stars = ((Long) firstAttackObject.get("stars")).intValue();
                    int destruction = ((Long) firstAttackObject.get("destructionPercentage")).intValue();
                    firstAttack = new Attack(defenderTag, stars, destruction);
                    addOpponentAttack(townhall);
                    if (stars == 3) addOpponentTripple(townhall);

                }
                if (attackAmount == 2) {
                    JSONObject secondAttackObject = (JSONObject) attacks.get(1);
                    String defenderTag = (String) secondAttackObject.get("defenderTag");
                    int stars = ((Long) secondAttackObject.get("stars")).intValue();
                    int destruction = ((Long) secondAttackObject.get("destructionPercentage")).intValue();
                    secondAttack = new Attack(defenderTag, stars, destruction);
                    addOpponentAttack(townhall);
                    if (stars == 3) addOpponentTripple(townhall);
                }

                JSONObject bestDefenseObject = (JSONObject) player.get("bestOpponentAttack");
                if (bestDefenseObject != null) {
                    String attackerTag = (String) bestDefenseObject.get("attackerTag");
                    int stars = ((Long) bestDefenseObject.get("stars")).intValue();
                    int destruction = ((Long) bestDefenseObject.get("destructionPercentage")).intValue();
                    Defense bestDefense = new Defense(attackerTag, stars, destruction);

                    WarPlayer warPlayer = new WarPlayer(playertag, playername, townhall, mapPos, firstAttack, secondAttack, bestDefense, defenses);
                    this.opponentPlayers.add(warPlayer);
                } else {
                    WarPlayer warPlayer = new WarPlayer(playertag, playername, townhall, mapPos, firstAttack, secondAttack, null, defenses);
                    this.opponentPlayers.add(warPlayer);
                }
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

    public int getClanLevel() {
        return clanLevel;
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

    public int getAttacks() {
        return attacks;
    }

    public int getStars() {
        return stars;
    }

    public double getDestruction() {
        return destruction;
    }

    public String getOpponentTag() {
        return opponentTag;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public int getOpponentClanLevel() {
        return opponentClanLevel;
    }

    public String getOpponentSmallIconURL() {
        return opponentSmallIconURL;
    }

    public String getOpponentMediumIconURL() {
        return opponentMediumIconURL;
    }

    public String getOpponentLargeIconURL() {
        return opponentLargeIconURL;
    }

    public int getOpponentAttacks() {
        return opponentAttacks;
    }

    public int getOpponentStars() {
        return opponentStars;
    }

    public double getOpponentDestruction() {
        return opponentDestruction;
    }

    public ClanWarState getState() {
        return state;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public LocalDateTime getPrepStart() {
        return prepStart;
    }

    public LocalDateTime getWarStart() {
        return warStart;
    }

    public LocalDateTime getWarEnd() {
        return warEnd;
    }

    public ArrayList<WarPlayer> getPlayers() {
        return players;
    }

    public ArrayList<WarPlayer> getOpponentPlayers() {
        return opponentPlayers;
    }

    public void setTimeZone(ZoneId id) {
        zoneID = id;
        prepStart = prepStart.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        warStart = warStart.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        warEnd = warEnd.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
    }

    public ZoneId getZoneID() {
        return zoneID;
    }

    public String getBreakdown() {

        String bd = "";
        int zero = 0;
        for (int i = 13; i > 3; i--) {
            int amount = breakdown.get(i);
            if (amount == 0) {
                zero++;
                continue;
            }
            for (int z = 0; z < zero; z++) {
                bd += "0/";
            }
            bd += amount + "/";
        }
        return bd.substring(0, bd.length() - 1);

    }

    public String getopponentBreakdown() {
        String bd = "";
        int zero = 0;
        for (int i = 13; i > 3; i--) {
            int amount = opponentBreakdown.get(i);
            if (amount == 0) {
                zero++;
                continue;
            }
            for (int z = 0; z < zero; z++) {
                bd += "0/";
            }
            bd += amount + "/";
        }
        return bd.substring(0, bd.length() - 1);

    }


    private void addTownhall(int thlevel) {
        int amount = breakdown.get(thlevel);
        amount = amount + 1;
        breakdown.remove(thlevel);
        breakdown.put(thlevel, amount);
    }

    private void addOpponentTownhall(int thlevel) {
        int amount = opponentBreakdown.get(thlevel);
        amount = amount + 1;
        opponentBreakdown.remove(thlevel);
        opponentBreakdown.put(thlevel, amount);
    }


    public int getRemainingPrepHours() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        return (int) now.until(prepStart, ChronoUnit.HOURS);
    }

    public int getRemainingPrepMinutes() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        return (int) now.until(prepStart, ChronoUnit.MINUTES) % 60;
    }

    public int getRemainingWarHours() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        return (int) now.until(warEnd, ChronoUnit.HOURS);
    }

    public int getRemainingWarMinutes() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        return (int) now.until(warEnd, ChronoUnit.MINUTES) % 60;
    }

    public int getWarEndDays() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        return (int) warEnd.until(now, ChronoUnit.DAYS);
    }

    public int getWarEndHours() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        return (int) warEnd.until(now, ChronoUnit.HOURS) % 24;
    }

    public int getWarEndMinutes() {
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC).atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneID).toLocalDateTime();
        return (int) warEnd.until(now, ChronoUnit.MINUTES) % 60;
    }

    public String getFormattedPrepDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return prepStart.format(formatter);
    }

    public String getFormattedWarStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return warStart.format(formatter);
    }

    public String getFormattedWarEndDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return warEnd.format(formatter);
    }

    public int getPrepHours() {
        return (int) prepStart.until(warStart, ChronoUnit.HOURS);
    }

    public int getPrepMinutes() {
        return (int) prepStart.until(warStart, ChronoUnit.MINUTES) % 60;
    }


    public int getWarHours() {
        return (int) warStart.until(warEnd, ChronoUnit.HOURS);
    }

    public int getWarMinutes() {
        return (int) warStart.until(warEnd, ChronoUnit.MINUTES) % 60;
    }

    private void addAttack(int townhall) {
        int currentHits = amountAttacks.get(townhall);
        currentHits += 1;
        amountAttacks.remove(townhall);
        amountAttacks.put(townhall, currentHits);
    }

    private void addTripple(int townhall) {
        int currentHits = amountTripples.get(townhall);
        currentHits += 1;
        amountTripples.remove(townhall);
        amountTripples.put(townhall, currentHits);
    }

    private void addOpponentAttack(int townhall) {
        int currentHits = opponentAmountAttacks.get(townhall);
        currentHits += 1;
        opponentAmountAttacks.remove(townhall);
        opponentAmountAttacks.put(townhall, currentHits);
    }

    private void addOpponentTripple(int townhall) {
        int currentHits = opponentAmountTripples.get(townhall);
        currentHits += 1;
        opponentAmountTripples.remove(townhall);
        opponentAmountTripples.put(townhall, currentHits);
    }

    public int getAttacks(int townhall) {
        return amountAttacks.get(townhall);
    }

    public int getTripples(int townhall) {
        return amountTripples.get(townhall);
    }

    public int getOpponentAttacks(int townhall) {
        return opponentAmountAttacks.get(townhall);
    }

    public int getOpponentTripples(int townhall) {
        return opponentAmountTripples.get(townhall);
    }

    public double getHitrate(int townhall) {
        int hits = amountAttacks.get(townhall);
        int tripples = amountTripples.get(townhall);

        double hitrate =  ((double)tripples / hits);
        return (int) Math.round((hitrate * Math.pow(10, 4)) / Math.pow(10, 4)*100);

    }

    public String getTripplesAsString(int townhall) {
        return amountTripples.get(townhall) + "/" + amountAttacks.get(townhall);
    }

    public String getOpponentTripplesAsString(int townhall) {
        return opponentAmountTripples.get(townhall) + "/" + opponentAmountAttacks.get(townhall);
    }

    public int getOpponentHitrate(int townhall) {
        int hits = opponentAmountAttacks.get(townhall);
        int tripples = opponentAmountTripples.get(townhall);
        double hitrate =  ((double)tripples / hits);
        return (int) Math.round((hitrate * Math.pow(10, 4)) / Math.pow(10, 4)*100);
    }

}
