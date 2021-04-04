package de.michi.clashutils.google;

import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class SheetWriter {
    private String tableName;

    private String scope;

    private String sheetID;

    public SheetWriter(String sheetID, String scope, String tableName) {
        this.sheetID = sheetID;
        this.scope = scope;
        this.tableName = tableName;
    }

    public void write(String input) {
        List<List<Object>> values = Arrays.asList((List<Object>[]) new List[]{Arrays.asList(new Object[]{input})});
        ValueRange body = (new ValueRange()).setValues(values);
        try {
            GoogleSheetsAuthentication.createSheetsService().spreadsheets().values().update(this.sheetID, this.tableName + "!" + this.scope, body)
                    .setValueInputOption("RAW")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(List<List<Object>> values) {
        ValueRange body = (new ValueRange()).setValues(values);
        try {
            GoogleSheetsAuthentication.createSheetsService().spreadsheets().values().update(this.sheetID, this.tableName + "!" + this.scope, body)
                    .setValueInputOption("RAW")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRange(List<ValueRange> data) {
        BatchUpdateValuesRequest batchBody = (new BatchUpdateValuesRequest()).setValueInputOption("USER_ENTERED").setData(data);
        try {
            GoogleSheetsAuthentication.createSheetsService().spreadsheets().values()
                    .batchUpdate(this.sheetID, batchBody)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
