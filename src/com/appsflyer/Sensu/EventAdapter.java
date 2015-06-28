package com.appsflyer.Sensu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by golan on 1/22/15.
 */

public class EventAdapter extends BaseAdapter {
    public String name;
    ArrayList<Event> events = new ArrayList<Event>();
    public Activity context;

    public LayoutInflater inflater;

    public EventAdapter(Activity context, ArrayList<Event> arr_events) {
        super();
        this.context = context;
        this.events = arr_events;

        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public Event getEventById (int position) {

        return events.get(position);
    }

    public class ViewHolder {
        ImageView image;
        TextView txtName;
        RelativeLayout row;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);

            holder.image = (ImageView) convertView.findViewById(R.id.imageView);
            holder.txtName = (TextView) convertView.findViewById(R.id.textView);
            holder.row = (RelativeLayout) convertView.findViewById(R.id.lineItem);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();


        Event event = events.get(position);
        holder.txtName.setText(event.eventName);


        try {
            int status = event.check.getInt("status");
            if (status == 2) {
                holder.txtName.setTextColor(context.getResources().getColor(R.color.red_color));
                holder.image.setImageResource(R.drawable.error);

            }
            else if (status == 1) {
                holder.txtName.setTextColor(context.getResources().getColor(R.color.black_color));
                holder.image.setImageResource(R.drawable.warning);

            }
            else if (status == 0 || status > 2) {
                holder.txtName.setTextColor(context.getResources().getColor(R.color.black_color));
                holder.image.setImageResource(R.drawable.green_v);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;


    }
}