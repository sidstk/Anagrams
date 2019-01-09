package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList = new ArrayList<>();
    private HashSet<String> wordSet = new HashSet<>();
    private HashMap<String, ArrayList<String> > lettersToWord = new HashMap<>();
    private HashMap<Integer, ArrayList<String> > sizeToWords = new HashMap<>();
    private HashMap<String, Boolean> visited = new HashMap<>();
    private int wordLength = DEFAULT_WORD_LENGTH;
    private int anagCount;

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            visited.put(word, false);
            String tmp = sortString(word);
            if(lettersToWord.containsKey(tmp)){
                lettersToWord.get(tmp).add(word);
                if(lettersToWord.get(tmp).size() >= MIN_NUM_ANAGRAMS) anagCount++;
            }
            else{
                ArrayList<String> tempList = new ArrayList<>();
                tempList.add(word);
                lettersToWord.put(tmp, tempList);
            }

            if(sizeToWords.containsKey(word.length()))
                sizeToWords.get(word.length()).add(word);

            else{
                ArrayList<String> tempList = new ArrayList<>();
                tempList.add(word);
                sizeToWords.put(word.length(), tempList);
            }


        }

        Log.d("count->", String.valueOf(anagCount));
        int count = 0;
        Set set = lettersToWord.entrySet();
        for(Object aSet: set){
            Map.Entry m = (Map.Entry) aSet;
            ArrayList<String> arl = (ArrayList<String>) m.getValue();
            if(arl.size() >= MIN_NUM_ANAGRAMS) count++;

        }
        Log.d("countin->", String.valueOf(count));
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }


    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sortWord = sortString(targetWord);
        for(int i=0; i<wordList.size(); i++){
            if(sortWord.equals(sortString(wordList.get(i)))){
                result.add(wordList.get(i));
                Log.d("words",wordList.get(i));
            }

        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char ch = 'a'; ch<='z'; ch++){
           String tmp = sortString(word+ch);
           if(lettersToWord.containsKey(tmp)){
               ArrayList<String> tempList = lettersToWord.get(tmp);
               for(int i=0; i<tempList.size(); i++){
                   if( !tempList.get(i).contains(word)) {
                       result.add(tempList.get(i));
                       Log.d("results", tempList.get(i));
                   }
               }
               //result.addAll(lettersToWord.get(tmp));
               //Log.d("result", result.toString());
           }
        }
        return result;
    }

    public String pickGoodStarterWord() {

        String result = null;
        while(true){

            ArrayList<String> words = sizeToWords.get(wordLength);
            for(int i=0; i<words.size(); i++){
                if(lettersToWord.get(sortString(words.get(i))).size() >= MIN_NUM_ANAGRAMS && !visited.get(words.get(i))) {
                    visited.put(words.get(i), true);
                    result = words.get(i);
                    Log.d("result", result);
                    break;
                }
            }
            if(wordLength >= MAX_WORD_LENGTH)
                wordLength = DEFAULT_WORD_LENGTH;
            else wordLength++;

            if(result != null)
                return result;

        }

        /*int index = random.nextInt(wordList.size());
        while(true){
            String sortWord = sortString(wordList.get(index));
            if(lettersToWord.get(sortWord).size() >= MIN_NUM_ANAGRAMS )
                return wordList.get(index);

            index = random.nextInt(wordList.size());
        }*/
        //return "badge";
    }

    private String sortString(String inputString)
    {
        // convert input string to char array
        char tempArray[] = inputString.toCharArray();

        // sort tempArray
        Arrays.sort(tempArray);

        // return new sorted string
        return new String(tempArray);
    }
}
