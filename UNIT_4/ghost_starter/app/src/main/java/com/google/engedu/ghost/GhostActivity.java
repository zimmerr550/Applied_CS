package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.*;
import java.lang.*;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary MyDictionary;
    private boolean userTurn = false;
    private Random random = new Random();

//    SimpleDictionary simpleDictionary;/////created instance to avoid making methods static.
    Button cBtn;
    Button rBtn;
    Button rst;
    TextView lText;
    TextView uScr;
    TextView cScr;
    TextView sText;
    static String wFrag="";
    String strNl="";
    String tempCheck="";
    static final String back="MYSAVE";
    static final String backUsr="MYSAVEUSR";
    static final String backCmp="MYSAVECMP";

    static int user=0;
    static int comp=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();


        try {
            InputStream inputStream = assetManager.open("words.txt");
            MyDictionary = new FastDictionary(inputStream);
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }

        cBtn = (Button) findViewById(R.id.cButton);
        rBtn = (Button) findViewById(R.id.rButton);
        rst=(Button)findViewById(R.id.rstAll);
        lText = (TextView) findViewById(R.id.lT);
        sText = (TextView) findViewById(R.id.sT);
        uScr=(TextView)findViewById(R.id.usrScr);
        cScr=(TextView)findViewById(R.id.cmpScr);
        lText.setText("");

        onStart(null);
        cBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                challenge();
            }
        });

        rBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lText.setText("");
                sText.setText("");
                wFrag="";
                cBtn.setEnabled(true);
                onStart(null);
            }
        });


        rst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lText.setText("");
                sText.setText("");
                wFrag="";
                user=0;
                comp=0;
                uScr.setText(user+"");
                cScr.setText(comp+"");
                cBtn.setEnabled(true);
                onStart(null);
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(back,wFrag);
        savedInstanceState.putInt(backCmp,comp);
        savedInstanceState.putInt(backUsr,user);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        wFrag=savedInstanceState.getString(back);
        user=savedInstanceState.getInt((backUsr));
        comp=savedInstanceState.getInt(backCmp);
        lText.setText(wFrag);
        uScr.setText(user+"");
        cScr.setText(comp+"");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    public void challenge()
    {

        if(wFrag.isEmpty())
        {
            sText.setText("Enter a char first");
            finishGame();
        }

        else
        {
            wFrag=wFrag.toLowerCase();
            if ((wFrag.length() >= 4) && MyDictionary.isWord(wFrag)) {
                sText.setText(wFrag + " is valid word.USER is the Winner !!");
                user++;
                uScr.setText(user+"");
                finishGame();
                ///comp was the last one to enter letter
            } else if (!MyDictionary.isWord(wFrag) && !MyDictionary.getAnyWordStartingWith(wFrag).isEmpty()) {
                //check if anyWordstartingwith(wFrag) exists in ArrayList words
                String testStr = MyDictionary.getAnyWordStartingWith(wFrag);
                if (MyDictionary.isWord(testStr)) ;
                sText.setText("Computer Wins ! Possible word:" + testStr);
                comp++;
                cScr.setText(comp+"");
                finishGame();

            } else if (MyDictionary.getAnyWordStartingWith(wFrag)==null) {
                sText.setText(wFrag + " is invalid prefix.USER is the Winner !!");
                user++;
                uScr.setText(user+"");
                finishGame();
            }
        }

    }

    void finishGame()
    {
        userTurn=false;
        cBtn.setEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
//        userTurn=true;
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.sT);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            wFrag="";
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        // I dont need this ->TextView label = (TextView) findViewById(R.id.sT);
        //sText is my label equivalent
        // Do computer turn stuff then make it the user's turn again
        if(wFrag.isEmpty())
        {
            wFrag="";
            int x=97+random.nextInt(26);
            char alpha=(char)(x);
            String alphaStr=Character.toString(alpha);
            alphaStr=alphaStr.toLowerCase();
            wFrag=wFrag.concat(alphaStr);
            wFrag=wFrag.toLowerCase();
            lText.setText(wFrag);
            userTurn=true;
            sText.setText(USER_TURN);
        }
        else if (wFrag.length() >= 4 && MyDictionary.isWord(wFrag.toLowerCase()))////comp wins as user completed
        {
            sText.setText("Its valid word.COMPUTER Wins");
            comp++;
            cScr.setText(comp+"");
            finishGame();
        }
        else
        {
            tempCheck=MyDictionary.getAnyWordStartingWith(wFrag.toLowerCase());
            if (tempCheck==null)
            {
//                String temp=wFrag;
//                temp=temp.substring(0,wFrag.length()-1);
//                temp=MyDictionary.getAnyWordStartingWith(temp.toLowerCase());
//                String possibleChar=Character.toString(temp.charAt(temp.length()));
                sText.setText("It isnt a valid prefix.COMPUTER Wins.");
                comp++;
                cScr.setText(comp+"");
                finishGame();
            }
            else if(tempCheck!=null)
            {
                char abc=tempCheck.charAt(wFrag.length());
                String nWord=wFrag.concat(Character.toString(abc));
                wFrag=nWord;
                lText.setText(wFrag);

                userTurn = true;
                sText.setText(USER_TURN);
            }

        }

    }

    @Override
    public boolean onKeyUp(int keyCode,KeyEvent event) {
        boolean m=false;
        if (userTurn) {
            if ((keyCode >=29 && keyCode <=54) && (lText.getText().toString().equals(strNl))) {
                char ctemp = (char) event.getUnicodeChar();
                String temp = Character.toString(ctemp);
                wFrag = temp;
                wFrag=wFrag.toLowerCase();
                lText.setText(wFrag);
                computerTurn();
                m= super.onKeyUp(keyCode, event);
            } else if ((keyCode >=29 && keyCode <=54)) {
                char ctemp = (char) event.getUnicodeChar();
                String temp=Character.toString(ctemp);
                wFrag = wFrag+temp;
                wFrag=wFrag.toLowerCase();
                lText.setText(wFrag);
//                if(MyDictionary.isWord(wFrag))
//                {
//                    sText.setText("Is Word");
//                }
               computerTurn();
                m= super.onKeyUp(keyCode, event);
            } else {
                m= super.onKeyUp(keyCode, event);
            }
        }
        return m;

    }
}
