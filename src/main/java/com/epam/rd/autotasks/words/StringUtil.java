package com.epam.rd.autotasks.words;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.lang.StringBuilder;
import java.util.stream.Collectors;
import java.util.List;
import java.util.LinkedList;
import java.util.function.Predicate;

public class StringUtil {
    public static int countEqualIgnoreCaseAndSpaces(String[] words, String sample) {
        //checking if sample is null or words is null or empty
        if(sample == null || words == null) return 0;
        if(sample.length() == 0 || words.length == 0) return 0;

        //setting a pattern
        sample = sample.trim();
        Pattern pattern = Pattern.compile("\\s*" + sample + "\\s*", Pattern.CASE_INSENSITIVE);

        //checking number of words from words array that are equal to sample
        int count = 0;
        for(int i = 0;i<words.length;i++){
            if(pattern.matcher(words[i]).matches()) count++;
        }

        return count;
    }

    public static String[] splitWords(String text) {
        //checking if text is null or empty
        if(text == null)return null;
        if(text.length() == 0) return null;

        //setting a pattern and splitting text
        Pattern pattern = Pattern.compile("[,.;: ?!]");
        String[] splitWords = pattern.split(text);

        //Creating an LinkedList and searching for blank values
        List<String> list = new LinkedList<String>();
        for(int i = 0; i < splitWords.length; i++){
            if(splitWords[i] == "");
            else list.add(splitWords[i]);
        }

        //checking if string consisting only of separating characters
        if(list.isEmpty())return null;

        return list.toArray(new String[list.size()]);
    }

    public static String convertPath(String path, boolean toWin) {
        if(path == null) return null;
        if(path.length() == 0) return null;
        if(path.contains("/") && path.contains("\\")) return null;
       

        if(toWin){ 
            //from Unix to windows

            //pattern starting with ~
            Pattern pattern1 = Pattern.compile("(^~(/+([a-zA-Z \\. \\_ \\d]{1,}))*/?)");
            Predicate<String> predicate1 = pattern1.asMatchPredicate();

            //pattern starting with /
            Pattern pattern2 = Pattern.compile("(^/(/*([a-zA-Z \\. \\_ \\d]{1,}))*/?)");
            Predicate<String> predicate2 = pattern2.asMatchPredicate();

            //pattrn starting with letters
            Pattern pattern3 = Pattern.compile("(^[a-zA-Z](/*([a-zA-Z \\. \\_ \\d]{1,}))*/?)");
            Predicate<String> predicate3 = pattern3.asMatchPredicate();

            //pattern starting with . or ..
            Pattern pattern4 = Pattern.compile("(^\\.{1,2}(/{1}([a-zA-Z \\. \\_ \\d]{1,}))*/?)");
            Predicate<String> predicate4 = pattern4.asMatchPredicate();

            if(predicate1.test(path)){
                //starting with ~
                if(path.charAt(path.length()-1)=='/'){ //if ends with /
                    String newPath = "";
                    for(int i = 1;i<path.length();i++) newPath+=path.charAt(i);

                    newPath = winChange(newPath);
                    return "C:\\User" + newPath + "\\";
                }
                String newPath = "";
                for(int i = 1;i<path.length();i++) newPath+=path.charAt(i);

                newPath = winChange(newPath);
                return "C:\\User" + newPath;
            }
            else if(predicate2.test(path)){
                //starting with /
                if(path.charAt(path.length()-1)=='/'){ //if ends with /
                    path = winChange(path);
                    return "C:" + path + "\\"; 
                }
                path = winChange(path);
                return "C:" + path;
            }
            else if(predicate3.test(path)){
                //starting with letters
                if(path.charAt(path.length()-1)=='/'){ //if ends with /
                    path = winChange(path);
                    return path + "\\"; 
                }
                path = winChange(path);
                return path;
            }
            else if(predicate4.test(path)){
                //starting with . or ..
                if(path.charAt(path.length()-1)=='/'){ //if ends with /
                    path = winChange(path);
                    return path + "\\"; 
                }
                path = winChange(path);
                return path;
            }
            else{
                return null;
            }

        }
        else{
            //from windows to unix
            if(countMatches(path, ':') > 1) return null;
            if(countMatches(path, '\\') == 0) return path;
            

            
            Pattern pattern = Pattern.compile("[\\\\]");
            String[] splitWords = pattern.split(path);

            List<String> list = new LinkedList<String>();
            for(int i = 0; i < splitWords.length; i++){
                if(splitWords[i].equals("")) continue;
                else list.add(splitWords[i]);
            }

            if(list.contains("C:")){
                list.add(0, "~");
                list.remove("C:");
                list.remove("User");
            }

            String result = list.stream().collect(Collectors.joining("/","",""));
            return result;
        }
        
    }

    private static String winChange(String path){
        //changing from "/"" to "\""
        Pattern pattern = Pattern.compile("[/]");
        String[] splitWords = pattern.split(path);

        List<String> list = new LinkedList<String>();
        for(int i = 0; i < splitWords.length; i++){
            list.add(splitWords[i]);
        }
        String result = list.stream().collect(Collectors.joining("\\","",""));
        return result;
    }

    private static int countMatches(String text, char match){
        int counter = 0;
        for(int i = 0; i < text.length(); i++){
            if(text.charAt(i) == match) counter++;
        }
        return counter;
    }

    public static String joinWords(String[] words) {
        //checking if array words is null or empty
        if(words == null) return null;
        if(words.length == 0) return null;

        //Creating an LinkedList and searching for blank values
        List<String> wordsList = new LinkedList<String>();
        for(int i = 0;i<words.length;i++){
            if(words[i]=="") continue;
            wordsList.add(words[i]);
        }

        if(wordsList.isEmpty()) return null;

        //joining words
        String result = wordsList.stream().collect(Collectors.joining(", ","[","]"));
        return result;
    }

    public static void main(String[] args) {
        System.out.println("Test 1: countEqualIgnoreCaseAndSpaces");
        String[] words = new String[]{" WordS    \t", "words", "w0rds", "WOR  DS", };
        String sample = "words   ";
        int countResult = countEqualIgnoreCaseAndSpaces(words, sample);
        System.out.println("Result: " + countResult);
        int expectedCount = 2;
        System.out.println("Must be: " + expectedCount);

        System.out.println("Test 2: splitWords");
        String text = "   ,, first, second!!!! third";
        String[] splitResult = splitWords(text);
        System.out.println("Result : " + Arrays.toString(splitResult));
        String[] expectedSplit = new String[]{"first", "second", "third"};
        System.out.println("Must be: " + Arrays.toString(expectedSplit));

        System.out.println("Test 3: convertPath");
        String unixPath = "/some/unix/path";
        String convertResult = convertPath(unixPath, true);
        System.out.println("Result: " + convertResult);
        String expectedWinPath = "C:\\some\\unix\\path";
        System.out.println("Must be: " + expectedWinPath);

        System.out.println("Test 4: joinWords");
        String[] toJoin = new String[]{"go", "with", "the", "", "FLOW"};
        String joinResult = joinWords(toJoin);
        System.out.println("Result: " + joinResult);
        String expectedJoin = "[go, with, the, FLOW]";
        System.out.println("Must be: " + expectedJoin);
    }
}