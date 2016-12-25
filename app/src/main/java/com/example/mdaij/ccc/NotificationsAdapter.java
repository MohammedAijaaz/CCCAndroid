package com.example.mdaij.ccc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mdaij on 12/23/2016.
 */
public class NotificationsAdapter extends ArrayAdapter<Notification> {

    public NotificationsAdapter(Context context, int resource, List<Notification> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.notification_item,parent,false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.titleid);
        TextView tags = (TextView) convertView.findViewById(R.id.tagid);

        Notification notification = getItem(position);
        String tag = notification.getTag();
        title.setText(notification.getTitle());
        tags.setText(tag);
        if (tag.equals("android")){
            tags.setTextColor(Color.rgb(27,170,84));
        }
        else if(tag.equals("web")){
            tags.setTextColor(Color.rgb(128,20,160));
        }
        else if(tag.equals("ccc"))
            tags.setTextColor(Color.rgb(30,109,188));
        else
            tags.setTextColor(Color.MAGENTA);

        return convertView;
    }
}
