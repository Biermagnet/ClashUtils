package de.michi.clashutils.google;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.List;

public class SheetReader {
    private String tableName;

    private String scope;

    private String sheetID;

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public SheetReader(String sheetID, String scope, String tableName) {
        this.sheetID = sheetID;
        this.scope = scope;
        this.tableName = tableName;
    }

    public SheetReader(String sheetID, String tableName) {
        this.sheetID = sheetID;
        this.tableName = tableName;
    }

    public List<List<Object>> read() {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            String range = this.tableName + "!" + this.scope;
            Sheets service = (new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleSheetsAuthentication.getCredentials(HTTP_TRANSPORT))).setApplicationName("KarmaBot").build();
            ValueRange response = service.spreadsheets().values().get(this.sheetID, range).execute();
            List<List<Object>> values = response.getValues();
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<List<Object>> readSheet() {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Sheets service = (new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, GoogleSheetsAuthentication.getCredentials(HTTP_TRANSPORT))).setApplicationName("KarmaBot").build();
            ValueRange response = service.spreadsheets().values().get(this.sheetID, this.tableName).execute();
            List<List<Object>> values = response.getValues();
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
