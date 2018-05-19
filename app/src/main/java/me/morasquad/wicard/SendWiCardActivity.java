package me.morasquad.wicard;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

public class SendWiCardActivity extends AppCompatActivity {

    String intentEmail;
    TextView connectionStatus;
    TextView fullNameText;
    TextView emailText;
    TextView addressText;
    TextView mobileNumText;
    Button wicardSendBtn;
    SqliteHelper sqliteHelper;
    String fullName, email, mobileNumber, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_wi_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("WiCard : Send WiCard");

        Intent intent = getIntent();
        intentEmail = intent.getStringExtra("email");

        fullNameText = (TextView) findViewById(R.id.fullNameTxt);
        emailText = (TextView) findViewById(R.id.emailTxt);
        addressText = (TextView) findViewById(R.id.addressTxt);
        mobileNumText = (TextView) findViewById(R.id.mobileNumTxt);

        wicardSendBtn = (Button) findViewById(R.id.sendWiCard);

        connectionStatus.setText(intentEmail);

        loadWiCard();

    }

    private void loadWiCard() {

        sqliteHelper = new SqliteHelper(getApplicationContext());
        Cursor cursor = sqliteHelper.getUser(intentEmail);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            fullName = cursor.getString(cursor.getColumnIndex("fullName"));
            email = cursor.getString(cursor.getColumnIndex("email"));
            mobileNumber = cursor.getString(cursor.getColumnIndex("mobileNumber"));
            address = cursor.getString(cursor.getColumnIndex("address"));

            fullNameText.setText(fullName);
            emailText.setText(email);
            mobileNumText.setText(mobileNumber);
            addressText.setText(address);
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }else {
            fullNameText.setText("");
            emailText.setText("");
            mobileNumText.setText("");
            addressText.setText("");
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        int id  = item.getItemId();

        if(id == android.R.id.home){
            SendUsertoMainActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void SendUsertoMainActivity() {

        Intent home = new Intent(SendWiCardActivity.this, MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();
    }
}
