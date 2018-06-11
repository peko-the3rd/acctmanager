package jp.thesaurus.acctmanager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.thesaurus.acctmanager.jp.thesaurus.utils.DBHelper;
import jp.thesaurus.acctmanager.jp.thesaurus.utils.ViewUtil;

public class AcctListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private Dialog mDialog;
    private CancellationSignal mCancellationSignal;

    static SQLiteDatabase db;
    private List<Map<String, String>> listData = new ArrayList<>();

    static List<String> names = new ArrayList<String>();
    static List<Bitmap> icons = new ArrayList<Bitmap>();
    static List<String> uids  = new ArrayList<String>();
    static ArrayAdapter<String> adapter;
    ListView listView;
    ImageView imageView;
    private static final String TAG = "AcctListActivity";

    public static final int PREFERENCE_INIT = 0;
    public static final int PREFERENCE_BOOTED = 1;


    //データ保存
    private void setState(int state) {
        // SharedPreferences設定を保存
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.edit().putInt("InitState", state).commit();
    }

    //データ読み出し
    private int getState() {
        // 読み込み
        int state;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        state = sp.getInt("InitState", PREFERENCE_INIT);
        return state;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listView = null;
        imageView =null;
        listData = null;
        names = new ArrayList<String>();
        icons = new ArrayList<Bitmap>();
        uids  = new ArrayList<String>();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acct_list);

        if (savedInstanceState == null) {
            useFingerprint();
            //createList();
        }

    }
    @Override
    protected void onRestart() {

        super.onRestart();
        Log.d("onRestart","-----------------------onRestart");

        listView = null;
        imageView =null;
        listData = null;
        names = new ArrayList<String>();
        icons = new ArrayList<Bitmap>();
        uids  = new ArrayList<String>();

        setContentView(R.layout.activity_acct_list);
        createList();
    }
/*    @Override
    protected void onResume() {
        super.onResume();
        listView = null;
        imageView =null;
        listData = null;
        names = new ArrayList<String>();
        icons = new ArrayList<Bitmap>();
        uids  = new ArrayList<String>();

        setContentView(R.layout.activity_acct_list);

        // ダイアログの作成と表示
        if(PREFERENCE_INIT != getState() ){
            //初回起動時のみ表示する
            useFingerprint();
            //setState(PREFERENCE_BOOTED);
        } else{
            createList();
        }
        setState(PREFERENCE_BOOTED);
    }*/

    private void useFingerprint() {
        if (checkSelfPermission(Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        FingerprintManager fingerprintManager = (FingerprintManager) getSystemService(Context.FINGERPRINT_SERVICE);
        if (fingerprintManager.isHardwareDetected() || fingerprintManager.hasEnrolledFingerprints()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("指紋認証");
            builder.setMessage("指紋センサーに触れてください");
            View alertView = getLayoutInflater().inflate(R.layout.alert_layout, null);
            builder.setView(alertView);
            builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCancellationSignal.cancel();
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mCancellationSignal.cancel();
                }
            });
            mDialog = builder.show();

            mCancellationSignal = new CancellationSignal();

            fingerprintManager.authenticate(null, mCancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    Toast.makeText(AcctListActivity.this, "onAuthentication-Error: " + errString, Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    Toast.makeText(AcctListActivity.this, "onAuthentication-Help: " + helpString, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                    Toast.makeText(AcctListActivity.this, "指紋認証成功", Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                    createList();
                }

                @Override
                public void onAuthenticationFailed() {
                    Toast.makeText(AcctListActivity.this, "onAuthentication-Failed", Toast.LENGTH_SHORT).show();
                }
            }, new Handler());
        }
    }

    /**
     * 一覧描画用関数
     *
     */
    private void createList() {
        //imageView = (ImageView) findViewById(R.id.gifView);
        //GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(imageView);
        //Glide.with(this).load(R.drawable.matrix).into(target);

        DBHelper helper = new DBHelper(getApplicationContext());
        db = helper.getWritableDatabase();

        Date date = new Date();
        listData = helper.getAllAccountList(date);

        ListView listView = (ListView)findViewById(R.id.listview);

        for(Map<String, String> m : listData){
            names.add(m.get("id"));
            uids.add(m.get("uid"));
            Resources r = getResources();
            icons.add(ViewUtil.getServiceMipmap(m.get("service_index"),r));
        }
        BaseAdapter adapter = new ListAdapter(this.getApplicationContext(),
                R.layout.row, listData);//names, icons);

        listView.setAdapter(adapter);
        // クリックリスナーをセット
        listView.setOnItemClickListener(this);
        findViewById(R.id.fab).setOnClickListener(fabClickListener);

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Log.d("","--------------------a---------------------");
        Intent intent = new Intent(
                this.getApplicationContext(), AcctDetailActivity.class);

        // clickされたpositionのtextとphotoのID
        String selectedText = names.get(position);
        Bitmap selectedIcon = icons.get(position);
        String selectedUid = uids.get(position);
        // インテントにセット
        intent.putExtra("id", selectedText);
        intent.putExtra("icon", selectedIcon);
        intent.putExtra("uid", selectedUid);

        // SubActivityへ遷移
        startActivity(intent);


    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.v("LifeCycle", "onDestroy");
        imageView = null;
        listView = null;
        listData = new ArrayList<>();
    }

    /**
     * Fabボタン
     *
     */
    View.OnClickListener fabClickListener  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), AcctEntryActivity.class);
            startActivity(intent);
            //Toast.makeText(v.getContext(),"FABが押されました",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void finalize() throws Throwable {
        try {
            super.finalize();
        } finally {
            names = new ArrayList<String>();
            icons = new ArrayList<Bitmap>();
            imageView = null;
            listView = null;
            listData = new ArrayList<>();
        }
    }

}
