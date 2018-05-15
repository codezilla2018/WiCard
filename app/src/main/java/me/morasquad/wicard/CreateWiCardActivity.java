package me.morasquad.wicard;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateWiCardActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private EditText FullName, Address, MobileNumber, EmailAddress;
    private Button SaveData;
    private SqliteHelper sqliteHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wi_card);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("WiCard : Create New WiCard");

        sqliteHelper = new SqliteHelper(this);
        FullName = (EditText) findViewById(R.id.fullName);
        Address = (EditText) findViewById(R.id.address);
        MobileNumber = (EditText) findViewById(R.id.mobileNumber);
        EmailAddress = (EditText) findViewById(R.id.email);
        SaveData = (Button) findViewById(R.id.save_data);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UserMenuSelector(item);
                return false;
            }
        });

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

    private void UserMenuSelector(MenuItem item) {

        switch (item.getItemId()){

            case R.id.home:
                Intent home = new Intent(CreateWiCardActivity.this, MainActivity.class);
                home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(home);
                break;

            case R.id.create_wicard:
                break;

            case R.id.my_wicard:
                Toast.makeText(this, "My WiCard", Toast.LENGTH_SHORT).show();
                break;

            case R.id.send_wicard:
                Toast.makeText(this, "Send WiCard", Toast.LENGTH_SHORT).show();
                break;

            case R.id.get_wicard:
                Toast.makeText(this, "Get WiCard", Toast.LENGTH_SHORT).show();
                break;

            case R.id.saved_wicards:
                Toast.makeText(this, "Save WiCards", Toast.LENGTH_SHORT).show();
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
