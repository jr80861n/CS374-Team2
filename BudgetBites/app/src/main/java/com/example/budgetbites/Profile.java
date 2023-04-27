package com.example.budgetbites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {
    private EditText firstNameEditText, lastnameEditText, emailEditText ;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        //Adding the info to the profile
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastnameEditText = findViewById(R.id.lastnameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        profileImageView = findViewById(R.id.profileImageView);
        Button saveButton = findViewById(R.id.Save_Button);
        Button deleteButton = findViewById(R.id.Delete_button);
        Button signOutButton = findViewById(R.id.Sign_out);
        Button changePasswordButton = findViewById(R.id.Change_password_button);

        //Loading the saved profile info
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String firstName = sharedPreferences.getString("firstName", "");
        String lastName = sharedPreferences.getString("lastName", "");
        String email = sharedPreferences.getString("email", "");
        String profileImage = sharedPreferences.getString("profileImage", "");
        firstNameEditText.setText(firstName);
        lastnameEditText.setText(lastName);
        emailEditText.setText(email);
        if (!profileImage.equals("")) {
            profileImageView.setImageURI(Uri.parse(profileImage));
        }// test the if statement



        //Setting up the Image feature that will allow you to pick a picture from your gallery
        profileImageView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //This is the intent that will allow you to pick a picture from your gallery
startActivityForResult(intent, 1); //This is the code that will allow you to pick a picture from your gallery

        });

        saveButton.setOnClickListener(v -> saveProfileInfo());

        deleteButton.setOnClickListener(v -> deleteProfileInfo());

        signOutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Profile.this, MainActivity.class);
            startActivity(intent);

        });
        changePasswordButton.setOnClickListener(v -> {
            //FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Profile.this, MainActivity.class);
            startActivity(intent);

        });//test the change password button and make a change password activity/fragment

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            profileImageView.setImageURI(selectedImage);

            //This is where you would save the user's profile image to the database
            SharedPreferences preferences = getSharedPreferences("profile_info", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("profileImage", selectedImage.toString());
            editor.apply();

        }


    }

    //Method that will give you the ability to save the user's profile data
    public void saveProfileInfo(){
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastnameEditText.getText().toString();
        String email = emailEditText.getText().toString();

        //Validating the user's input
        if(firstName.equals("") || lastName.equals("") || email.equals("")){
            Toast.makeText(this, "Please enter all the information", Toast.LENGTH_SHORT).show();
            return;
        }



        //This is where you would save the user's profile data to the database
        SharedPreferences preferences = getSharedPreferences("profile_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.apply();


        Toast.makeText(this, "Profile Saved, welcome :)", Toast.LENGTH_SHORT).show();


    }

    //Method that will give you the ability to delete the user's profile data
    public void deleteProfileInfo(){
        SharedPreferences preferences = getSharedPreferences("profile_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("firstName");
        editor.remove("lastName");
        editor.remove("email");
        editor.apply();


        //Data that will be deleted from the database
        firstNameEditText.setText("");
        lastnameEditText.setText("");
        emailEditText.setText("");
        profileImageView.setImageResource(R.drawable.ic_launcher_background);//change the name

        Toast.makeText(this, "Profile Deleted :(", Toast.LENGTH_SHORT).show();

    }


}
