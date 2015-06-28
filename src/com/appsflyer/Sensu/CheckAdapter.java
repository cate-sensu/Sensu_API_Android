package com.appsflyer.Sensu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by golan on 1/25/15.
 */
public class CheckAdapter extends ArrayAdapter<Check> {

    public CheckAdapter(Context context, ArrayList<Check> checks) {
        super(context, 0, checks);
    }

    private static class ViewHolder {
        TextView name;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Check check = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_event, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.tvEventName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(check.checkName);
        // Return the completed view to render on screen
        return convertView;
    }
}
