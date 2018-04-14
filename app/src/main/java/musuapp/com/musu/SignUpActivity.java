package musuapp.com.musu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final TextView _firstname = findViewById(R.id.input_firstname);
        final TextView _lastname = findViewById(R.id.input_lastname);
        final TextView _username = findViewById(R.id.input_username);
        final TextView _password = findViewById(R.id.input_password);
        final TextView _verifyPassword = findViewById(R.id.input_verify_password);
        final TextView _email =  findViewById(R.id.input_username);
        final Button btn_signup  = findViewById(R.id.btn_SignUp);

        btn_signup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String firstname = _firstname.getText().toString();
                String lastname = _lastname.getText().toString();
                String username = _username.getText().toString();
                String password = _password.getText().toString();
                String email = _email.getText().toString();

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
                                    btn_signup.setText(getString(R.string.signup_success));
                                } else {
                                    btn_signup.setText(getString(R.string.signup_failed));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 2000
                );

//                btn_signup.setText("signed up");
            }
        });

    }
}
