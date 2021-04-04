package de.michi.clashutils.google;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleSheetsAuthentication {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private static final List<String> SCOPES = Collections.singletonList("https://www.googleapis.com/auth/spreadsheets");


    public static Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = GoogleSheetsAuthentication.class.getResourceAsStream("/credentials.json");
        if (in == null)
            throw new FileNotFoundException("Resource not found: /credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow = (new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                JSON_FACTORY, clientSecrets, SCOPES)).setDataStoreFactory(new FileDataStoreFactory(new File("tokens")))
                .setAccessType("offline").build();
        LocalServerReceiver receiver = (new LocalServerReceiver.Builder()).setPort(8888).build();
        return (new AuthorizationCodeInstalledApp(flow, receiver)).authorize("user");

    }


    public static Sheets createSheetsService() {
        NetHttpTransport httpTransport = null;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        try {
            return (new Sheets.Builder((HttpTransport) httpTransport, (JsonFactory) jacksonFactory, (HttpRequestInitializer) getCredentials(httpTransport)))
                    .setApplicationName("ClashUtils")
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
