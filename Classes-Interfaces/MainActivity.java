package generator.nex.rexx.userregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.rexx.userregistration.R;

public class MainActivity extends AppCompatActivity {

    Button btnSignUp, btnSignIn;
    TextView smallText, largeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);
        btnSignIn = (Button)findViewById(R.id.btnSignIn);

        smallText = (TextView)findViewById(R.id.smallText);
        largeText = (TextView)findViewById(R.id.largeText);

        /*
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/sedgwickavedisplay.ttf");
        smallText.setTypeface(face);
        largeText.setTypeface(face);
        */

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(getApplicationContext(), SignUp.class);
                startActivity(signup);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signin = new Intent(getApplicationContext(), SignIn.class);
                startActivity(signin);
            }
        });

    }
}
