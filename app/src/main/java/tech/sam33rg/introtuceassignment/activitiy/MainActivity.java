package tech.sam33rg.introtuceassignment.activitiy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tech.sam33rg.introtuceassignment.R;
import tech.sam33rg.introtuceassignment.adapter.SimplePagerAdapter;
import tech.sam33rg.introtuceassignment.fragment.AddUserFragment;
import tech.sam33rg.introtuceassignment.fragment.AllUsersFragment;

public class MainActivity extends AppCompatActivity {

    String TAG = "************";
    Integer number = 1234567890;
    Integer old = 1234567891;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    SimplePagerAdapter simplePagerAdapter;
    ArrayList<Fragment> fragments;
    ArrayList<String> tabTitles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);

        fragments = new ArrayList<>();
        tabTitles = new ArrayList<>();

        fragments.add(AllUsersFragment.newInstance());
        tabTitles.add("Users");

        fragments.add(AddUserFragment.newInstance());
        tabTitles.add("Enroll");


        simplePagerAdapter = new SimplePagerAdapter(this,fragments);

        viewPager.setAdapter(simplePagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTitles.get(position));
                    }
                }).attach();


















        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("firstName", "sameer");
        user.put("lastName", "gangar");
        user.put("dateOfBirth", "11/08/1999");
        user.put("gender", "male");
        user.put("country", "IN");
        user.put("state", "delhi");
        user.put("homeTown", "delhi");
        user.put("phoneNumber", old);
        user.put("telephoneNumber", 123123214);
        db.collection("users")
                .whereEqualTo("phoneNumber", number)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, ""+task.getResult().size());

                            if(task.getResult().size()==0){
                                Log.d(TAG, "number not present");

                            }
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });





// Add a new document with a generated ID
       /* db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });*/




    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode,resultCode,data);
        Log.d(TAG,"result");
        fragments.get(1).onActivityResult(requestCode,resultCode,data);

    }
}