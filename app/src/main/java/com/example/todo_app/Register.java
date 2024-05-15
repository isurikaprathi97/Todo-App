package com.example.todo_app;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.todo_app.Utils.DataBaseHelper;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Declare EditTexts and Button
        EditText name = findViewById(R.id.txtname);
        EditText email = findViewById(R.id.txtemail);
        EditText password = findViewById(R.id.txtpassword);
        EditText confirm_Password = findViewById(R.id.txtcomfirmpassword);
        Button Register = findViewById(R.id.btnregister);

        Register.setOnClickListener((v -> {

            // Get the text entered by the user in the EditTexts
            String Name = name.getText().toString();
            String Email = email.getText().toString();
            String Password = password.getText().toString();
            String Confirm_Password = confirm_Password.getText().toString();

            //Check all fields are not empty
            if (Name.isEmpty() && Email.isEmpty() && Password.isEmpty() && Confirm_Password.isEmpty()){
                Toast.makeText(Register.this, "Please enter all the data...", Toast.LENGTH_SHORT).show();
            }
            //check password match
            else if (!Password.equals(Confirm_Password)){
                Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

            }

            else {
                try (DataBaseHelper dbHelper = new DataBaseHelper(Register.this)) {
                    //pass data to db helper
                    dbHelper.registerUser(Name, Email, Password, Confirm_Password);
                    Toast.makeText(Register.this, "Register successful !.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e("RegisterActivity", "DB Error occurred", e);
                }
            }

        }));



        //swap using login button
        TextView Login = findViewById(R.id.btnswaplogin);
        Login.setOnClickListener(v -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}