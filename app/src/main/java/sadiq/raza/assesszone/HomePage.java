package sadiq.raza.assesszone;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView navName,navEmail;
    ImageView imageView ;
    public static ListView listView;
    static String s_name,s_email;
    ProgressBar progressBar;
    CardView cardView;
    ArrayList<String> myList;
    static ProgressDialog openTestPb;
    private ArrayList<AvailableTestDetails> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view=navigationView.getHeaderView(0);
        Intent intent=getIntent();
        Bundle extras =intent.getExtras();
        imageView=view.findViewById(R.id.imageView);

         listView= findViewById(R.id.list_view);
       AvailableTestBt abt= new AvailableTestBt(HomePage.this,true);
       abt.execute();



        assert extras != null;
        s_name=extras.getString("myStr1");
        s_email=extras.getString("myStr2");
        navName = view.findViewById(R.id.navName);
        navEmail=view.findViewById(R.id.navEmail);
        s_name="   "+s_name;
        s_email="   "+s_email;
        navName.setText(s_name);
        navEmail.setText(s_email);
        openTestPb=new ProgressDialog(HomePage.this);
        openTestPb.setMessage("Pleae Wait...");
        TextView tvName=findViewById(R.id.textView2);
        tvName.setText("Welcome "+s_name);

        //ProgressBar


        myList = new ArrayList<>();

        /*cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //openTestList();
                new AvailableTestBt(HomePage.this).execute();

            }
        });*/


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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.practice) {
            // Handle the camera action
            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.attepmt) {
            new AvailableTestBt(HomePage.this,false).execute();

        } else if (id == R.id.result) {
            //Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.profile) {
            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_share) {
            ShowResult showResult= new ShowResult(HomePage.this);
            showResult.execute();
            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "Under Construction", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}