package me.morasquad.wicard;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditMyWiCardActivity extends AppCompatActivity {

    EditText email,address,mobileNo,fullName;
    Button upadateBtn;
    String intentEmail;
    SqliteHelper sqliteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_wi_card);

        email = (EditText) findViewById(R.id.email_edit);
        address = (EditText) findViewById(R.id.address_edit);
        mobileNo = (EditText) findViewById(R.id.mobileNumber_edit);
        fullName = (EditText) findViewById(R.id.fullName_edit);
        upadateBtn = (Button) findViewById(R.id.update_data);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("WiCard : Edit My WiCard");

        Intent intent = getIntent();
        intentEmail = intent.getStringExtra("email");

        loadWiCard();

        upadateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String full_Name = fullName.getText().toString();
                String emailAdrress = email.getText().toString();
                String mobileNumber = mobileNo.getText().toString();
                String Address = address.getText().toString();

                if(TextUtils.isEmpty(full_Name)){
                    Toast.makeText(EditMyWiCardActivity.this, "You Need to Enter Full Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(emailAdrress)){
                    Toast.makeText(EditMyWiCardActivity.this, "You Need to Enter Email Address", Toast.LENGTH_SHORT).show();
                }else {

                    boolean result = sqliteHelper.saveWiCard(emailAdrress, Address, mobileNumber, full_Name);

                    if(result){
                        Intent back = new Intent(EditMyWiCardActivity.this, ViewMyWiCardActivity.class);
                        back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(back);
                        Toast.makeText(getApplicationContext(), "Your WiCard is Successfully Created!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(), "Failed Creating WiCard!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


    }

    private void loadWiCard() {
        sqliteHelper = new SqliteHelper(getApplicationContext());
        Cursor cursor = sqliteHelper.getUser(intentEmail);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();

            String full_Name = cursor.getString(cursor.getColumnIndex("fullName"));
            String emailAdrress = cursor.getString(cursor.getColumnIndex("email"));
            String mobileNumber = cursor.getString(cursor.getColumnIndex("mobileNumber"));
            String Address = cursor.getString(cursor.getColumnIndex("address"));

            fullName.setText(full_Name);
            email.setText(emailAdrress);
            mobileNo.setText(mobileNumber);
            address.setText(Address);
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }else {
            fullName.setText("");
            email.setText("");
            mobileNo.setText("");
            address.setText("");
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

        Intent home = new Intent(EditMyWiCardActivity.this, MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();

    }
}
