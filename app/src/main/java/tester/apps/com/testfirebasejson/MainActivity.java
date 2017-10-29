package tester.apps.com.testfirebasejson;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.et_msg)
    EditText addRoom;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ListView)
    ListView listView;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("users");
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_chat = new ArrayList<>();
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list_of_chat);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
            intent.putExtra("addRoom",((TextView)view).getText().toString() );
            intent.putExtra("user_name",name);
            startActivity(intent);
        });

        onClickButtonSend();
        getRoot();
        listFriends();
        request_user_name();
    }

    private void onClickButtonSend(){
        btnSend.setOnClickListener(v -> getRoom());
    }

    private void getRoom(){
        Map<String,Object> map = new HashMap<>();
        map.put(addRoom.getText().toString(),"");
        root.updateChildren(map);
    }

    private void listFriends(){
        Map<String,Object> map = new HashMap<>();
        map.put(addRoom.getText().toString(),"");
        root.updateChildren(map);
    }

    private void getRoot(){
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
//                    Log.d(TAG, "datas" + ((DataSnapshot)i.next()).getValue());
                }

                list_of_chat.clear();
                list_of_chat.addAll(set);

                Log.e(TAG, "onDataChange: ");

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void request_user_name() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter name:");

        final EditText input_field = new EditText(this);

        builder.setView(input_field);
        builder.setPositiveButton("OK", (dialogInterface, i) -> name = input_field.getText().toString());

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            dialogInterface.cancel();
            request_user_name();
        });

        builder.show();
    }

}
