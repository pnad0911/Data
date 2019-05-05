package com.example.data;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.SearchView;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    SearchAdapter searchAdapter;
    AlertDialog InsertDialog, subDialog;
    SearchView emailV, serverV;
    private int sIdx;

    Activity Main;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Intent intentMain = new Intent(SearchActivity.this ,
                            MainActivity.class);
                    SearchActivity.this.startActivity(intentMain);
                    return true;
                case R.id.navigation_dashboard:
                    Intent intentSearch = new Intent(SearchActivity.this ,
                            SearchActivity.class);
                    SearchActivity.this.startActivity(intentSearch);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ListView sListView =(ListView)findViewById(R.id.simpleListView);
        searchAdapter = new SearchAdapter(this);
        sListView.setAdapter(searchAdapter);
        sListView.setClickable(true);
        sListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                show(position);
            }
        });

        emailV = (SearchView) findViewById(R.id.searchEmail);
        serverV = (SearchView) findViewById(R.id.searchServer);

        emailV.setIconified(false); emailV.setQueryHint("Enter email address here");
        serverV.setIconified(false); serverV.setQueryHint("Enter server address here");

        emailV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.setEmail().getFilter().filter(newText);
                return true;
            }
        });

        serverV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.setServer().getFilter().filter(newText);
                return true;
            }
        });
    }

    private void show(int position) {
        try {
            InsertDialog.dismiss();
            subDialog.dismiss();
        } catch (Exception e) {

        }
        sIdx = position;
        final EditText edittext = new EditText(SearchActivity.this);
        InsertDialog = new AlertDialog.Builder(SearchActivity.this).create();
        InsertDialog.setTitle("REGISTRATION");
        InsertDialog.setMessage("Do you want to change device ID?");
        InsertDialog.setView(edittext);
        edittext.setHint("Enter new device ID");
        InsertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Change",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String ingEmail = searchAdapter.getSearchItem(sIdx).email;
                        String ingSer = searchAdapter.getSearchItem(sIdx).server;
                        showSuccess(searchAdapter.DC.update(edittext.getText().toString(),
                                searchAdapter.getSearchItem(sIdx)));

                        searchAdapter.getFilter().filter(ingEmail + " " + ingSer);
                        dialog.dismiss();
                    }
                });
        InsertDialog.show();
    }

    private void showSuccess(boolean bool) {
        try {
            InsertDialog.dismiss();
            subDialog.dismiss();
        } catch (Exception e) {

        }
        subDialog = new AlertDialog.Builder(SearchActivity.this).create();
        subDialog.setTitle("UPDATE");
        subDialog.setMessage(bool ? "Successful update" : "This ID is already taken");
        subDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        subDialog.show();
    }
//    @Override
//    public boolean onQueryTextSubmit(String query) {
//        return false;
//    }
//
//    @Override
//    public boolean onQueryTextChange(String newText) {
//        searchAdapter.getFilter().filter(newText);
//        return true;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.options_menu, menu);
//
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.search).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));
//        searchView.setSubmitButtonEnabled(true);
//        searchView.setOnQueryTextListener(this);
//        return true;
//    }
}
