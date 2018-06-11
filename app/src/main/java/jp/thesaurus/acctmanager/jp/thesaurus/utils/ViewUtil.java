package jp.thesaurus.acctmanager.jp.thesaurus.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.api.defaults.DefaultBootstrapBrand;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.thesaurus.acctmanager.Constants;
import jp.thesaurus.acctmanager.R;
import jp.thesaurus.acctmanager.ServiceBean;

public class ViewUtil {

    /**
     * スピナーの画像取得
     */
    public static Bitmap getServiceMipmap(String serviceId,Resources r) {

        Bitmap bmp = null;

        for(Map.Entry<String, ServiceBean> entry :Constants.SERVICE_BEANS.entrySet()){
            ServiceBean sb;
            sb = entry.getValue();//entry.getKey()
            if(serviceId.equals(entry.getKey())){
                bmp = BitmapFactory.decodeResource(r, sb.getServicMipmap());
            }
        }
        return bmp;
    }

    /**
     * 入力チェック
     */
    public static void formValidate(BootstrapEditText id, BootstrapEditText password,BootstrapEditText remarks) {

        String displayId = id.getText().toString();
        String displayPassword = password.getText().toString();
        String displayRemarks = remarks.getText().toString();
        if (displayId.isEmpty() && displayPassword.isEmpty() && displayRemarks.isEmpty()) {
            id.setError("文字を入力してください");
            password.setError("文字を入力してください");
            remarks.setError("文字を入力してください");
            id.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
            password.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
            remarks.setBootstrapBrand(DefaultBootstrapBrand.DANGER);
            return;
        }
        return;
    }
}
