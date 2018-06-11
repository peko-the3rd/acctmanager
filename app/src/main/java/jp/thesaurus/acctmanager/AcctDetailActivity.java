package jp.thesaurus.acctmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.thesaurus.acctmanager.jp.thesaurus.utils.DBHelper;
import jp.thesaurus.acctmanager.jp.thesaurus.utils.ViewUtil;

public class AcctDetailActivity extends AppCompatActivity {

    //交互切り替え
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_detail);

        Intent intent = getIntent();
        String selectedId = intent.getStringExtra("id");
        int selectedIcon= intent.getIntExtra("icon", 0);
        String selectedUid= intent.getStringExtra("uid");

        BootstrapEditText textView = findViewById(R.id.selected_id);
        textView.setText(selectedId);
        TextView textViewGone = findViewById(R.id.selected_id_gone);
        textViewGone.setText(selectedId);

        ImageView imageView = findViewById(R.id.selected_img);
        imageView.setImageResource(selectedIcon);

        TextView uidGoneView = findViewById(R.id.selected_uid_gone);
        uidGoneView.setText(selectedUid);

        Date date = new Date();
        DBHelper helper = new DBHelper(getApplicationContext());
        Map map= helper.getAccountData(date,selectedUid);

        BootstrapEditText passwordView = findViewById(R.id.selected_password);
        passwordView.setText(map.get("password").toString());
        TextView passwordViewGone = findViewById(R.id.selected_password_gone);
        passwordViewGone.setText(map.get("password").toString());

        BootstrapEditText remarksView = findViewById(R.id.selected_remarks);
        remarksView.setText(map.get("remarks").toString());
        TextView remarksGoneView = findViewById(R.id.selected_remarks_gone);
        remarksGoneView.setText(map.get("remarks").toString());



        findViewById(R.id.edit_button).setOnClickListener(editClickListener);
        findViewById(R.id.update_button).setOnClickListener(updateClickListener);
        findViewById(R.id.finish_button).setOnClickListener(finishClickListener);

        Spinner spinner = findViewById(R.id.service_spinner);
        spinner.setEnabled(false);
        spinner.setClickable(false);
        SpinnerAdapter adapter = new SpinnerAdapter(this.getApplicationContext(),
                R.layout.spinner_list, Constants.SERVICE_BEANS
        );
        spinner.setAdapter(adapter);
        int selectioPosition = Integer.parseInt(map.get("service_index").toString())-1;
        spinner.setSelection(selectioPosition,true);
        TextView spinnerGone = findViewById(R.id.service_spinner_gone);
        spinnerGone.setText(map.get("service_index").toString());
        // 初回動作の対応
        spinner.setFocusable(false);

    }
    /**
    * 編集ボタン
    * 活性非活性
    */
    View.OnClickListener editClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BootstrapButton button = findViewById(R.id.update_button);
            BootstrapEditText idView = findViewById(R.id.selected_id);
            BootstrapEditText passwordView = findViewById(R.id.selected_password);
            BootstrapEditText remarks = findViewById(R.id.selected_remarks);
            Spinner spinner = findViewById(R.id.service_spinner);
            if(i == 1){
                i = 0;
                button.setEnabled(false);
                idView.setEnabled(false);
                passwordView.setEnabled(false);
                remarks.setEnabled(false);
                spinner.setEnabled(false);
                spinner.setClickable(false);
/*                button.setFocusable(false);
                idView.setFocusable(false);
                passwordView.setFocusable(false);
                remarks.setFocusable(false);*/
                //idView.setBackgroundColor(0xaa808080);
                //passwordView.setBackgroundColor(0xaa808080);
                //remarks.setBackgroundColor(0xaa808080);
                //setColorFilter(0xaa808080);
            } else {
                i = 1;
                button.setEnabled(true);
                idView.setEnabled(true);
                passwordView.setEnabled(true);
                remarks.setEnabled(true);
                spinner.setEnabled(true);
                spinner.setClickable(true);
/*                button.setFocusable(true);
                idView.setFocusable(true);
                passwordView.setFocusable(true);
                remarks.setFocusable(true);*/
                //idView.setBackgroundColor(0x66ff0000);
                //passwordView.setBackgroundColor(0x66ff0000);
                //remarks.setBackgroundColor(0x66ff0000);
            }
        }
    };
    /**
     * 終了ボタン
     */
    View.OnClickListener finishClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };
    /**
     * 更新ボタン
     * データ更新
     */
    View.OnClickListener updateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Spinner  spinner = findViewById(R.id.service_spinner);
            TextView spinnerGone = findViewById(R.id.service_spinner_gone);
            BootstrapEditText id = findViewById(R.id.selected_id);
            BootstrapEditText password = findViewById(R.id.selected_password);
            BootstrapEditText remarks = findViewById(R.id.selected_remarks);
            TextView idGone = findViewById(R.id.selected_id_gone);
            TextView passwordGone = findViewById(R.id.selected_password_gone);
            TextView remarksGone = findViewById(R.id.selected_remarks_gone);
            TextView uidGone = findViewById(R.id.selected_uid_gone);

            if(!ViewUtil.formValidate(id,password,remarks)){
                return;
            }
            DBHelper helper = new DBHelper(getApplicationContext());
            String spinnerPosition = String.valueOf(spinner.getSelectedItemPosition()+1);

            List<String> columnList = new ArrayList<>();
            //001
            if( !(id.getText().toString().equals(idGone.getText().toString())) ){
                columnList.add("id");
            }
            //010
            if( !(password.getText().toString().equals(passwordGone.getText().toString())) ){
                columnList.add("password");
            }
            //100
            if( !(remarks.getText().toString().equals(remarksGone.getText().toString())) ){
                columnList.add("remarks");
            }
            //1000
            if( !(spinnerPosition.equals(spinnerGone.getText().toString())) ){
                columnList.add("service_index");
            }
            if( columnList.size() > 0 ){
                SQLiteDatabase db = helper.getWritableDatabase();
                int res = helper.updateData(
                        db,
                        uidGone.getText().toString(),
                        id.getText().toString(),
                        password.getText().toString(),
                        remarks.getText().toString(),
                        spinnerPosition,
                        columnList
                );
                if (res == -1) {
                    Toast.makeText(getApplication(), "更新失敗", Toast.LENGTH_SHORT).show();
                }
                if (res == 0) {
                    Toast.makeText(getApplication(), "0更新", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication(), "更新成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
}
