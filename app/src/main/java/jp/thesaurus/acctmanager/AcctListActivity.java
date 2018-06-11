package jp.thesaurus.acctmanager;

import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.thesaurus.acctmanager.jp.thesaurus.utils.DBHelper;
import jp.thesaurus.acctmanager.jp.thesaurus.utils.ViewUtil;

public class AcctListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    static SQLiteDatabase db;
    private List<Map<String, String>> listData = new ArrayList<>();

    static List<String> names = new ArrayList<String>();
    static List<Bitmap> icons = new ArrayList<Bitmap>();
    static List<String> uids  = new ArrayList<String>();
    static ArrayAdapter<String> adapter;
    ListView listView;
    ImageView imageView;

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

        this.createList();

    }
    @Override
    protected void onResume() {
        super.onResume();
        listView = null;
        imageView =null;
        listData = null;
        names = new ArrayList<String>();
        icons = new ArrayList<Bitmap>();
        uids  = new ArrayList<String>();

        setContentView(R.layout.activity_acct_list);

        this.createList();
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
