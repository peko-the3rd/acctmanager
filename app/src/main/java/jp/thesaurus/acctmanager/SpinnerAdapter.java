package jp.thesaurus.acctmanager;

import android.util.Log;
import android.widget.BaseAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpinnerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int layoutID;

    List serviceKey = new ArrayList<String>();
    List serviceName = new ArrayList<String>();
    List serviceMipmap = new ArrayList<Integer>();

    static class ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView indexViewGone;
    }

    SpinnerAdapter(Context context,
                int itemLayoutId,
                Map<String, ServiceBean> spinnerMap){

        inflater = LayoutInflater.from(context);
        layoutID = itemLayoutId;


        for (Map.Entry<String, ServiceBean> entry : spinnerMap.entrySet()) {
            ServiceBean sb;
            sb = entry.getValue();
            serviceName.add(sb.getServicName());
            serviceMipmap.add(sb.getServicMipmap());
            serviceKey.add(entry.getKey());
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(layoutID, null);
            holder = new ViewHolder();

            holder.imageView = convertView.findViewById(R.id.s_image_view);
            holder.textView = convertView.findViewById(R.id.s_name_view);
            holder.indexViewGone = convertView.findViewById(R.id.s_index_view_gone);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageResource(Integer.parseInt(serviceMipmap.get(position).toString()));
        holder.textView.setText(serviceName.get(position).toString());
        holder.indexViewGone.setText(serviceKey.get(position).toString());

        return convertView;
    }

    @Override
    public int getCount() {
        return serviceName.size();
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
