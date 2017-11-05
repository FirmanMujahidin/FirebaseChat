package tester.apps.com.testfirebasejson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import tester.apps.com.testfirebasejson.base.BaseActivity;
import tester.apps.com.testfirebasejson.model.User;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";
    @BindView(R.id.et_name_register)
    AppCompatEditText etNameRegister;
    @BindView(R.id.et_email_register)
    AppCompatEditText etEmailRegister;
    @BindView(R.id.et_password_register)
    ShowHidePasswordEditText etPasswordRegister;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_login)
    TextView tvLogin;


    private FirebaseAuth mAuth;

//    private ProgressDialog mProgressDialog;
    private LovelyProgressDialog waitingDialog;

    private Context context;

    private String neme;
    private String email;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_register);

//        mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

        mAuth = FirebaseAuth.getInstance();

//        ProgressDialog mProgressDialog = new ProgressDialog((Activity) context);

//        mProgressDialog = new ProgressDialog(getApplicationContext());

        waitingDialog = new LovelyProgressDialog(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user1 = new User();
                registerUser(user1);
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(in);
                finish();
            }
        });


        if (mAuth.getCurrentUser() != null) {
            Intent in = new Intent(this, MainActivity.class);
            startActivity(in);
        }

    }


    public User registerUser(User user) {
        String name = etNameRegister.getText().toString().trim();
        String email = etEmailRegister.getText().toString().trim();
        String password = etPasswordRegister.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toasty.warning(getApplicationContext(),"Nama Tidak boleh Kosong",Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(), "Nama Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(email)) {
            Toasty.warning(getApplicationContext(),"Email Tidak boleh Kosong",Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(), "Email Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
//            return user;
        }
        if (TextUtils.isEmpty(password)) {
            Toasty.warning(getApplicationContext(),"Password Tidak boleh Kosong",Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(), "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
//            return user;
        }

//        new Handler().post(new Runnable() {
//            @Override
//            public void run() {
//                mProgressDialog = ProgressDialog.show(
//                        RegisterActivity.this.getApplicationContext(), "Register User",
//                        RegisterActivity.this.getString(R.string.loading), true);
//            }
//        });
//        mProgressDialog.setMessage("Please wait...");
//        mProgressDialog.show();

//        mProgressDialog = ProgressDialog.show(this, "LOADING",
//                "Please wait..", true);

        waitingDialog.setIcon(R.drawable.ic_signup)
                .setTitle("Register....")
                .setTopColorRes(R.color.colorPrimary)
                .show();

        new Handler().post(() -> mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
//                    mProgressDialog.dismiss();
                    waitingDialog.dismiss();
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
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }));


//                .addOnCompleteListener(this, task -> {
////                    task.getResult().getUser().getUid();
//                });
        return user;
    }
}
