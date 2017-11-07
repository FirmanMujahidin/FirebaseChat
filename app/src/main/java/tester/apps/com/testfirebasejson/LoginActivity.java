package tester.apps.com.testfirebasejson;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText;
import com.yarolegovich.lovelydialog.LovelyProgressDialog;

import butterknife.BindView;
import es.dmoral.toasty.Toasty;
import tester.apps.com.testfirebasejson.base.BaseActivity;
import tester.apps.com.testfirebasejson.model.User;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";


    @BindView(R.id.et_email_login)
    AppCompatEditText etEmailLogin;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.et_password_login)
    ShowHidePasswordEditText etPasswordLogin;

    private FirebaseAuth mAuth;

//    private ProgressDialog mProgressDialog;
    private LovelyProgressDialog waitingDialog;

    private String email;
    private String password;
    private Object user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bind(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (mAuth.getCurrentUser() != null) {
            Intent in = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(in);
        }


        btnLogin.setOnClickListener(v -> {
            User user1 = new User();
            loginUser(user1);
        });

        tvRegister.setOnClickListener(v -> {
            Intent in = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(in);
            finish();
        });

//        mProgressDialog = new ProgressDialog(this);
        waitingDialog = new LovelyProgressDialog(this);


//        user.setEmail("123@gmail.com");
//        user.setPassword("123456");
//        registerUser(user);
    }


    private User loginUser(User user) {
//        user.setEmail(etEmailLogin.getText().toString());
//        user.setPassword(etPasswordLogin.getText().toString());
        String email = etEmailLogin.getText().toString().trim();
        String password = etPasswordLogin.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toasty.warning(getApplicationContext(),"Email Tidak boleh Kosong",Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(), "Emal Tidak Boleh Kossong", Toast.LENGTH_SHORT).show();
            return user;
        }
        if (TextUtils.isEmpty(password)) {
            Toasty.warning(getApplicationContext(),"Password Tidak boleh Kosong",Toast.LENGTH_SHORT, true).show();
//            Toast.makeText(getApplicationContext(), "Password Tidak Boleh Kossong", Toast.LENGTH_SHORT).show();
            return user;
        }
//        mProgressDialog.setMessage("Please wait..");
//        mProgressDialog.show();

//        mProgressDialog = ProgressDialog.show(this, "LOADING",
//                "Please wait..", true);
        waitingDialog.setIcon(R.drawable.ic_person_low)
                .setTitle("Login....")
                .setTopColorRes(R.color.colorPrimary)
                .show();

        new Handler().post(() -> mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
//                    mProgressDialog.dismiss();
                    waitingDialog.dismiss();
                    Log.d(TAG, "onComplete: " + email + "-" + password);
                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//                    Log.d(TAG, "getResult" + task.getResult());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success");
                        FirebaseUser user1 = mAuth.getCurrentUser();
                        Log.d(TAG, "onComplete user: " + user1);
                        finish();
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


        return user;
    }
}
