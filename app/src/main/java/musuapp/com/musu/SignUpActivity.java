package musuapp.com.musu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    public static Context context;
    public static SignUpActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        context = super.getBaseContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final TextView _firstname = findViewById(R.id.input_firstname);
        final TextView _lastname = findViewById(R.id.input_lastname);
        final TextView _username = findViewById(R.id.input_username);
        final TextView _password = findViewById(R.id.input_password);
        final TextView _verifyPassword = findViewById(R.id.input_verify_password);
        final TextView _email =  findViewById(R.id.input_emailaddress);
        final Button btn_signup  = findViewById(R.id.btn_SignUp);

        activity = this;

        btn_signup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String firstname = _firstname.getText().toString();
                String lastname = _lastname.getText().toString();
                String username = _username.getText().toString();
                String password = _password.getText().toString();
                String verifyPassword = _verifyPassword.getText().toString();
                String email = _email.getText().toString();

                if(!verifyPassword.equals(password)) {
                    Toast toast = Toast.makeText(context,"Passwords do not match", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    SharedPreferences access = getSharedPreferences("Login", MODE_PRIVATE);
                    String token = access.getString("token", "");

                    final databaseConnection conn = new databaseConnection();
                    String serverName = getString(R.string.api_url);
                    JSONObject jsonTest = new JSONObject();

                    try {
                        jsonTest.put("function", "createUser");
                        jsonTest.put("firstName", firstname);
                        jsonTest.put("lastName", lastname);
                        jsonTest.put("username", username);
                        jsonTest.put("password", password);
                        jsonTest.put("emailAddress", email);
                        jsonTest.put("token", token);

                        Log.e("sign (JSON payload): ", jsonTest.toString());

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

                                        Log.e("sign (JSON result): ", successResult);

                                        if (successResult.equals("true")) {
                                            Toast toast = Toast.makeText(SignUpActivity.context, "Successfully signed up!", Toast.LENGTH_LONG);
                                            toast.show();
                                            activity.finish();
                                        } else {
                                            Toast toast = Toast.makeText(SignUpActivity.context, "Problem when trying to Sign up", Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, 2000
                    );
                }

            }
        });

    }
}
