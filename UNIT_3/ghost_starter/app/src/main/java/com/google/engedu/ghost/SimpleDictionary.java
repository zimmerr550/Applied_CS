/*	TEAM MEMBERS/AUTHORS:	RAKSHITH RAJ
			SATYAM BHARADWAJ
*/
package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.*;
import java.util.*;
import java.lang.*;


public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    Random rand=new Random();
    String nlStr="";

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public  boolean isWord(String word)
    {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix)
    {
        if((prefix==null) || prefix.isEmpty())
        {
            int randNo=rand.nextInt(words.size());
            return words.get(randNo);
        }
        else /////Binary search part
        {
            int l=0;
            int r=words.size()-1;
            while(l<=r)
            {
                int m=(l+r)/2;
                if(words.get(m).startsWith(prefix))
                {
                    return words.get(m);
                }
                else if(words.get(m).compareTo(prefix)>0)//if return less than 0 then arg is lexicographiclly greater
                {
                    r=m-1;
                }
                else
                {
                    l=m+1;
                }
            }
        }
        return null;
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String str="hell";
        return str;
    }
}
