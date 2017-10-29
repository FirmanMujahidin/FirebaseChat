package tester.apps.com.testfirebasejson;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tester.apps.com.testfirebasejson.base.BaseActivity;


public class ChatActivity extends AppCompatActivity {

    private static final String TAG = "ChatActivity";

    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.edittext_chat_message)
    EditText edittext_chat_message;
    @BindView(R.id.textView)
    TextView textViewChat;

    private String user_name,addRoom;
    private DatabaseReference root ;
    private String temp_key,chat_msg,chat_user_name;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        initToolbar("Chat" + addRoom,true);
        user_name = getIntent().getExtras().get("user_name").toString();
        addRoom = getIntent().getExtras().get("addRoom").toString();


        root = FirebaseDatabase.getInstance().getReference().child(addRoom);

        onClickButtonSend();

        getRoot();
    }

    private void onClickButtonSend(){
        btnSend.setOnClickListener(v ->clickSend());
    }



    private void clickSend(){
        Map<String,Object> map = new HashMap<>();
        temp_key = root.push().getKey();
        root.updateChildren(map);

        DatabaseReference message_root = root.child(temp_key);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name",user_name);
        map2.put("msg",edittext_chat_message.getText().toString());
        edittext_chat_message.setText("");
        message_root.updateChildren(map2);
    }

    private void getRoot(){
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
            textViewChat.append(chat_user_name +" : "+chat_msg +" \n");


        }Log.d(TAG, "append_chat_conversation: " + hashCode());


    }
}
