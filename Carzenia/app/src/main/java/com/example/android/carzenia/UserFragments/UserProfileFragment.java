package com.example.android.carzenia.UserFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android.carzenia.R;
import com.example.android.carzenia.SystemDatabase.DBHolder;
import com.example.android.carzenia.SystemDatabase.UserModel;
import com.example.android.carzenia.SystemDatabase.UserType;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class UserProfileFragment extends Fragment {

    private static final int GALLERY_REQUEST_CODE = 1;
    private ImageView userImage;
    private TextView nameTxt;
    private EditText mailTxt, passwordTxt, phoneTxt;
    private Button saveChangesButton;
    private Uri oldUserImageUrl;
    private Uri newUserImageUrl;
    private ProgressBar circularProgress;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;
    private StorageTask uploadTask;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        makeHooks(view);
        circularProgress.setVisibility(View.INVISIBLE);

        oldUserImageUrl = Uri.parse(this.getArguments().getString("ImageUrl"));
        if (!oldUserImageUrl.toString().equals(UserModel.NO_IMAGE))
            Picasso.get().load(oldUserImageUrl).into(userImage);
        else
            oldUserImageUrl = Uri.parse(UserModel.NO_IMAGE);
        nameTxt.setText(this.getArguments().getString("UserName"));
        mailTxt.setText(this.getArguments().getString("UserMail"));
        passwordTxt.setText(this.getArguments().getString("UserPassword"));
        phoneTxt.setText(this.getArguments().getString("UserPhone"));

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (uploadTask != null && uploadTask.isInProgress())
                    Toast.makeText(getContext(), getString(R.string.toast_update_in_progress),
                            Toast.LENGTH_SHORT).show();
                else {
                    String mail, phone, password;
                    mail = mailTxt.getText().toString();
                    phone = phoneTxt.getText().toString();
                    password = passwordTxt.getText().toString();

                    if (isValidData(mail, phone, password))
                        uploadUpdatedData(savedInstanceState);
                }

            }
        });

        return view;
    }

    private void makeHooks(View view) {
        userImage = view.findViewById(R.id.profile_image);
        nameTxt = view.findViewById(R.id.name_profile);
        mailTxt = view.findViewById(R.id.email_profile);
        passwordTxt = view.findViewById(R.id.password_profile);
        phoneTxt = view.findViewById(R.id.phone_profile);
        saveChangesButton = view.findViewById(R.id.saveChangesBtn);
        circularProgress = view.findViewById(R.id.progress_profile_circular);

        storageRef = FirebaseStorage.getInstance().getReference(DBHolder.USERS_DATABASE_IMG_ROOT);
        databaseRef = FirebaseDatabase.getInstance().getReference(DBHolder.USERS_DATABASE_INFO_ROOT);
    }

    private boolean isValidData(String mail, String phone, String password) {
        if (mail.equals("") || phone.equals("") || password.equals("")) {
            Toast.makeText(getContext(), getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 3) {
            Toast.makeText(getContext(), getString(R.string.toast_pass_validation), Toast.LENGTH_SHORT).show();
            return false;
        } else if (phone.length() != 11) {
            Toast.makeText(getContext(), getString(R.string.toast_phone_size_validation),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else
            for (int i = 0; i < phone.length(); ++i)
                if (phone.charAt(i) > '9' || phone.charAt(i) < '0') {
                    Toast.makeText(getContext(), getString(R.string.toast_phone__number_validation),
                            Toast.LENGTH_SHORT).show();
                    return false;
                }

        return true;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.toast_pick_image)),
                GALLERY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            newUserImageUrl = data.getData();
            Picasso.get().load(newUserImageUrl).into(userImage);
        } else
            Toast.makeText(getContext(), getString(R.string.toast_no_selected_img), Toast.LENGTH_SHORT).show();
    }

    private void uploadUpdatedData(@Nullable final Bundle savedInstanceState) {
        circularProgress.setVisibility(View.VISIBLE);
        if (newUserImageUrl != null && !oldUserImageUrl.equals(newUserImageUrl))
        {
            StorageReference fileRef = storageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            uploadTask = fileRef.putFile(newUserImageUrl)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    updateUserData(savedInstanceState, uri);
                                    circularProgress.setVisibility(View.INVISIBLE);
                                }
                            });
                        }
                    });
        }
        // In case the user hasn't choose image yet.
        else
            updateUserData(savedInstanceState, oldUserImageUrl);
    }

    private void updateUserData(@Nullable final Bundle savedInstanceState, Uri imageUri) {
        UserModel userModel = new UserModel(nameTxt.getText().toString(),
                mailTxt.getText().toString(), phoneTxt.getText().toString(),
                UserType.CUSTOMER, imageUri.toString());
        String branchID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseRef.child(branchID).setValue(userModel);
        Toast.makeText(getContext(), getString(R.string.toast_data_saved),
                Toast.LENGTH_SHORT).show();
        if (savedInstanceState == null)
            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DisplayCarsFragment()).commit();
    }

}
