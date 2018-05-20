package me.morasquad.wicard;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.ArrayList;

import me.morasquad.wicard.models.RecievedWiCards;

public class RecievedWiCardActivity extends AppCompatActivity {

    private AppCompatActivity activity = RecievedWiCardActivity.this;
    private Context context = RecievedWiCardActivity.this;
    private RecyclerView recyclerView;
    private ArrayList<RecievedWiCards> recievedWiCards;
    private RecievedWiCardRecyclerAdapter recievedWiCardRecyclerAdapter;
    private SqliteHelper sqliteHelper;
    private ArrayList<RecievedWiCards> filteredList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieved_wi_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("WiCard : Received WiCards");

        recyclerView = (RecyclerView) findViewById(R.id.rec_wicard_recycler);
        recievedWiCards = new ArrayList<>();
        recievedWiCardRecyclerAdapter = new RecievedWiCardRecyclerAdapter(recievedWiCards, this);

        RecyclerView.LayoutManager mlayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recievedWiCardRecyclerAdapter);

        sqliteHelper = new SqliteHelper(this);

        getDataFromSqlite();
     }

    private void getDataFromSqlite() {

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                recievedWiCards.clear();
                recievedWiCards.addAll(sqliteHelper.getRecievedWiCards());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                recievedWiCardRecyclerAdapter.notifyDataSetChanged();
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

        Intent home = new Intent(RecievedWiCardActivity.this, MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();
    }
}
