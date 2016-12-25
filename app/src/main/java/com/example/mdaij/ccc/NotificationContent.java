package com.example.mdaij.ccc;

import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationContent extends AppCompatActivity {

    TextView title,content,tag;
    String shareText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_content);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                try {
                    startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    //ToastHelper.MakeShortText("Whatsapp have not been installed.");
                    Snackbar.make(view,"Whatsapp is not installed!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        Snackbar.make(fab, "Share on Whatsapp", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        title = (TextView)findViewById(R.id.ncTitleId);
        content = (TextView)findViewById(R.id.ncContentId);
        tag = (TextView)findViewById(R.id.ncTagId);



        content.setMovementMethod(LinkMovementMethod.getInstance());

        Intent intent = getIntent();
        Notification notification = (Notification) intent.getSerializableExtra("notification");
        title.setText(notification.getTitle());
        //content.setText(notification.getContent());
        content.setText(Html.fromHtml(notification.getContent()));
        String tagVal = notification.getTag();
        tag.setText(tagVal);
        if (tagVal.equals("android")){
            tag.setBackgroundColor(Color.rgb(27,170,84));
        }
        else if(tagVal.equals("web")){
            tag.setBackgroundColor(Color.rgb(128,20,160));
        }
        else if(tagVal.equals("ccc"))
            tag.setBackgroundColor(Color.rgb(30,109,188));
        else
            tag.setBackgroundColor(Color.MAGENTA);

        shareText = "*"+notification.getTitle()+"*"+"\n"+Html.fromHtml(notification.getContent())+"\n"+"_"+notification.getTag()+"_";

       // if(intent.getIntExtra("notifFlag",0)== 1){

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.cancel(intent.getIntExtra("notifId",001));
            //Toast.makeText(this,"Clearing"+intent.getIntExtra("notifId",0),Toast.LENGTH_SHORT).show();
       // }
       // else{
        //    Toast.makeText(this,"not Clearing",Toast.LENGTH_SHORT).show();
       // }
    }

}
