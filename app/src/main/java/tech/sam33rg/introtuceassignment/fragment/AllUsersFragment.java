package tech.sam33rg.introtuceassignment.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

import tech.sam33rg.introtuceassignment.R;


public class AllUsersFragment extends Fragment {

    FirebaseFirestore db;
    public AllUsersFragment() {
        // Required empty public constructor
    }

    public static AllUsersFragment newInstance() {
        AllUsersFragment fragment = new AllUsersFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_users, container, false);
    }
}