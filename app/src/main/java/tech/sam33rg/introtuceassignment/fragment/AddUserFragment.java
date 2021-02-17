package tech.sam33rg.introtuceassignment.fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import tech.sam33rg.introtuceassignment.R;

public class AddUserFragment extends Fragment {


    FirebaseFirestore db;
    ArrayList<TextInputLayout> textInputLayouts; //firstName,lastName,dateOfBirth,gender,country,state,homeTown,phoneNumber,telephoneNumber;
    ArrayList<String> texts;//_firstName,_lastName,_dateOfBirth,_gender,_country,_state,_homeTown,_phoneNumber,_telephoneNumber;
    String TAG = "****AddUserFragment";
    Button addUser;
    View profileImage;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public AddUserFragment() {
        // Required empty public constructor
    }


    public static AddUserFragment newInstance() {
        AddUserFragment fragment = new AddUserFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_user, container, false);
        textInputLayouts = new ArrayList<>();
        texts = new ArrayList<>();
        textInputLayouts.add(view.findViewById(R.id.firstName));
        textInputLayouts.add(view.findViewById(R.id.lastName));
        textInputLayouts.add(view.findViewById(R.id.dateOfBirth));
        textInputLayouts.add(view.findViewById(R.id.gender));
        textInputLayouts.add(view.findViewById(R.id.country));
        textInputLayouts.add(view.findViewById(R.id.state));
        textInputLayouts.add(view.findViewById(R.id.homeTown));
        textInputLayouts.add(view.findViewById(R.id.phoneNumber));
        textInputLayouts.add(view.findViewById(R.id.telephoneNumber));

        addUser = view.findViewById(R.id.submit);

        profileImage = view.findViewById(R.id.profileImage);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();

        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyData();
            }
        });

        for (TextInputLayout layout:textInputLayouts){
            Objects.requireNonNull(layout.getEditText()).setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        String s = layout.getEditText().getText().toString();
                        texts.add(s);
                        if(!(s == null || s.equals(""))){
                            layout.setErrorEnabled(false);
                        }else {
                            layout.setError(layout.getHint()+" is required.");
                        }

                    }
                }
            });
        }

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }
    private void verifyData (){
        texts.clear();
        Boolean complete = true;
        for (TextInputLayout layout:textInputLayouts){
            String s = layout.getEditText().getText().toString();
            texts.add(s);
            if(s == null || s.equals("")){
                layout.setError(layout.getHint()+" is required.");
//                complete = false;
            }
        }
        if(complete){
            checkUniqueNumber(texts.get(7));
        }


    }
    private void checkUniqueNumber(String number){

        db.collection("users")
                .whereEqualTo("phoneNumber", number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, ""+task.getResult().size());

                            if(task.getResult().size()!=0){
                                Log.d(TAG, "number already present" + number );
                                textInputLayouts.get(7).setError("Number already registered");
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void uploadFile(final Uri file, String phone) {
        //if there is a file to upload
        if (file != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child("profile_img/"+phone+".jpg");
            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            progressDialog.dismiss();

                            getDownloadUrl();

                            Toast.makeText(getActivity(), "image uploaded! ", Toast.LENGTH_LONG).show();
                        }
                        private void getDownloadUrl() {


                            FirebaseStorage storage = FirebaseStorage.getInstance();
                            StorageReference storageRef = storage.getReference();

                            storageRef.child("profile_img/"+phone+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    // Got the download URL for 'users/me/profile.png'


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Toast.makeText(getActivity(), "Some error occurred. Try Again!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
}