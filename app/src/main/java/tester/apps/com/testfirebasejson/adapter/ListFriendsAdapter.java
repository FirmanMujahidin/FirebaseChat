package tester.apps.com.testfirebasejson.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tester.apps.com.testfirebasejson.R;
import tester.apps.com.testfirebasejson.model.User;

/**
 * Created by firma on 05-Nov-17.
 */

public class ListFriendsAdapter extends RecyclerView.Adapter<ListFriendsAdapter.ViewHolder>{

    private List<User> mUser;
    private Context context;

    public ListFriendsAdapter(List<User> mUser, Context context) {
        this.mUser = mUser;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_friend, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.icon_avata.setImageDrawable();
        holder.txtName.setText(mUser.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "" + mUser.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser == null ? 0 : mUser.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView icon_avata;
        public TextView txtName,txtTime,txtMessage;


        public ViewHolder(View itemView) {
            super(itemView);

            icon_avata = (CircleImageView) itemView.findViewById(R.id.icon_avata);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtTime = (TextView) itemView.findViewById(R.id.txtMessage);

        }
    }
}
