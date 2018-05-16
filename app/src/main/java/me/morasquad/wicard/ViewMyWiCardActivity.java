package me.morasquad.wicard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import me.morasquad.wicard.models.MyWicard;

public class ViewMyWiCardActivity extends AppCompatActivity {

    private AppCompatActivity activity = ViewMyWiCardActivity.this;
    private Context context = ViewMyWiCardActivity.this;
    private RecyclerView recyclerView;
    private ArrayList<MyWicard> myWicards;
    private WiCardRecyclerAdapter wiCardRecyclerAdapter;
    private SqliteHelper sqliteHelper;
    private ArrayList<MyWicard> filteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_wi_card);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("WiCard : My WiCards");


        recyclerView = (RecyclerView) findViewById(R.id.wicard_recycler);
        myWicards = new ArrayList<>();
        wiCardRecyclerAdapter = new WiCardRecyclerAdapter(myWicards,this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(wiCardRecyclerAdapter);

        sqliteHelper = new SqliteHelper(this);

        getDataFromSQLite();



    }

    private void getDataFromSQLite() {

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                myWicards.clear();
                myWicards.addAll(sqliteHelper.getMyWiCard());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                wiCardRecyclerAdapter.notifyDataSetChanged();
            }
        }.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == android.R.id.home){
            SendUsertoMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUsertoMainActivity() {

        Intent home = new Intent(ViewMyWiCardActivity.this, MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();
    }
}
