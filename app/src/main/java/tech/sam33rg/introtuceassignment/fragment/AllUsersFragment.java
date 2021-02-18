package tech.sam33rg.introtuceassignment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tech.sam33rg.introtuceassignment.R;
import tech.sam33rg.introtuceassignment.adapter.UsersRecyclerAdapter;


public class AllUsersFragment extends Fragment {

    FirebaseFirestore db;
    ArrayList<Map<String, Object>> userList;
    String TAG = "*****AllUsersFragment";
    UsersRecyclerAdapter recyclerAdapter;
    RecyclerView recyclerView;
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
        db = FirebaseFirestore.getInstance();
        userList = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_all_users, container, false);
        recyclerView = view.findViewById(R.id.recyclerciew);
        recyclerAdapter = new UsersRecyclerAdapter(userList, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        userList.clear();
        db.collection("users").orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> map = document.getData();
                                map.put("id",document.getId());
                                Log.d(TAG, document.getId()+ " => " + map);
                                userList.add(map);
                            }
                            recyclerView.setAdapter(recyclerAdapter);
                            recyclerAdapter.notifyDataSetChanged();
                            Log.d("********",""+userList.size());

                        }
                    }
                });


    }
}