package com.example.rakshith.roll_a_dice;

import android.graphics.drawable.Drawable;
import android.os.*;
import android.app.*;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.*;
import java.lang.*;
import java.util.logging.Handler;


public class Roll extends AppCompatActivity
{

    static int ctr=0;
    int uTs=0;
    static int cTs=0;
    int uFs=0;
    int cFs=0;
    Button rBtn;
    Button play;
    Button hBtn;
    Button rstBtn;
    ImageView img;
    TextView rT;
    TextView uTscr;
    TextView cTscr;
    TextView uOscr;
    TextView cOscr;
    int[] DICE_SIDE = {R.drawable.dice1, R.drawable.dice2, R.drawable.dice3,
            R.drawable.dice4, R.drawable.dice5, R.drawable.dice6};
    final android.os.Handler handler = new android.os.Handler();

    Random rand=new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        rBtn=(Button)findViewById(R.id.rollBtn);
        hBtn=(Button)findViewById(R.id.holdBtn);
        rstBtn=(Button)findViewById(R.id.resetBtn);
        play=(Button)findViewById(R.id.playAgn);
        img=(ImageView)findViewById(R.id.diceImg);
        rT=(TextView)findViewById(R.id.resTxt);
        uTscr=(TextView)findViewById(R.id.uTurnScr);
        cTscr=(TextView)findViewById(R.id.compTurnScr);
        uOscr=(TextView)findViewById(R.id.usrScr);
        cOscr=(TextView)findViewById(R.id.compScr);
        uTscr.setText(" "+uTs);
        uOscr.setText(" "+uFs);
        cTscr.setText(" "+cTs);
        cOscr.setText(" "+cFs);
        play.setVisibility(View.GONE);
        rT.setVisibility(View.GONE);
        userPlay();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);
    }


    void userPlay()
    {
        rBtn.setEnabled(true);
        hBtn.setEnabled(true);
        rBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                int diceVal=rollDice();
                if(diceVal != 1)
                {
                    uTs=uTs+diceVal;
                    userPlay();
                }
                else
                {
                    uTs=0;
                    computerPlay();
                }

                uTscr.setText(" "+uTs);
            }
        });

        hBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //rBtn.setVisibility(View.INVISIBLE);
                //hBtn.setVisibility(View.INVISIBLE);
                //rBtn.setEnabled(false);
                //hBtn.setEnabled(false);
                uFs=uFs+uTs;
                uTs=0;
                uOscr.setText(" "+uFs);
                uTscr.setText(" "+uTs);
                validate();
                computerPlay();
            }
        });

        play.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uTs=0;
                uFs=0;
                cTs=0;
                cFs=0;
                ctr=0;
                uTscr.setText(" "+uTs);
                uOscr.setText(" "+uFs);
                cTscr.setText(" "+cTs);
                cOscr.setText(" "+cFs);
                img.setVisibility(View.VISIBLE);
                rBtn.setVisibility(View.VISIBLE);
                hBtn.setVisibility(View.VISIBLE);
                rstBtn.setVisibility(View.VISIBLE);
                rBtn.setEnabled(true);
                hBtn.setEnabled(true);
                rstBtn.setEnabled(true);
                rT.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
            }
        });

        rstBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                uTs=0;
                uFs=0;
                cTs=0;
                cFs=0;
                ctr=0;

                uTscr.setText(" "+uTs);
                uOscr.setText(" "+uFs);
                cTscr.setText(" "+cTs);
                cOscr.setText(" "+cFs);
                rBtn.setVisibility(View.VISIBLE);
                hBtn.setVisibility(View.VISIBLE);
                rT.setVisibility(View.GONE);
                play.setVisibility(View.GONE);
                //rBtn.setEnabled(true);
                //hBtn.setEnabled(true);

            }
        });
    }

    int rollDice()
    {
        int dv=0;
        dv=rand.nextInt(6);
        Drawable d=getResources().getDrawable(DICE_SIDE[dv]);
        img.setImageDrawable(d);
        dv=dv+1;
        return dv;
    }

    void validate()
    {
        if(cFs>99)
        {

            //img.setVisibility(View.GONE);
            rBtn.setEnabled(false);
            hBtn.setEnabled(false);
            rstBtn.setEnabled(false);
            //hBtn.setVisibility(View.GONE);
            //rstBtn.setVisibility(View.GONE);
            rT.setVisibility(View.VISIBLE);
            play.setVisibility(View.VISIBLE);
            rT.setText("Result:Computer Wins !!");

        }
        else if(uFs>99)
        {
            //img.setVisibility(View.GONE);
            //rBtn.setVisibility(View.GONE);
            //hBtn.setVisibility(View.GONE);
            //rstBtn.setVisibility(View.GONE);
            rBtn.setEnabled(false);
            hBtn.setEnabled(false);
            rstBtn.setEnabled(false);
            rT.setVisibility(View.VISIBLE);
            play.setVisibility(View.VISIBLE);
            rT.setText("Result:User Wins !!");
        }
    }
    void cHold()
    {
           // img.setVisibility(View.GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    cFs=cFs+cTs;
                    cTs=0;
                    cTscr.setText(" "+cTs);
                    cOscr.setText(" "+cFs);
                    ctr=0;
                    validate();
                    userPlay();
                }
            },500);
            img.setVisibility(View.VISIBLE);
    }

    void computerPlay()
    {
        rBtn.setEnabled(false);
        hBtn.setEnabled(false);
        if(ctr<4)
        {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int cDiceVal = rollDice();
                    if (cDiceVal != 1) {
                        cTs = cTs + cDiceVal;
                        ctr++;
                        computerPlay();

                    } else {
                        cTs = 0;
                        userPlay();
                    }

                    cTscr.setText(" " + cTs);
                    //ctr++;

                }
            }, 500);
        }
        img.setVisibility(View.VISIBLE);
        if(ctr>=4)
        {
            cHold();
        }






        rBtn.setVisibility(View.VISIBLE);
        hBtn.setVisibility(View.VISIBLE);
        //rBtn.setEnabled(true);
        //hBtn.setEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_roll, menu);
        return true;
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
}
