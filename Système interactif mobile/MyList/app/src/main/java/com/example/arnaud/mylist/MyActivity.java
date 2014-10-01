package com.example.arnaud.mylist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class MyActivity extends Activity {

    private ListView lv;
    private MyAdapter a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        lv = (ListView) findViewById(R.id.dalist);
        a = new MyAdapter(this);
        lv.setAdapter(a);

        a.add("Handle");
        a.add("action");
        a.add("bar");
        a.add("item");
        a.add("clicks");
        a.add("here.");
        a.add("The");
        a.add("action");
        a.add("bar");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
