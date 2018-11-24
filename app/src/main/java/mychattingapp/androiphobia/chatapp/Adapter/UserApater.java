package mychattingapp.androiphobia.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import mychattingapp.androiphobia.chatapp.MessageActivity;
import mychattingapp.androiphobia.chatapp.Model.User;
import mychattingapp.androiphobia.chatapp.R;

public class UserApater extends RecyclerView.Adapter<UserApater.UserViewolder> {
    private ArrayList<User> usersList;
    private Context context;

    public UserApater(ArrayList<User> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public UserViewolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_row, viewGroup, false);
        return new UserViewolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewolder holder, int i) {

        final User user = usersList.get(i);

        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profile_image.setImageResource(R.drawable.ic_person);
        } else {
            Glide.with(context).load(user.getImageURL()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class UserViewolder extends RecyclerView.ViewHolder{
        TextView username;
        CircleImageView profile_image;

        public UserViewolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username_textView_user_row);
            profile_image = itemView.findViewById(R.id.profile_image_view_user_row);
        }
    }
}
