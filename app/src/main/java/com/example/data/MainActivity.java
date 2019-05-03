package com.example.data;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText id ,email,reg,server;
    DataControl DC;
    AlertDialog errorInsertDialog;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:

                    return true;
                case R.id.navigation_notifications:

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
                if(!DC.addTuple(id.getText().toString(), email.getText().toString(),
                        reg.getText().toString(), server.getText().toString())) showError();
                DC.print();
            }
        });
    }

    @Override
    protected void onDestroy() {
        DC.close();
        super.onDestroy();
    }

    private void showError() {
        try {
            errorInsertDialog.dismiss();
        } catch (Exception e) {

        }
        errorInsertDialog = new AlertDialog.Builder(MainActivity.this).create();
        errorInsertDialog.setTitle("ERROR REGISTRATION");
        errorInsertDialog.setMessage(email.getText().toString() + " is already registered in " + server.getText().toString());
        errorInsertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        errorInsertDialog.show();
    }
}
