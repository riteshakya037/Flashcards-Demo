package com.sorcery.flashcards.Helper;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Ritesh Shakya on 8/23/2016.
 */

public class DownloadMp3Async extends AsyncTask<String, String, String> {


    private Context context;
    String firebaseLink;
    private String TAG = "DownloadMp3Async";

    public DownloadMp3Async(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... args) {
        int count;

        try {
            firebaseLink = args[0];
            URL url = new URL(firebaseLink);
            URLConnection connection = url.openConnection();
            connection.connect();
            String outputDirectory = context.getCacheDir() + File.separator + substringBetween(firebaseLink, "mp3Storage%2", "?alt=");
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(outputDirectory);

            byte data[] = new byte[1024];

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            return outputDirectory;
        } catch (Exception ignored) {
            return null;
        }

    }

    private String substringBetween(String mainString, String leftString, String rightString) {
        return mainString.substring(mainString.indexOf(leftString) + leftString.length(), mainString.indexOf(rightString));
    }

    @Override
    protected void onPostExecute(String cacheLink) {
        if (cacheLink != null) {
            DatabaseContract.DbHelper dbHelper = new DatabaseContract.DbHelper(context);
            dbHelper.onInsert(firebaseLink, cacheLink);
        }
    }

}