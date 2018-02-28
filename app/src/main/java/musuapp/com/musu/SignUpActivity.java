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
import java.util.concurrent.ExecutionException;

/**
 * Created by Kevin Santana on 2/28/2018.
 */

public class SignUpActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_signup);
       final TextView username = findViewById(R.id.input_Username);
       final TextView password = findViewById(R.id.input_Password);
       final TextView vPassword = findViewById(R.id.input_VerifyPassword);
       final TextView email =  findViewById(R.id.input_Email);
       final Button btn_signup  = findViewById(R.id.btn_SignUp);

       btn_signup.setOnClickListener(new View.OnClickListener()
       {
           public void onClick(View v)
           {
             btn_signup.setText("signed up");
           }
       });

    }

}
