package de.michi.clashutils.clashofclans;

import de.michi.clashutils.ClashUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.List;

public class DynamicSupercellToken {

    private String email;
    private String password;
    private String sessionCookie;
    private String token;

    public DynamicSupercellToken(String email, String password) {
        this.email = email;
        this.password = password;
        this.sessionCookie = "";
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return this.token;
    }

    public String getNewKey() {
        this.token = createKey();
         return this.token;
    }

    public String createKey() {
        sendLoginRequest();
        deleteAllKeys();
        String newKey;

        String result = sendCreateKeyRequest();
        JSONParser parser = new JSONParser();
        try {
            JSONObject obj = (JSONObject) parser.parse(result);
            JSONObject authObj = (JSONObject) obj.get("key");
            newKey = (String) authObj.get("key");
            ClashUtils.getLogger().success("Successfully created new token.");
            this.token = newKey;
            return newKey;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void deleteAllKeys() {
        try {
            URL url = new URL("https://developer.clashofclans.com/api/apikey/list");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonString.toString());
            JSONArray keys = (JSONArray) obj.get("keys");
            for (int i = 0; i < keys.size(); i++) {
                JSONObject key = (JSONObject) keys.get(i);
                deleteKey((String) key.get("id"));
            }
        } catch (Exception e) {
            ClashUtils.getLogger().warn("Could not delete all supercell tokens: " + e.getMessage());
        }
    }

    private void deleteKey(String key) {

        try {
            URL url = new URL("https://developer.clashofclans.com/api/apikey/revoke");
            String payload = "{\"id\":\"" + key + "\"}";
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private String sendCreateKeyRequest() {
        URL whatismyip;
        String ip;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = in.readLine();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            ClashUtils.getLogger().error("Could not get own IP from aws.");
            return null;
        }

        String payload = "{\"name\":\"KarmaBot Key\"," +
                "\"description\": \"KarmaBot OP\"," +
                "\"cidrRanges\": \"" + ip + "\", \"scopes\":\"\"}";
        String requestUrl = "https://developer.clashofclans.com/api/apikey/create";
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("cookie", this.sessionCookie);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        } catch (Exception e) {
            ClashUtils.getLogger().error("Could not create new token: " + e.getMessage());
            throw new RuntimeException(e.getMessage());

        }

    }

    private String sendLoginRequest() {
        String requestUrl = "https://developer.clashofclans.com/api/login";
        String payload = "{\"email\":\"" + email + "\", \"password\": \"" + this.password + "\"}";
        try {
            URL url = new URL(requestUrl);
            CookieManager manager = new CookieManager();
            manager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(manager);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();

            CookieStore cookieJar = manager.getCookieStore();
            List<HttpCookie> cookies =
                    cookieJar.getCookies();
            for (HttpCookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("cookie")) {
                    this.sessionCookie = cookie.getValue();
                    break;
                }
            }
            connection.disconnect();
            return jsonString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
