package com.pia.tchittchat;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Pia on 10/20/2017.
 */

public class ConnectionManager {

    HttpURLConnection urlConnection = null;
    public InputStream getConnection (){
        try {
            URL url = new URL("https://training.loicortola.com/chat-rest/1.0/connect/PiaMonge/piamonge");
            urlConnection = (HttpURLConnection) url.openConnection();
           // return readInputStream(urlConnection.getInputStream());
        }  catch (MalformedURLException e1) {
            e1.printStackTrace();
        }catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

}
