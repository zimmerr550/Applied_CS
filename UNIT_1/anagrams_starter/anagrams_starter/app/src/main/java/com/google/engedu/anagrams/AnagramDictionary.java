package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.*;
import java.util.*;
import java.util.Random;

public class AnagramDictionary
{

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private static final String TAG="AnagramDictionary";
    //
    ArrayList<String> wordList=new ArrayList<String>();
    HashSet<String>	wordSet=new HashSet<String>();
    HashMap<String,ArrayList<String>> lettersToWord=new HashMap<String,ArrayList<String>>();
    HashMap<Integer,ArrayList<String>> sizeToWord=new HashMap<Integer,ArrayList<String>>();
    //
    public AnagramDictionary(InputStream wordListStream) throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;
        while((line = in.readLine()) != null)
        {
            String targetWord = line.trim();
            wordList.add(targetWord);
            wordSet.add(targetWord);
            String key=sortLetters(targetWord);
            //If key exists then add it to Arraylist present already
            if(lettersToWord.containsKey(key))
            {
                lettersToWord.get(key).add(targetWord);
            }
            else
            {
                //Make a new arraylist
                ArrayList<String> ALS=new ArrayList<String>();
                ALS.add(targetWord);//add string to list
                lettersToWord.put(key,ALS);///making a new entry in hashmap
            }

            if(sizeToWord.containsKey(targetWord.length()))
            {
                sizeToWord.get(targetWord.length()).add(targetWord);
            }

            else
            {
                ArrayList<String> ALS=new ArrayList<String>();
                ALS.add(targetWord);
                sizeToWord.put(targetWord.length(),ALS);
            }
        }
    }
/////////////////////////////*************************METHODS***************************///////////////////////////////////////
//
    public ArrayList<String> getAnagrams(String baseWord)
    {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0;i<wordList.size();i++)
        {
            String text=wordList.get(i);
            String sListTarget=sortLetters(text);
            String sListBase=sortLetters(baseWord);
            if(sListBase.equals(sListTarget))
            {
                result.add(text);
            }
        }

        return result;
    }

    public String sortLetters(String word)
    {
        char[] charArray=word.toCharArray();
        Arrays.sort(charArray);
        String sorted=new String(charArray);
        return sorted;
    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String baseWord)
    {
        ArrayList<String> result = new ArrayList<String>();
        //ensure that added symbol  is a valid char
        for(int i=97;i<123;i++)
        {
            //remove comments with # to achieve an extension to get a valid anagram with two more chars
            /*#
            for( int j=i+1;j<123;j++)
            {
                if(j!=i)
                {
                #*/
                    String charStr=(char)i+baseWord;///convert to ascii equivalent values.
                //# charStr=charStr+(char)j;
                    String sortedCharStr=sortLetters(charStr);
                    if(lettersToWord.containsKey(sortedCharStr))
                    {
                        ArrayList<String> newList=lettersToWord.get(sortedCharStr);
                        for(String ss : newList)
                        {
                            if(isGoodWord(ss,baseWord))
                            {
                                result.add(ss);
                            }
                        }
                    }
              /*#
                }
            }#*/
        }
        return result;
    }

    boolean isGoodWord(String targetWord,String baseWord)
    {
        boolean m=false;
        if(wordSet.contains(targetWord))
        {
            if((targetWord.contains(baseWord)))
            {
                m=false;
                //System.out.println(m);

            }
            else
            {
                m=true;
                //System.out.println(m);

            }
        }
        return m;

    }

    public String pickGoodStarterWord()
    {
        String startWord="";
        int i=0;

        ArrayList<String> tempp=new ArrayList(1);

        int wordLen=DEFAULT_WORD_LENGTH;
        boolean isMin=false;
        Log.d(TAG,"HELLO:"+ i);
        i++;
        ArrayList<String> wordCandidates=sizeToWord.get(wordLen);
        int index=random.nextInt(wordCandidates.size());
        while(!isMin)
        {
            if(index>wordCandidates.size()-1)
            {
                index=0;
            }
            tempp=sizeToWord.get(wordLen);
            startWord=tempp.get(index);
            if(getAnagramsWithOneMoreLetter(startWord).size() >=MIN_NUM_ANAGRAMS )
            {
                isMin=true;
            }
            index++;

        }
        if(wordLen<=MAX_WORD_LENGTH)
        {
            wordLen++;
        }

        return startWord;
    }
}
