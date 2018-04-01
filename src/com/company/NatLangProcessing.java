package com.company;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NatLangProcessing {

    public static String[] splitter(String text){
        String[] answer = text.split("\\d\\.");
        return answer;
    }

    public static int experience(String text){

        // replace numericals with numbers

        text = text.replaceAll("ё","е");
        String text1 = text.replaceAll(" одного"," 1");
        text1 = text1.replaceAll(" двух"," 2");
        text1 = text1.replaceAll(" тр.х"," 3");
        text1 = text1.replaceAll(" четыр.х"," 4");
        text1 = text1.replaceAll(" пяти"," 5");
        text1 = text1.replaceAll(" шести"," 6");
        text1 = text1.replaceAll(" семи"," 7");
        text1 = text1.replaceAll(" восьми"," 8");
        text1 = text1.replaceAll(" девяти"," 9");

        //set default values for begin and end

        int ind_begin = 0;
        int ind_end = text1.length();

        // begin regex matching

        Pattern pattern = Pattern.compile(" [Сс]таж| [Оо]пыт");
        Matcher matcher = pattern.matcher(text1);
        if(matcher.find()) {
            ind_begin = matcher.end();
        }
        Pattern pattern1 = Pattern.compile(" год.| лет");
        Matcher matcher1 = pattern1.matcher(text1);
        if(matcher1.find()) {
            ind_end = matcher1.start();
        }

        // get numbers from substring
        int experience = 0;

        if(ind_begin !=0 && ind_end != text1.length()){
            Pattern pattern2 = Pattern.compile("\\d+");
            Matcher matcher2 = pattern2.matcher(text1.substring(ind_begin,ind_end));
            if(matcher2.find()){
                experience =Integer.parseInt(matcher2.group());
            }
        }
        return experience;
    }

    public static ArrayList<String> readTagFile(String path) throws IOException {
        Scanner in  = new Scanner(new FileInputStream(path),"cp1251");
        ArrayList<String> tagList = new ArrayList<>();
        while(in.hasNext()) {
            tagList.add(in.nextLine());
        }
        in.close();
//        System.out.println(tagList);
        return tagList;
    }

    public static ArrayList<String> getTags(ArrayList<String> tags, String text){

        ArrayList<String> tags_ok = new ArrayList<>();
        for (int i=0;i<tags.size();i++) {
            int a = FuzzySearch.partialRatio(tags.get(i).toUpperCase(), text.toUpperCase());
            if (a > 85)
                tags_ok.add(tags.get(i));
        }
        return tags_ok;
    }


}
