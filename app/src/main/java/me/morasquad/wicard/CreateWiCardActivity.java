package me.morasquad.wicard;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateWiCardActivity extends AppCompatActivity {


    private EditText FullName, Address, MobileNumber, EmailAddress;
    private Button SaveData;
    private SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wi_card);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("WiCard : Create New WiCard");

        sqliteHelper = new SqliteHelper(this);
        FullName = (EditText) findViewById(R.id.fullName);
        Address = (EditText) findViewById(R.id.address);
        MobileNumber = (EditText) findViewById(R.id.mobileNumber);
        EmailAddress = (EditText) findViewById(R.id.email);
        SaveData = (Button) findViewById(R.id.save_data);


        SaveData.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String fullName = FullName.getText().toString();
                String address = Address.getText().toString();
                String mobileNumber = MobileNumber.getText().toString();
                String emailAddress = EmailAddress.getText().toString();

                if(TextUtils.isEmpty(fullName)){
                    Toast.makeText(CreateWiCardActivity.this, "You Need to Enter Full Name", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(emailAddress)){
                    Toast.makeText(CreateWiCardActivity.this, "You Need to Enter Email Address", Toast.LENGTH_SHORT).show();
                }else {

                    boolean result = sqliteHelper.saveWiCard(emailAddress, address, mobileNumber, fullName);

                    if(result){
                        FullName.setText("");
                        Address.setText("");
                        MobileNumber.setText("");
                        EmailAddress.setText("");
                        Toast.makeText(CreateWiCardActivity.this, "Your WiCard is Successfully Created!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(CreateWiCardActivity.this, "Failed Creating WiCard!", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });




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

        Intent home = new Intent(CreateWiCardActivity.this, MainActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(home);
        finish();
    }
}
