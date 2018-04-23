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

    String username, token;
    private int userID;
    private boolean stayLogin;

    int currentUserID;

    @InjectView(R.id.loading_text) TextView loadingText;
    @InjectView(R.id.progressBar2) ProgressBar loadingProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.inject(this);

        access = getSharedPreferences("Login", MODE_PRIVATE);
        stayLogin = access.getBoolean("stayLogin", false);
        userID = access.getInt("userID", -1);


                if(stayLogin == true){
                    token = access.getString("token", "");
                    if(userID == -1){Log.e("SplashScreen user Id", "UserID == -1"); }
                    dologin();
                }
                else{
                    SharedPreferences.Editor editor = access.edit();
                    editor.clear();
                    editor.commit();
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
            jsonTest.put("function", "loginWithToken");
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
                            if(stayLogin == true){
                            Toast.makeText(SplashScreen.this, "Session expired", Toast.LENGTH_SHORT).show();}
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

