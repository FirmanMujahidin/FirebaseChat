package tester.apps.com.testfirebasejson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import tester.apps.com.testfirebasejson.base.BaseActivity;
import tester.apps.com.testfirebasejson.model.User;

public class RegisterActivity extends BaseActivity{

    private static final String TAG = "RegisterActivity";
    @BindView(R.id.et_name_register)
    AppCompatEditText etNameRegister;
    @BindView(R.id.et_email_register)
    AppCompatEditText etEmailRegister;
    @BindView(R.id.et_password_register)
    AppCompatEditText etPasswordRegister;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgressDialog;

    private String neme;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        btnRegister.setOnClickListener(v -> {
            User user1 = new User();
            registerUser(user1);
        });

        tvLogin.setOnClickListener(v -> {
            Intent in = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(in);
            finish();
        });

    }



    public User registerUser(User user) {

        String name = etNameRegister.getText().toString().trim();
        String email = etEmailRegister.getText().toString().trim();
        String password = etPasswordRegister.getText().toString().trim();

        if (TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(), "Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return user;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
            return user;
        }

        mProgressDialog.setMessage("Please wait...");
        mProgressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    mProgressDialog.dismiss();
                    Log.d(TAG, ":onComplete:" + task.isSuccessful());
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (task.isSuccessful()) {
                        Log.d(TAG, "createUserFailed" + task.getException());

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
                        DatabaseReference currentUserDB = mDatabase.child(mAuth.getCurrentUser().getUid());
                        currentUserDB.child("created_time").setValue("created_time");
                        currentUserDB.child("name").setValue(name);
                        currentUserDB.child("email").setValue(email);
                        currentUserDB.child("last_login").setValue("last_login");
                        currentUserDB.child("uid").setValue(mAuth.getCurrentUser().getUid());


                        Intent in = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(in);
                        finish();
                    }else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
//                    task.getResult().getUser().getUid();
                });
        return user;
    }
}
