package musuapp.com.musu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import musuapp.com.musu.R;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        databaseTest();

        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    public void databaseTest() {
        JSONObject jsonTest = new JSONObject();

        String serverName = "https://renrokusmall.herokuapp.com/API/API.php";

        try {
            jsonTest.put("function", "loginAttempt");
            jsonTest.put("username", "Amuro");
            jsonTest.put("password", "test");

            Log.e("TEST (JSON payload): ", jsonTest.toString());

            new MusuTest().execute(serverName, jsonTest.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class MusuTest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String data = "";

            HttpURLConnection httpURLConnection = null;
            try {

                httpURLConnection = (HttpURLConnection) new URL(params[0]).openConnection();
                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoOutput(true);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(params[1]);
                wr.flush();
                wr.close();

                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);

                int inputStreamData = inputStreamReader.read();
                while (inputStreamData != -1) {
                    char current = (char) inputStreamData;
                    inputStreamData = inputStreamReader.read();
                    data += current;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("TEST (JSON result): ", result); // this is expecting a response code to be sent from your server upon receiving the POST data
        }
    }
}