package generator.nex.rexx.userregistration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.rexx.userregistration.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(3500);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
            }
        };
        timer.start();
    }

    protected void onPause(){
        super.onPause();
        finish();
    }
}
