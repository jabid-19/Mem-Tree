package nsu.lict.familytree;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;
import java.util.UUID;

import nsu.lict.familytree.controler.DBController;
import nsu.lict.familytree.settings.SettingsConstant;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etEmail;
    private EditText etPass;
    private EditText etConfirmPass;
    private EditText etClan;

    private String name;
    private String email;
    private String pass;
    private String clan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setTitle("Sign Up");

        etName = findViewById(R.id.editTextSignUpName);
        etEmail = findViewById(R.id.editTextSignUpEmail);
        etPass = findViewById(R.id.editTextSignUpPassword);
        etConfirmPass = findViewById(R.id.editTextConfirmPass);
        etClan = findViewById(R.id.editTextSignUpClanCode);


    }

    public void addUser(View view){
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        pass = etPass.getText().toString();
        clan = etClan.getText().toString();

        if(!name.isEmpty() && !email.isEmpty() && !pass.isEmpty()){
            if(pass.equals(etConfirmPass.getText().toString())){
                if(clan.isEmpty()){
                    Random random = new Random();
                    clan = String.valueOf(random.nextInt(10000));
                }
                DBController dbController = new DBController(getApplicationContext());
                dbController.addUser(name,email,pass,clan);
                Toast.makeText(getApplicationContext(),SettingsConstant.USER_ADD,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this,LogIn.class);
                startActivity(intent);

            }else{
                Toast.makeText(getApplicationContext(), SettingsConstant.PASSWORD_MISMATCH,Toast.LENGTH_SHORT).show();
            }
        }

    }
}
