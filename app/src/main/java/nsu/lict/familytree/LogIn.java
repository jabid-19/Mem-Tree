package nsu.lict.familytree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import nsu.lict.familytree.controler.DBController;
import nsu.lict.familytree.settings.SettingsConstant;

public class LogIn extends AppCompatActivity {

    private EditText etEmail;
    private EditText etPass;
    private TextView createId;

    String email;
    String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.editTextUserName);
        etPass = findViewById(R.id.editTextPassword);
        createId = findViewById(R.id.textViewCreateId);


        createId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }
    public void signIn(View view){
        email = etEmail.getText().toString();
        password = etPass.getText().toString();

        DBController dbController = new DBController(getApplicationContext());
        if(dbController.validateUser(email,password)){

            SharedPreferences.Editor editor = getSharedPreferences(SettingsConstant.SHARED_PREF_NAME, MODE_PRIVATE).edit();
            editor.putString("email", email);
            editor.apply();

            Intent mainIntent = new Intent(this,LandingActivity.class);
            startActivity(mainIntent);

        }else{
            Toast.makeText(getApplicationContext(), SettingsConstant.INCORRECT_LOGIN,Toast.LENGTH_SHORT).show();
        }

    }

    public void signUp(View view){
        Intent intent = new Intent(LogIn.this, SignUpActivity.class);
        startActivity(intent);
    }

}

