package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class novatek {
    public static ArrayList<Vacancy> call() throws IOException {

        System.out.println("novatek call started at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        ArrayList<Vacancy> Vacancies = new ArrayList<>();
        String url = "http://www.novatek.ru/ru/about/career/jobs";
        Document doc = Jsoup.connect(url).get();

        String q = doc.getElementsByAttributeValue("class","center").toString();
        String[] qq = q.split("<p>&nbsp;</p> ");

        // load taglist for parsing

        ArrayList<String> allTags = new ArrayList<>();
        allTags = NatLangProcessing.readTagFile("d://IWTGAJ/java/info/IT.txt");
        allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/ED.txt"));
        allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/OTHER.txt"));

        for (int Q=0; Q<qq.length; Q++) {

            String qqq = qq[Q];

            Document doc1 = Jsoup.parse(qqq);
            String employer = doc1.getElementsByTag("p").first().text().split(":")[0];
            String contacts = doc1.getElementsByTag("p").first().text().split(":")
                    [doc1.getElementsByTag("p").first().text().split(":").length - 1];

            Elements titles = doc1.select("a[class=\"slider\"]");
            Elements contents = doc1.select("div[class=\"slider\"]");

            for (int i = 0; i < contents.size(); i++) {
                if (!contents.get(i).text().contains(" ")) {
                    contents.remove(i);
                }
            }

            for (int i = 0; i < contents.size(); i++) {

//                System.out.println(i);
                String responsibilities = "";
                String requirements = "";
                Element a4 = contents.get(i);
                Elements tmp = a4.getElementsByTag("ul").first().getElementsByTag("li");
                for (Element tmp1 : tmp) {
                    responsibilities = responsibilities + ";" + tmp1.text();
                }
                tmp = a4.getElementsByTag("ul").last().getElementsByTag("li");
                for (Element tmp1 : tmp) {
                    requirements = requirements + ";" + tmp1.text();
                }

                // parse for experience
                int experience = NatLangProcessing.experience(requirements);

                //parse for tags

                ArrayList<String> a = NatLangProcessing.getTags(allTags, requirements);
                String tags = "";
                for (String s : a) {
                    tags = tags + s + ";";
                }
                Vacancy vac1 = new Vacancy(url, titles.get(i).text(), employer, "", "", "",
                        "", requirements, responsibilities, experience, tags, "", "", contacts);
//                vac1.print();
                Vacancies.add(vac1);
            }
        }

        System.out.println("novatek call finished at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        return Vacancies;
    }

}
