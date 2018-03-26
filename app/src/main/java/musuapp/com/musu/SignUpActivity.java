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

public class SignUpActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final TextView _username = findViewById(R.id.input_Username);
        final TextView _password = findViewById(R.id.input_Password);
        final TextView _vPassword = findViewById(R.id.input_VerifyPassword);
        final TextView _email =  findViewById(R.id.input_Email);
        final Button btn_signup  = findViewById(R.id.btn_SignUp);

        btn_signup.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                String username;
                username = _username.getText().toString();
                String password = _password.getText().toString();
                String email = _email.getText().toString();

                final databaseConnection conn = new databaseConnection();
                String serverName = getString(R.string.api_url);
                JSONObject jsonTest = new JSONObject();

                try {
                    jsonTest.put("function", "createUser");
                    jsonTest.put("username", username);
                    jsonTest.put("password", password);
//                    jsonTest.put("emailAddress", email);

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

                                Log.e("TEST (JSON result): ", connJSON.get("success").toString());

                                if (connJSON.get("success").toString().equals("true")) {
                                    btn_signup.setText("signed up");
                                } else {
                                    btn_signup.setText("failed");
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
