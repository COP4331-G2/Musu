package musuapp.com.musu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import musuapp.com.musu.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    Button logoutButton;
    View inflatedView;
    SharedPreferences access;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflatedView = inflater.inflate(R.layout.settings_fragment, container, false);
        access = getActivity().getPreferences(MODE_PRIVATE);
        return this.inflatedView;
    }


}