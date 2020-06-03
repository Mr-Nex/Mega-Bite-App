package generator.nex.rexx.userregistration;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import generator.nex.rexx.userregistration.Model.User;

import com.example.rexx.userregistration.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {

    MaterialEditText phoneText, nameText, passwordText, emailText;
    Button btnSignUp;

    DatabaseReference table_user;

    //ProgressDialog mDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        phoneText = (MaterialEditText)findViewById(R.id.phoneUp);
        nameText = (MaterialEditText)findViewById(R.id.name);
        emailText=(MaterialEditText)findViewById(R.id.email);
        passwordText = (MaterialEditText)findViewById(R.id.passwordUp);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        table_user = firebaseDatabase.getReference("User");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();*/

                addData();
            }
        });
    }

    private void addData(){

        String phone = phoneText.getText().toString();
        String name = nameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ){
            Toast.makeText(SignUp.this, "Empty Field!!!" , Toast.LENGTH_SHORT).show();
        }
        else if(!name.matches("[a-zA-Z ]+")){
            nameText.requestFocus();
            nameText.setError(getString(R.string.err_msg));
        }
        else if(!password.matches("[a-zA-Z ]+")){
            passwordText.requestFocus();
            passwordText.setError(getString(R.string.err_msg));
        }
        else if(!email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
            emailText.requestFocus();
            emailText.setError(getString(R.string.err_msg));
        }
        else if(phone.length() > 10 || phone.length() <10){
            phoneText.requestFocus();
            phoneText.setError(getString(R.string.err_msg));
        }
        else{
            User user = new User(name,password,phone,email);
            table_user.child(phone).setValue(user);
            Toast.makeText(SignUp.this, "Registration Successful" , Toast.LENGTH_SHORT).show();

            nameText.setText("");
            passwordText.setText("");
            emailText.setText("");
            phoneText.setText("");
        }

    }

}
