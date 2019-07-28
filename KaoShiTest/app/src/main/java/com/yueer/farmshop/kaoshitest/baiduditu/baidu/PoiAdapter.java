package com.yueer.farmshop.kaoshitest.baiduditu.baidu;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.yueer.farmshop.kaoshitest.R;

import java.util.List;


/**
 */
public class PoiAdapter  extends BaseAdapter {
    private Context context;
    private List<PoiInfo> pois;
    private LinearLayout linearLayout;

    public PoiAdapter(Context context, List<PoiInfo> pois) {
        this.context = context;
        this.pois = pois;
    }

    @Override
    public int getCount() {
        return pois.size();
    }

    @Override
    public Object getItem(int position) {
        return pois.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.locationpois_item, null);
            linearLayout = (LinearLayout) convertView.findViewById(R.id.locationpois_linearlayout);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.iv_gps.setImageResource(R.mipmap.ic_launcher);
            holder.locationpoi_name.setTextColor(Color.parseColor("#000000"));
            holder.locationpoi_address.setTextColor(Color.parseColor("#000000"));
        }
        if (position == 0 && linearLayout.getChildCount() < 2) {
            ImageView imageView = new ImageView(context);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(32, 32);
            imageView.setLayoutParams(params);
            imageView.setBackgroundColor(Color.TRANSPARENT);
            imageView.setImageResource(R.mipmap.ic_launcher);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            linearLayout.addView(imageView, 0, params);
            holder.locationpoi_name.setTextColor(Color.parseColor("#000000"));
        }
        PoiInfo poiInfo = pois.get(position);
        holder.locationpoi_name.setText(poiInfo.name);
        holder.locationpoi_address.setText(poiInfo.address);
        return convertView;
    }

    class ViewHolder {
        ImageView iv_gps;
        TextView locationpoi_name;
        TextView locationpoi_address;

        ViewHolder(View view) {
            locationpoi_name = (TextView) view.findViewById(R.id.locationpois_name);
            locationpoi_address = (TextView) view.findViewById(R.id.locationpois_address);
            iv_gps = (ImageView) view.findViewById(R.id.iv_gps);
        }
    }
}
