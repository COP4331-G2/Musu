package musuapp.com.musu;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static JSONObject connJSON;
    private int sessionUserID;
    private String sessionUserName;
    private String sessionToken;
    private SharedPreferences access;
    private SharedPreferences.Editor editor;

    int currentUserID;

    @InjectView(R.id.input_username) EditText _usernameText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    @InjectView(R.id.keep_login) CheckBox _keepMeLogin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);


        _loginButton  = findViewById(R.id.btn_login);
        _passwordText = findViewById(R.id.input_password);
        _usernameText    = findViewById(R.id.input_username);
        _signupLink   = findViewById(R.id.link_signup);
        _keepMeLogin  = findViewById(R.id.keep_login);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dologin();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
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

        if (!validate()) {
            onLoginFailed();
            return;
        }

        // This needs to be fixed
        // Should re-enable upon failed login (timer?)
        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        final databaseConnection conn = new databaseConnection();

        JSONObject jsonTest = new JSONObject();

        String serverName = getString(R.string.api_url);

        try {
            jsonTest.put("function", "loginWithUsername");
            jsonTest.put("username", email);
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

                        LoginActivity.connJSON = new JSONObject(connResult);

                        Log.e("TEST (JSON result): ", connJSON.get("success").toString());

                        if (connJSON.get("success").toString() == "true") {
                            JSONObject jsonArray = (JSONObject)  connJSON.get("results");
                            sessionUserID = jsonArray.getInt("userID");
                            sessionUserName = jsonArray.getString("username");
                            sessionToken = jsonArray.getString("token");

                            onLoginSuccess();
                        } else {
                            onLoginFailed();
                            //onLoginSuccess();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    progressDialog.dismiss();
                }
            }, 2000
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() throws JSONException {
        _loginButton.setEnabled(true);

        access = getSharedPreferences("Login", MODE_PRIVATE);
        editor = access.edit();
        editor.putInt("userID", sessionUserID);
        editor.putString("token", sessionToken);
        editor.commit();

        keepLogin();

        Intent intent = new Intent();
        JSONObject jsonResult = (JSONObject) LoginActivity.connJSON.get("results");
        intent.putExtra("userID", (int) jsonResult.get("userID"));

        setResult(RESULT_OK, intent);

        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String username = _usernameText.getText().toString();
        String password = _passwordText.getText().toString();

        if (username.isEmpty()) {
            _usernameText.setError(null);
        }

        if (password.isEmpty()) {
            _passwordText.setError("password can't be empty");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void keepLogin(){
        boolean checked = _keepMeLogin.isChecked();
        if(checked) {
            //

            access = getSharedPreferences("Login", MODE_PRIVATE);
            editor = access.edit();
            editor.putBoolean("stayLogin", true);
            editor.commit();
        }
    }
}
