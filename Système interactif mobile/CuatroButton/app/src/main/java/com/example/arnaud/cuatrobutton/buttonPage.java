package com.example.arnaud.cuatrobutton;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class buttonPage extends Activity {

    private Button b1;
    private Button b2;
    private Button b3;
    private Button b4;

    private enum State {s1, s2, s3, s4};
    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_page);
        initButtons();
        state = State.s1;
        updateStates();
    }

    private void initButtons() {
        b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = State.s2;
                updateStates();
            }
        });
        b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = State.s3;
                updateStates();
            }
        });
        b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = State.s4;
                updateStates();
            }
        });
        b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = State.s1;
                updateStates();
            }
        });
    }

    private void updateStates() {
        switch (state){
            case s1:
                b1.setEnabled(true);
                b2.setEnabled(false);
                b3.setEnabled(false);
                b4.setEnabled(false);
                break;
            case s2:
                b1.setEnabled(false);
                b2.setEnabled(true);
                b3.setEnabled(false);
                b4.setEnabled(false);
                break;
            case s3:
                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setEnabled(true);
                b4.setEnabled(false);
                break;
            case s4:
                b1.setEnabled(false);
                b2.setEnabled(false);
                b3.setEnabled(false);
                b4.setEnabled(true);
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.button_page, menu);
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
