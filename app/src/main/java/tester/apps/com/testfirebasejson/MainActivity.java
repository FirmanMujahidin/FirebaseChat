package tester.apps.com.testfirebasejson;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import tester.apps.com.testfirebasejson.adapter.ListFriendsAdapter;
import tester.apps.com.testfirebasejson.base.BaseActivity;
import tester.apps.com.testfirebasejson.model.User;

import static android.content.ContentValues.TAG;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.recycleListFriend)
    RecyclerView recycleListFriend;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("users");
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> list_of_chat = new ArrayList<>();
    boolean doubleBackToExitPressedOnce = false;
    private String name;

    private List<User> mUser = new ArrayList<User>();

    private ListFriendsAdapter mFriendsAdapter;

//    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_main);
        initToolbar("Chat");
        getRoot(0);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false);

        recycleListFriend.setLayoutManager(linearLayoutManager);
        mFriendsAdapter = new ListFriendsAdapter(mUser,this);
        Log.d(TAG, "after adapter: ");
//        swipeRefreshLayout.setOnRefreshListener();
        Log.d(TAG, "onCreate: " + mFriendsAdapter);
        recycleListFriend.setAdapter(mFriendsAdapter);
        Toasty.success(getApplicationContext(), "Success!", Toast.LENGTH_SHORT, true).show();

//        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list_of_chat);

//        listView.setAdapter(arrayAdapter);

//        listView.setOnItemClickListener((adapterView, view, i, l) -> {
//            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
////            intent.putExtra("addRoom",((TextView)view).getText().toString() );
//            intent.putExtra("user_name", name);
//            startActivity(intent);
//        });

//        onClickButtonSend();
        listFriends();
//        request_user_name();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(getApplicationContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logout();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.logout, (dialog, which) -> {
                    dialog.dismiss();
                    FirebaseAuth.getInstance().signOut();
                    Intent in = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(in);
                    finish();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void onClickButtonSend() {
//        btnSend.setOnClickListener(v -> getRoom());
    }

    private void getRoom() {
        Map<String, Object> map = new HashMap<>();
//        map.put(addRoom.getText().toString(),"");
        root.updateChildren(map);
    }

    private void listFriends() {
        Map<String, Object> map = new HashMap<>();
//        map.put(addRoom.getText().toString(),"");
        root.updateChildren(map);
    }

    private void getRoot(final int index) {
        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    HashMap mapUserInfo = (HashMap) dataSnapshot.getValue();

                    for (Object map : mapUserInfo.values()) {
                        HashMap mapTemp = (HashMap) map;
                        Log.d(TAG, "mapTemp: " + mapTemp);
                        String email = mapTemp.get("email").toString();
                        Log.d(TAG, "emailMap" + email);
//                        for (Object string : mapTemp.values()) {
//                            Log.d(TAG, "onDataChange: " + string);
//                        }
                        User uUser = new User();
                        uUser.setName(mapTemp.get("name").toString());
                        uUser.setEmail(mapTemp.get("email").toString());
                        uUser.setUid(mapTemp.get("uid").toString());
                        mUser.add(uUser);
                    }
//                    Iterator it = mapUserInfo.entrySet().iterator();
//                    while (it.hasNext()) {
//                        Map.Entry pair = (Map.Entry)it.next();
//                        Log.d(TAG, "hashmapdata: "+pair.getKey() + " = " + pair.getValue());
//                    }
                    mFriendsAdapter.notifyDataSetChanged();
                }


//                Set<String> get = new HashSet<>();
//                Iterator i = dataSnapshot.getChildren().iterator();
//
//                while (i.hasNext()) {
//                    String nama = ((DataSnapshot) i.next()).child("name").getValue().toString().trim();
//                    String email = ((DataSnapshot) i.next()).child("email").getValue().toString().trim();
//                    String uid = ((DataSnapshot) i.next()).child("uid").getValue().toString().trim();
//
//                    get.add(((DataSnapshot) i.next()).child("name").getValue().toString().trim());
//                    Log.d(TAG, "datas" + nama);
//                    User uUser = new User();
//                    uUser.setName(nama);
//                    uUser.setEmail(email);
//                    uUser.setUid(uid);
//                    mUser.add(uUser);
//                }
//
//                mUser.clear();
////                list_of_chat.addAll(get);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Internet Error!!!", Toast.LENGTH_SHORT).show();
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