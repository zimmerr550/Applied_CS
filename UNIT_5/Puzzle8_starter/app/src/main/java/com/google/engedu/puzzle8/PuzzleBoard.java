package com.google.engedu.puzzle8;

import android.graphics.Bitmap;
import android.graphics.*;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.*;

public class PuzzleBoard {

    private PuzzleBoard previousBoard;
    private static int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    private ArrayList<PuzzleTile> tiles;
//  Keep track of number of moves in every new instance of Puzzle board
    int moves;
    PuzzleBoard(Bitmap bitmap, int parentWidth)
    {
        previousBoard=null;
        moves=0;
        tiles =new ArrayList<>();
        parentWidth*=4;
        Bitmap sBitmap=Bitmap.createScaledBitmap(bitmap,parentWidth,parentWidth,true);
        for(int i=0;i<NUM_TILES;i++)
        {
            for(int j=0;j<NUM_TILES;j++)
            {
                int nthTile=XYtoIndex(i,j);
                if(nthTile <= NUM_TILES*NUM_TILES-2)
                {
                    ///try and get a new tile for nth tile
                    /*
                    Bitmap createBitmap (Bitmap source,int x,int y,int width,int height)
                    x=y=first px
                    width and height are x and y increments respectively
                    */

                    Bitmap tileBit=Bitmap.createBitmap(sBitmap,i*parentWidth/NUM_TILES,j*parentWidth/NUM_TILES,parentWidth/NUM_TILES,parentWidth/NUM_TILES);
                    //store each tile in an array of PuzzleTile
                    PuzzleTile pt=new PuzzleTile(tileBit,(nthTile));
                    tiles.add(pt);
                }
                else//leave the last tile empty
                {
                    tiles.add(null);
                }
            }
        }
    }

    PuzzleBoard(PuzzleBoard otherBoard,int moves)
    {
        previousBoard=otherBoard;
        if(otherBoard.tiles.clone()!=null)
        {
            tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
            this.moves=moves+1;
        }


    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    public void setNumTiles(int n)
    {
        NUM_TILES=n;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i / NUM_TILES, i % NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i / NUM_TILES, i % NUM_TILES)) {
                    return tryMoving(i / NUM_TILES, i % NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                return true;
            }

        }
        return false;
    }

    public boolean resolved()///checks if the current PuzzleBoard is equivalent to initial PuzzleBoard
    {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x * NUM_TILES+y;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours()
    {
        ArrayList<PuzzleBoard> nearby=new ArrayList<PuzzleBoard>();
        int x=0;
        int y=0;
        //find the empty tile and push its co-ordinates to x and y;
        for(int i=0;i<NUM_TILES;i++) {
            for (int j = 0; j < NUM_TILES; j++) {
                int nth = i * NUM_TILES + j;
                if (tiles.get(nth) == null) {
                    x = i;
                    y = j;
                    break;
                }
            }
        }   //flick validity checking from method tryMoving  :)
            //find all neighbours,swap with valid neighbour.If new puzzleBoard is obtained save the instance of puzzleBoard in ArrayList created above.
            for (int[] delta : NEIGHBOUR_COORDS)
            {
                int nearbyX = x + delta[0];
                int nearbyY = y + delta[1];
                if ((nearbyX >= 0 && nearbyX < NUM_TILES) && (nearbyY >= 0 && nearbyY < NUM_TILES))
                {
                    PuzzleBoard possibleBoard=new PuzzleBoard(this,moves);
                    possibleBoard.swapTiles(XYtoIndex(nearbyX,nearbyY),XYtoIndex(x,y));
                    //add possible board to ArrayList
                    nearby.add(possibleBoard);
                }
            }

        return nearby;
    }

    void setPreviousBoard(PuzzleBoard previousBoard)
    {
        this.previousBoard=previousBoard;
    }
    PuzzleBoard getPreviousBoard()
    {
        return this.previousBoard;
    }

    public int priority()
    {
        int dist=0;
        for(int i=0;i<NUM_TILES;i++)
        {
            for(int j=0;j<NUM_TILES;j++)
            {
                PuzzleTile pT=tiles.get(XYtoIndex(i,j));
                if(pT!=null)
                {
//                    retieve actual x and y coordinates
                    int actual=pT.getNumber();
                    int actX=actual/NUM_TILES;
                    int actY=actual%NUM_TILES;
                    dist=dist+Math.abs(actX-i)+Math.abs(actY-j);
                }
            }
        }
        return dist+moves;
    }

}
