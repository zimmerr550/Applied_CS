package com.google.engedu.ghost;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;
import java.lang.*;



public class TrieNode
{
    private HashMap<String, TrieNode> children;
    private boolean isComplete;
    Random rand=new Random();
    ArrayList<String > badWords;
    ArrayList<String> wordsExist;
    HashMap root;
    public TrieNode()
    {
        children=new HashMap<>();
        root=children;
        boolean isComplete=false;
    }

    public void add(String s)
    {
        children=root;
        TrieNode trie=null;
        for(int i=0;i<s.length();i++)
        {
            char k=s.charAt(i);
            String key=Character.toString(k);

            if(!children.containsKey(key))
            {
                trie=new TrieNode();
                children.put(key,trie);

            }
            else if(children.containsKey(key))
            {
                trie=children.get(key);
            }

            children=trie.children;

        }
        trie.isComplete=true;
    }

    public boolean isWord(String s)
    {
        children=root;
        for(int i=0;i<s.length();i++)//traverse till u get a true for isWord
        {
            TrieNode t=null;
            String key=Character.toString(s.charAt(i));
            if(children.containsKey(key))
            {
                t=children.get(key);
                if(t.isComplete)
                {
                    return true;
                }

            }
            else////Doesnt contain key at all
            {
                return false;
            }
            children=t.children;
        }
        return  false;
    }

    public String getAnyWordStartingWith(String s)
    {
        wordsExist=new ArrayList<String>();
        children=root;
        TrieNode t=null;
        for(int i=0;i<s.length();i++)
        {
            String key=Character.toString(s.charAt(i));
            if(children.containsKey(key))
            {
                t=children.get(key);
            }
            else
            {
                return null;
            }
            children=t.children;
        }
        preOrder(t,s,wordsExist);
        int n=rand.nextInt(wordsExist.size());
        String result=wordsExist.get(n);
        return result;
    }

    public void preOrder(TrieNode t,String s,ArrayList<String> wordsExist)
    {
        if((t.isComplete) && (t.children.isEmpty()))////if prefix is a complete word and has no children
        {
            wordsExist.add(s);
        }
        else
        {
            //case1:is a word but has children
            //case2:isnt a word yet.Traverse further below the tree
            if(isComplete)
            {
                wordsExist.add(s);
            }
            for(String key: t.children.keySet())
            {
                String temp=s.concat(key);
                preOrder(t.children.get(key),temp,wordsExist);
            }
        }
    }

    public String getGoodWordStartingWith(String s)
    {
        TrieNode t=null;
        badWords=new ArrayList<String>();
        children=root;
        for(int i=0;i<s.length();i++)
        {
            String key=Character.toString(s.charAt(i));
            if(children.containsKey(key))
            {
                t=children.get(key);
            }
            else
            {
                return null;
            }
            children=t.children;
        }

        for(String x: t.children.keySet())
        {
            if(!t.isComplete)
            {
                badWords.add(s);
            }
        }

        if (badWords.isEmpty())
        {
            preOrder(t,s,wordsExist);
        }
        else
        {
            int r=rand.nextInt(badWords.size());
            String key=badWords.get(r);
            s=s+key;
            preOrder(t.children.get(key),s,wordsExist);
        }

        int index=rand.nextInt(wordsExist.size());

        String word=wordsExist.get(index);
        return word;

    }
}
