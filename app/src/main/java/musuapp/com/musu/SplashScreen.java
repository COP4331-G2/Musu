package musuapp.com.musu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SplashScreen extends AppCompatActivity {

    private static final String TAG = "SplashScreen";
    private static final int REQUEST_SIGNUP = 0;
    private static JSONObject connJSON;
    SharedPreferences access;

    String username, password;

    int currentUserID;

    @InjectView(R.id.loading_text) TextView loadingText;
    @InjectView(R.id.progressBar2) ProgressBar loadingProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.inject(this);

        access = getSharedPreferences("Login", MODE_PRIVATE);
        username = access.getString("username", "");


                if(!username.equals("")){
                    password = access.getString("password", "");
                    dologin();
                }
                else{
                    onLoginFailed();
                }

    }


    public  void login()
    {
        try {
            onLoginSuccess();

        }
        catch(Exception e)
        {
            Log.e("JSON", "JSON Error: "+e.toString());
        }
    }
    public void dologin() {
        Log.d(TAG, "Login");

        final databaseConnection conn = new databaseConnection();

        JSONObject jsonTest = new JSONObject();

        String serverName = getString(R.string.api_url);

        try {
            jsonTest.put("function", "loginAttempt");
            jsonTest.put("username", username);
            jsonTest.put("password", password);

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

                            SplashScreen.connJSON = new JSONObject(connResult);

                            Log.e("TEST (JSON result): ", connJSON.get("success").toString());

                            if (connJSON.get("success").toString() == "true") {
                                onLoginSuccess();
                            }
                            else{
                                onLoginFailed();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 2000
        );
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() throws JSONException {
        JSONObject jsonResult = (JSONObject) SplashScreen.connJSON.get("results");
        goToMain ((int) jsonResult.get("userID"));
    }

    public void onLoginFailed() {
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            goToMain(-1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 500
        );
    }

    public void goToMain(int userID){
        Log.i("ID",Integer.toString(userID));
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("userID", userID);
        startActivity(intent);
        finish();
    }
}

