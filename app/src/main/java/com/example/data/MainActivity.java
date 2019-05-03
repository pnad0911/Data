package com.example.data;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText id ,email,reg,server;
    DataControl DC;
    AlertDialog InsertDialog;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentMain = new Intent(MainActivity.this ,
                            MainActivity.class);
                    MainActivity.this.startActivity(intentMain);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intentSearch = new Intent(MainActivity.this ,
                            Main2Activity.class);
                    MainActivity.this.startActivity(intentSearch);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        button = (Button) findViewById(R.id.Enter);
        id = (EditText) findViewById(R.id.ID);
        email = (EditText) findViewById(R.id.Email);
        reg = (EditText) findViewById(R.id.Reg);
        server = (EditText) findViewById(R.id.Server);

        DC = new DataControl(this);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                show(DC.addTuple(id.getText().toString(), email.getText().toString(),
                        reg.getText().toString(), server.getText().toString()));
                DC.print();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    protected void onDestroy() {
        DC.close();
        super.onDestroy();
    }

    private void show(boolean success) {
        try {
            InsertDialog.dismiss();
        } catch (Exception e) {

        }
        InsertDialog = new AlertDialog.Builder(MainActivity.this).create();
        InsertDialog.setTitle("REGISTRATION");
        InsertDialog.setMessage(success ? "Registration Successful" :
                (id.getText().toString() + " is already registered"));
        InsertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        InsertDialog.show();
    }
}
