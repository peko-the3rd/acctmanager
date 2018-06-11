package jp.thesaurus.acctmanager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.thesaurus.acctmanager.jp.thesaurus.utils.ViewUtil;

public class ListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int layoutID;
    List mailList;
    Bitmap[] photolist;
    List uidList;

    static class ViewHolder {
        //TextView text;
        ImageView img;
        TextView mail;
        TextView uid;
    }

    ListAdapter(Context context, int itemLayoutId,List<Map<String, String>> listData ){//List mails, List<Bitmap> icons ){

        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;

        int i = 0;
        mailList =  new ArrayList<String>();
        uidList=  new ArrayList<String>();
        photolist = new Bitmap[listData.size()];
        for(Map<String, String> m : listData){
            mailList.add(m.get("id"));
            Resources r = context.getResources();
            photolist[i] = ViewUtil.getServiceMipmap(m.get("service_index"),r);
            uidList.add(m.get("uid"));
            i++;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();
            holder.img = convertView.findViewById(R.id.row_img);
            holder.mail = convertView.findViewById(R.id.row_mail);
            holder.uid = convertView.findViewById(R.id.row_uid);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.img.setImageBitmap(photolist[position]);
        String id = mailList.get(position).toString();
        holder.mail.setText(id);
        String uid = uidList.get(position).toString();
        holder.uid.setText(uid);

        return convertView;
    }

    @Override
    public int getCount() {
        return mailList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
