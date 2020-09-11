package com.alexandr7035.skills10000.data;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;

public class SkillStatHelper {

    private Context context;

    private String fileName;
    private ExecutorService executor;
    private FileOutputStream writer;

    private JSONObject statJSON;

    private final String LOG_TAG = "DEBUG_10000";

    public SkillStatHelper(String fileName, Context context) {

        this.context = context;
        this.fileName = fileName;

        if (! new File(context.getFilesDir().getAbsolutePath() + File.separator + fileName).exists()) {
            Log.d(LOG_TAG, "file doesn't exist");
            writeStatToFile("{}");
        }

        // Init json object
        try {
            statJSON = new JSONObject(getStatFromFile());
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    // Write JSON string to file
    private void writeStatToFile(String json_str) {


        try {
            writer = context.openFileOutput(fileName, Context.MODE_PRIVATE);

            writer.write(json_str.getBytes());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // Load JSON string from file
    private String getStatFromFile() {

        String stringData = "";

        try {
            InputStream inputStream = context.openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receivedString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receivedString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receivedString);
                }

                inputStream.close();
                stringData = stringBuilder.toString();

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringData;

    }


    public void updateStat(int hoursChange) {

        long date = System.currentTimeMillis() / 1000;

        String dateStr = DateFormat.format("yyyyMMdd", date*1000).toString();

        Log.d(LOG_TAG, "update stat " + dateStr);

        try {
            if (! statJSON.has(dateStr)) {
                statJSON.put(dateStr, hoursChange);
            }
            else {

                int previousVal = (int) statJSON.get(dateStr);

                statJSON.remove(dateStr);
                statJSON.put(dateStr, previousVal + hoursChange);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG, "updated stat " + statJSON.toString());
        writeStatToFile(statJSON.toString());

    }


    public int getTodayHours() {

        long curr_date = System.currentTimeMillis() / 1000;
        String curr_date_str = DateFormat.format("yyyyMMdd", curr_date*1000).toString();
        int today_hours = 0;

        try {
            today_hours = (Integer) statJSON.get(String.valueOf(curr_date_str));
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return today_hours;
    }

}
