package tech.sam33rg.introtuceassignment.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

import tech.sam33rg.introtuceassignment.R;

public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder> {

    private Context context;
    ArrayList<Map<String, Object>> userList;

    public UsersRecyclerAdapter(ArrayList<Map<String, Object>> userList, Context context) {
        this.userList = userList;
        this.context = context;
        Log.d("********","constructor");
    }

    @Override
    public int getItemViewType(int position) {

        return 1;
    }

    @NonNull
    @Override
    public UsersRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user, parent, false);

        return new UsersRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDetails(position);
        Log.d("********","details");

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView username, desciption;
        ImageButton delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.username);
            desciption = itemView.findViewById(R.id.descrpition);
            delete = itemView.findViewById(R.id.deleteButton);

        }
        private void setDetails(int posistion){
            String firstName = (String) userList.get(posistion).get("firstName");
            String lastName = (String) userList.get(posistion).get("lastName");
            String dateOfBirth = (String) userList.get(posistion).get("dateOfBirth");
            String gender = (String) userList.get(posistion).get("gender");
            String state = (String) userList.get(posistion).get("state");
            String id = (String) userList.get(posistion).get("id");
            String profileImg = (String) userList.get(posistion).get("profileImg");


            username.setText(firstName+" "+lastName);
            desciption.setText(gender+"|"+dateOfBirth+"|"+state);

            Glide.with(context).load(profileImg).apply(new RequestOptions()).centerCrop()    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(profile);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore.getInstance()
                            .collection("users")
                            .document(id)
                            .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            userList.remove(posistion);
                            notifyDataSetChanged();
                        }
                    });
                }
            });

        }
    }
}
