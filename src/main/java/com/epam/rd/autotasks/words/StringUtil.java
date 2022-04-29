package com.epam.rd.autotasks.words;

import java.util.Arrays;
import java.util.StringTokenizer;
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
        sample = sample.stripLeading();
        sample = sample.stripTrailing();
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

        //Unix
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

        //Windows
        //pattern starting with C:\User
        Pattern pattern9 = Pattern.compile("(^C\\u003A\\u005CUser(\\u005C+([a-zA-Z \\. \\_ \\d]{1,}))*\\u005C?)");
        Predicate<String> predicate9 = pattern9.asMatchPredicate();

        //pattern starting with C:
        Pattern pattern8 = Pattern.compile("(^C\\u003A(\\u005C*([a-zA-Z \\. \\_ \\d]{1,}))*\\u005C?)");
        Predicate<String> predicate8 = pattern8.asMatchPredicate();

        //pattern starting with letters
        Pattern pattern7 = Pattern.compile("(^[a-zA-Z](\\u005C*([a-zA-Z \\. \\_ \\d]{1,}))*\\u005C?)");
        Predicate<String> predicate7 = pattern7.asMatchPredicate();

        //pattrn starting with \
        Pattern pattern6 = Pattern.compile("(^\\u005C+(\\u005C*([a-zA-Z \\. \\_ \\d]{1,}))*\\u005C?)");
        Predicate<String> predicate6 = pattern6.asMatchPredicate();

        //pattern starting with . or ..
        Pattern pattern5 = Pattern.compile("(^\\.{1,2}(\\u005C{1}([a-zA-Z \\. \\_ \\d]{1,}))*\\u005C?)");
        Predicate<String> predicate5 = pattern5.asMatchPredicate();
        
       
        if(toWin){ 
            //from unix to windows
            //pattern if ends with /
            Pattern end = Pattern.compile(".*/");
            Predicate<String> predicateEnd = end.asMatchPredicate();
            if(predicate1.test(path)){
                //starting with ~
                String newPath = "";
                for(int i = 1;i<path.length();i++) newPath+=path.charAt(i);
                newPath = winChange(newPath);
                if(predicateEnd.test(path)) return "C:\\User" + newPath + "\\";
                else return "C:\\User" + newPath;
            }
            else if(predicate2.test(path)){
                //starting with /
                String newPath = winChange(path);
                if(predicateEnd.test(path)) return "C:" + newPath + "\\"; 
                else return "C:" + newPath;
            }
            else if(predicate3.test(path) || predicate4.test(path)){
                //starting with letters or dots
                String newPath = winChange(path);
                if(predicateEnd.test(path)) return newPath + "\\"; 
                else return newPath;
            }
            else if(predicate5.test(path)||predicate6.test(path)||predicate7.test(path)||predicate8.test(path)||predicate9.test(path)){
                return path;
            }
            else{
                return null;
            }
        }
        else{
            //from windows to unix
            //pattern if ends with \
            Pattern end = Pattern.compile(".*\\u005C");
            Predicate<String> predicateEnd = end.asMatchPredicate();
            if(predicate9.test(path)){
                //starting with C:\User
                String newPath = "";
                for(int i = 7;i<path.length();i++) newPath+=path.charAt(i);
                newPath = uniChange(newPath);
                if(predicateEnd.test(path)) return "~" + newPath + "/";
                else return "~" + newPath;
            }
            else if(predicate8.test(path)){
                //starting with C:\
                String newPath = "";
                for(int i = 2;i<path.length();i++) newPath+=path.charAt(i);
                newPath = uniChange(newPath);
                if(predicateEnd.test(path)) return  newPath + "/";
                else return newPath;
            }
            else if(predicate7.test(path) || predicate6.test(path) || predicate5.test(path)){
                //starting with letter, . or \
                String newPath = "";
                newPath = uniChange(path);
                if(predicateEnd.test(path)) return newPath + "/"; 
                else return newPath;
            }
            else if(predicate1.test(path)||predicate2.test(path)||predicate3.test(path)||predicate4.test(path)){
                return path;
            }
            else{
                return null;
            }
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

    private static String uniChange(String path){
        //changing from "/"" to "\""
        Pattern pattern = Pattern.compile("[\\\\]");
        String[] splitWords = pattern.split(path);

        List<String> list = new LinkedList<String>();
        for(int i = 0; i < splitWords.length; i++){
            list.add(splitWords[i]);
        }
        String result = list.stream().collect(Collectors.joining("/","",""));
        return result;
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