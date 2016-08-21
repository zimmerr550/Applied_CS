package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;

    private ArrayList<PuzzleBoard> animation;
    private Random random = new Random();

    Comparator<PuzzleBoard> comparator=new Comparator<PuzzleBoard>() {
        @Override
        public int compare(PuzzleBoard one, PuzzleBoard two) {
            int diff=one.priority()-two.priority();
            return diff;
        }
    };
    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = imageBitmap.getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    //puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            // Do something. Then:
            //update puzzleBoard to NUM_SHUFFLE STEPS times.Pick random value from PuzzleBoard.puzzleBoard.neighbours()

            //puzzleBoard.reset();
            for(int i=0;i<NUM_SHUFFLE_STEPS;i++)
            {
                ArrayList<PuzzleBoard> shuffled=puzzleBoard.neighbours();
                int x=random.nextInt(shuffled.size());
                puzzleBoard=shuffled.get(x);
            }
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve()
    {
        PriorityQueue<PuzzleBoard> pq=new PriorityQueue<>(1,comparator);
        //make new current PuzzleBoard with 0 moves and set previousBoard to null
        PuzzleBoard current=new PuzzleBoard(puzzleBoard,-1);
        HashSet<PuzzleBoard> visited=new HashSet<>();
        //made a new method setPreviousBoard in clas PuzzleBoard.
        current.setPreviousBoard(null);
        pq.add(current);
        visited.add(current);
        if(current.resolved())
        {
            Toast toast = Toast.makeText(activity, "Shuffle first ??!!", Toast.LENGTH_LONG);
            toast.show();

        }
        //visited.add(current);
        else
        {

            while(pq!=null)
            {
                //retrieve and remove head / first element in pq
                PuzzleBoard first=pq.poll();
//            PuzzleBoard prevPopped=first;
//            if first isnt the inital state of PuzzleBoard,then subject all its neighbours to method solve by adding it to queue
//             use the method resolved() to check if current PuzzleBoard matches the original PuzzleBoard
                visited.add(first);
                if(!first.resolved())
                {
                ArrayList<PuzzleBoard> neigh=first.neighbours();
                for(PuzzleBoard p:neigh)
                {
                    if(p!=current && !visited.contains(p))
                    {
                        pq.add(p);
                    };
                }
//                    pq.addAll(first.neighbours());
                }
                //if the first itself a solution
                //form an ArrayList of previous board of current until the initial
                //reverse the ArrayList to get ordered sequence of PuzzleBoards
                else if(first.resolved())
                {
                    ArrayList<PuzzleBoard> ans=new ArrayList<>();
                    if(first.getPreviousBoard()==null)
                    {
                        animation=ans;
                        invalidate();
                        break;
                    }
                    else
                    {
                        ans.add(first);
                        while(first.getPreviousBoard()!=null)
                        {
                            ans.add(first.getPreviousBoard());
                            first=first.getPreviousBoard();
                        }
                    }

                    Collections.reverse(ans);
                    animation=ans;
                    invalidate();
                    break;
//
                }
            }

        }

    }
}
