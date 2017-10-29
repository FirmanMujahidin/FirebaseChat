package tester.apps.com.testfirebasejson.base;

import android.app.ProgressDialog;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import tester.apps.com.testfirebasejson.R;

/**
 * Created by firma on 29-Oct-17.
 */

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static BaseActivity mInstance;
    private ProgressDialog pDialog;



    protected void bind (int layout){
        setContentView(layout);
        ButterKnife.bind(this);
        setupProgressDialog();
    }

    protected void initToolbar(String title, boolean backhome){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(backhome);
        getSupportActionBar().setHomeButtonEnabled(backhome);
        getSupportActionBar().setTitle(title.equals("") ? "" : title);
        if (backhome){
            toolbar.setNavigationOnClickListener(v -> onBackPressed());
            toolbar.setNavigationIcon(R.drawable.ic_back_home);
        }
    }

    protected void initToolbar(String title) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title.equals("") ? "" : title);
    }


    private void setupProgressDialog() {
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        mInstance = this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
