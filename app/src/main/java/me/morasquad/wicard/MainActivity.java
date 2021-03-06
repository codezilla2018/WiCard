package me.morasquad.wicard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private Button createWicard, recieveWiCard, savedWicard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        createWicard = (Button) findViewById(R.id.create_main);
        recieveWiCard = (Button) findViewById(R.id.recieve_main);
        savedWicard = (Button) findViewById(R.id.saved_main);


        createWicard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create = new Intent(MainActivity.this, CreateWiCardActivity.class);
                startActivity(create);
            }
        });

        recieveWiCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent recieve = new Intent(MainActivity.this, RecieveWiCardActivity.class);
                startActivity(recieve);
            }
        });

        savedWicard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent saved = new Intent(MainActivity.this, RecievedWiCardActivity.class);
                startActivity(saved);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });


    }

    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()){

            case R.id.home:

                break;

            case R.id.create_wicard:
                Intent newCard = new Intent(MainActivity.this, CreateWiCardActivity.class);
                startActivity(newCard);
                break;

            case R.id.my_wicard:
                Intent create = new Intent(MainActivity.this, ViewMyWiCardActivity.class);
                startActivity(create);
                break;

            case R.id.get_wicard:
                Intent recieve = new Intent(MainActivity.this, RecieveWiCardActivity.class);
                startActivity(recieve);
                break;

            case R.id.saved_wicards:
                Intent recieved = new Intent(MainActivity.this, RecievedWiCardActivity.class);
                startActivity(recieved);
                break;


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);


    }
}
