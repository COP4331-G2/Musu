package musuapp.com.musu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static JSONObject connJSON;

    int currentUserID;

    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        _loginButton  = findViewById(R.id.btn_login);
        _passwordText = findViewById(R.id.input_password);
        _emailText    = findViewById(R.id.input_email);
        _signupLink   = findViewById(R.id.link_signup);

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

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        final databaseConnection conn = new databaseConnection();

        JSONObject jsonTest = new JSONObject();

        String serverName = getString(R.string.api_url);

        try {
            jsonTest.put("function", "loginAttempt");
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

        Intent intent = new Intent();
        currentUserID = (int) LoginActivity.connJSON.get("results");
        intent.putExtra("userID", currentUserID);

        setResult(RESULT_OK, intent);

        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
