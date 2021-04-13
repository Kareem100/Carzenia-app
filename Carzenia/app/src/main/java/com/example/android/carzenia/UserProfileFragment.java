package com.example.android.carzenia;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment
{
    private static final int GALLERY_REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ImageView image;
    private TextView nameTxt;
    private EditText mailTxt, passwordTxt, phoneTxt;
    private Button button;
    private DBManager DB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_profile_fragment, container, false);

        DB = new DBManager(getContext());
        image = view.findViewById(R.id.profile_image);
        nameTxt = view.findViewById(R.id.name_profile);
        mailTxt = view.findViewById(R.id.email_profile);
        passwordTxt = view.findViewById(R.id.password_profile);
        phoneTxt = view.findViewById(R.id.phone_profile);
        button = view.findViewById(R.id.saveChangesBtn);

        Bitmap bm = DB.getUserImage(HomeActivity.username);
        if(bm!=null)
            image.setImageBitmap(bm);
        nameTxt.setText(HomeActivity.username);
        mailTxt.setText(DB.getUserMail(HomeActivity.username));
        passwordTxt.setText(DB.getUserPassword(HomeActivity.username));
        phoneTxt.setText(DB.getUserPhone(HomeActivity.username));

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String name, mail, phone, password;
                name = nameTxt.getText().toString();
                mail = mailTxt.getText().toString();
                phone = phoneTxt.getText().toString();
                password = passwordTxt.getText().toString();

                if(goodData(mail, phone, password)){
                    if(bitmap!=null)
                        DB.addUserImage(nameTxt.getText().toString(), bitmap);
                    DB.updateUserData(name, mail, phone, password);
                    Toast.makeText(getContext(), "Data Saved Successfully !!",
                            Toast.LENGTH_SHORT).show();
                    if (savedInstanceState == null)
                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new DisplayCarsFragment()).commit();
                }
        }});
        return view;
    }

    private boolean goodData(String mail, String phone, String password){
        if (mail.equals("") || phone.equals("") || password.equals("")){
            Toast.makeText(getContext(), "Please Fill The Empty Fields !!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (password.length() < 3){
            Toast.makeText(getContext(), "Password Must Be At Least Of 3 Characters !!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (phone.length()!=11){
            Toast.makeText(getContext(), "Phone Number Must Consist Of 11 Digits !!",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            for (int i = 0; i < phone.length(); ++i)
                if(phone.charAt(i) > '9' || phone.charAt(i) < '0'){
                    Toast.makeText(getContext(), "Phone Number Must Consist Of Only Numbers !!",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

        return true;
    }

    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pick An Image !"), GALLERY_REQUEST_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageData = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageData);
                image.setImageURI(imageData);
            } catch (IOException e) {
                Toast.makeText(getContext(), "Error Reading The Image..", Toast.LENGTH_SHORT).show();
            }
        }
        else Toast.makeText(getContext(), "No New Image Selected !", Toast.LENGTH_SHORT).show();
    }
}
