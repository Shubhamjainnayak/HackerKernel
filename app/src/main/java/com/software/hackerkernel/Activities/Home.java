package com.software.hackerkernel.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.software.hackerkernel.Adapter.HomeTabAdapter;
import com.software.hackerkernel.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    public static final String SHARED_PREF_NAME = "user_info";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView Personimages;
    TextView user_email;
    String personemail;
    ViewPager viewPager;
    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.viewpager);
        HomeTabAdapter homeTabAdapter=new HomeTabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(homeTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        sharedPreferences = Home.this.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        personemail = sharedPreferences.getString("email", "");
        Personimages = header.findViewById(R.id.imageView_sidebar);
        user_email = header.findViewById(R.id.user_email_sidebar);
        Picasso.with(getBaseContext()).load(R.drawable.image).transform(new CircleTransform()).into(Personimages);
        user_email.setText(personemail);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
            alertDialogBuilder.setMessage("Are you sure, You Want To Logout");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            editor.clear();
                            editor.apply();
                            Intent loginscreen=new Intent(Home.this,LoginActivity.class);
                            loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            loginscreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            loginscreen.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginscreen);
                            finishAffinity();
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
         if (id == R.id.logout) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Home.this);
            alertDialogBuilder.setMessage("Are you sure, You Want To Logout");
            alertDialogBuilder.setPositiveButton("yes",
                    new DialogInterface.OnClickListener() {
                        @SuppressLint("NewApi")
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SharedPreferences preferences =getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent loginscreen=new Intent(Home.this,LoginActivity.class);
                            loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            loginscreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            loginscreen.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                            loginscreen.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginscreen);
                            finishAffinity();
                            finish();
                        }
                    });

            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }

        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }
}
