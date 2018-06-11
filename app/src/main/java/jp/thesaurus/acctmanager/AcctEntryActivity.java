package jp.thesaurus.acctmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapEditText;

import jp.thesaurus.acctmanager.jp.thesaurus.utils.DBHelper;
import jp.thesaurus.acctmanager.jp.thesaurus.utils.ViewUtil;

public class AcctEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_entry);

        Spinner spinner = findViewById(R.id.service_spinner);

        SpinnerAdapter adapter = new SpinnerAdapter(this.getApplicationContext(),
                R.layout.spinner_list, Constants.SERVICE_BEANS
                );
        spinner.setAdapter(adapter);

        findViewById(R.id.entry_button).setOnClickListener(entryClickListener);
    }

    /**
     * 登録ボタン
     */
    View.OnClickListener entryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BootstrapEditText id = findViewById(R.id.entry_id);
            BootstrapEditText password = findViewById(R.id.entry_password);
            BootstrapEditText remarks = findViewById(R.id.entry_remarks);
            TextView serviceIndex = findViewById(R.id.s_index_view_gone);
            TextView serviceName = findViewById(R.id.s_name_view);

            if(!ViewUtil.formValidate(id,password,remarks)){
                return;
            }

            DBHelper helper = new DBHelper(getApplicationContext());
            SQLiteDatabase db = helper.getWritableDatabase();
            long res = helper.insertData(db,
                    id.getText().toString(),
                    password.getText().toString(),
                    remarks.getText().toString(),
                    serviceIndex.getText().toString(),
                    serviceName.getText().toString());
            if (res == -1) {
                Toast.makeText(getApplication(), "登録失敗", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplication(), "登録成功", Toast.LENGTH_SHORT).show();
            }
            Log.d("--------------------a---------------------",serviceName.getText().toString());

            finish();
        }
    };
}
