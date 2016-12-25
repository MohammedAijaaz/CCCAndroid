package com.example.mdaij.ccc;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String userName, uid;
    ProgressBar progressBar;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
   // private FirebaseAuth mFirebaseAuth;
    //private FirebaseAuth.AuthStateListener mAuthStateListener;
    //public static final int RC_SIGN_IN = 1;

    ListView mListView;
    //NotificationsAdapter mNotificationsAdapter;
    FirebaseListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

      /*  mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    onSignInInitialize(user.getDisplayName());
                    userName = user.getDisplayName();
                    uid = user.getUid();
                } else {
                    onSignOutCleanUp();
                    //user is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build()))
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };*/

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("notifications");

        mListView = (ListView) findViewById(R.id.listView);

       /* List<Notification> list = new ArrayList<>();
        mNotificationsAdapter = new NotificationsAdapter(this, R.layout.notification_item, list);
        mListView.setAdapter(mNotificationsAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Notification notification = mNotificationsAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, NotificationContent.class);
                intent.putExtra("notification", (Serializable) notification);
                startActivity(intent);
            }
        });*/

        mAdapter = new FirebaseListAdapter<Notification>(this,Notification.class,R.layout.notification_item, mDatabaseReference) {
            @Override
            protected void populateView(View view, Notification notification, int position) {
                ((TextView)view.findViewById(R.id.titleid)).setText(notification.getTitle());
                ((TextView)view.findViewById(R.id.tagid)).setText(notification.getTag());
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Notification notification =(Notification) mAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, NotificationContent.class);
                intent.putExtra("notification", (Serializable) notification);
                startActivity(intent);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //if(mAdapter.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
        //}

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Notification notification = dataSnapshot.getValue(Notification.class);
                NotificationCompat.Builder mBuilder =
                        (NotificationCompat.Builder) new NotificationCompat.Builder(MainActivity.this)
                                .setSmallIcon(R.drawable.logo3dsmall)
                                .setContentTitle(notification.getTitle())
                                .setContentText(Html.fromHtml(notification.getContent()));

                int notificationId = 001;
                Intent resultIntent = new Intent(getApplicationContext(), NotificationContent.class);
                resultIntent.putExtra("notification",(Serializable)notification);
                resultIntent.putExtra("notifFlag",1);
                resultIntent.putExtra("notifId",notificationId);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                stackBuilder.addParentStack(NotificationContent.class);
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(notificationId, mBuilder.build());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //attachDatabaseReadListener();

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                //Toast.makeText(MainActivity.this,"Signed In!",Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), "Signed In", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else if (resultCode == RESULT_CANCELED) {
                Snackbar.make(findViewById(android.R.id.content), "Sign In cancelled!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                finish();
            }
        }
    }*/

   /* public void onSignInInitialize(String username) {
        if (username != null) {
            // Toast.makeText(MainActivity.this,"Welcome "+username,Toast.LENGTH_SHORT).show();
            Snackbar.make(findViewById(android.R.id.content), "Welcome " + username, Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        } else
            Snackbar.make(findViewById(android.R.id.content), "Welcome ", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        attachDatabaseReadListener();
    }

    public void onSignOutCleanUp() {
        Snackbar.make(findViewById(android.R.id.content), "Signed out!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        mNotificationsAdapter.clear();
        uid = null;
        userName = null;
        detachDatabaseListener();
    }*/

   /* private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Notification notification = dataSnapshot.getValue(Notification.class);
                    mNotificationsAdapter.add(notification);
                    progressBar.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDatabaseReference.addChildEventListener(mChildEventListener);
        }
    }*/


   /* private void detachDatabaseListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
            mChildEventListener = null;
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
       // mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        // progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onPause() {
        super.onPause();
      //  if (mAuthStateListener != null) {
       //     mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        //}
     //   detachDatabaseListener();
     //   mNotificationsAdapter.clear();
       // progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAdapter.cleanup();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            /*AuthUI.getInstance().signOut(this);
            userName = null;
            uid = null;
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //if (id == R.id.nav_camera) {
            // Handle the camera action
        //}
         if (id == R.id.nav_gallery) {

        } //else if (id == R.id.nav_slideshow) {

      //  } else if (id == R.id.nav_manage) {

      //  } else if (id == R.id.nav_share) {

    //    }
    else if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this,About.class);
             startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
