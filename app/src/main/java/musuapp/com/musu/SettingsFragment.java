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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import musuapp.com.musu.R;

import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.makeText;

public class SettingsFragment extends Fragment {

    Button logoutButton;
    View inflatedView;
    SharedPreferences access;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.inflatedView = inflater.inflate(R.layout.settings_fragment, container, false);

        final TextView _firstN = inflatedView.findViewById(R.id.FirstName);
        final TextView _lastN = inflatedView.findViewById(R.id.LastName);
        final TextView _userN = inflatedView.findViewById(R.id.Username);
        final TextView _pass = inflatedView.findViewById(R.id.Password);
        final TextView _verifyPass = inflatedView.findViewById(R.id.ConfirmPassword);
        final TextView _Email =  inflatedView.findViewById(R.id.Email);
        final Button _btn_SubmitChanges  = inflatedView.findViewById(R.id.Submit_Changes);

        _btn_SubmitChanges.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String firstname = _firstN.getText().toString();
                String lastname = _lastN.getText().toString();
                String username = _userN.getText().toString();
                String password = _pass.getText().toString();
                String verify = _verifyPass.getText().toString();
                String email = _Email.getText().toString();

                if(!verify.equals(password))
                {
                    Toast toast = Toast.makeText(getContext(),"Passwords do not match", Toast.LENGTH_LONG);
                    toast.show();
                    return;

                }

                SharedPreferences access = getActivity().getSharedPreferences("Login", MODE_PRIVATE);
                String userID = Integer.toString(access.getInt("userID",  -9));
                String token = access.getString("token", "");

                final databaseConnection conn = new databaseConnection();
                String serverName = getString(R.string.api_url);
                JSONObject jsonTest = new JSONObject();

                try {
                    jsonTest.put("function", "updateUser");
                    jsonTest.put("firstName", firstname);
                    jsonTest.put("lastName", lastname);
                    jsonTest.put("username", username);
                    jsonTest.put("password", password);
                    jsonTest.put("emailAddress", email);
                    jsonTest.put("token", token);
                    jsonTest.put("userID", userID);

                    Log.e("TEST (JSON payload): ", jsonTest.toString());

                    conn.execute(serverName, jsonTest.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            try {
                                String connResult = conn.get();

                                JSONObject connJSON = new JSONObject(connResult);

                                String successResult = new String();
                                successResult = connJSON.get("success").toString().trim();

                                Log.e("TEST (JSON result): ", successResult);

                                if (successResult.equals("true")) {
                                    Toast toast = Toast.makeText(getContext(), "Change made successfully", Toast.LENGTH_LONG);
                                    toast.show();
                                } else {
                                    Toast toast = Toast.makeText(getContext(), "Problem when trying to make change", Toast.LENGTH_LONG);
                                    toast.show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 2000
                );

            }
        });
        return this.inflatedView;
    }


}