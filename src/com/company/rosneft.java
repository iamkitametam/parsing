package com.company;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class rosneft {

    public static ArrayList<Vacancy> call() throws IOException {

        System.out.println("rosneft call started at: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
        ArrayList<Vacancy> Vacancies = new ArrayList<>();


        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--test-type");
        System.setProperty("webdriver.chrome.driver", "d:/IWTGAJ/java/LIB/chromedriver_win32 (2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver(opts);
        driver.get( "https://www.rosneft.ru/about/career/");
        try {
            ((ChromeDriver) driver).findElementByClassName("yellow-button").click();
            Thread.sleep(10000);
        }
        catch (Exception e){
            System.out.println("click error");
        }
        Document doc = Jsoup.parse(driver.getPageSource());
        driver.quit();

        Elements q = doc.getElementsByAttributeValue("class","sl-holder");

        for (int Q = 0; Q<q.size(); Q++) {
//        for (int Q = 150;Q<151;Q++){

            Elements qq = q.get(Q).getElementsByTag("tr");

            if(qq.size()>0)  {

                String responsibilities = "", requirements = "", conditions = "", jobType = "", contacts = "";
                int experience = 0;

                String title = q.get(Q).getElementsByAttributeValue("class", "sl-item-title").text();
                String location = q.get(Q).getElementsByAttributeValue("class", "block").first().text().replace("Город: ", "");
                String employer = q.get(Q).getElementsByAttributeValue("class", "block").last().text().replace("Компания: ", "");

                for (int i = 0; i < qq.size(); i++) {
//                System.out.println(i);
                    if (qq.get(i).toString().contains("Обязанности")) {
                        responsibilities = qq.get(i + 1).text();
                    } else if (qq.get(i).toString().contains("Требования")) {
                        requirements = qq.get(i + 1).text();
                    } else if (qq.get(i).toString().contains("Знания")) {
                        requirements = requirements + ";" + qq.get(i + 1).text();
                    } else if (qq.get(i).toString().contains("Опыт работы")) {
                        experience = NatLangProcessing.experience(qq.get(i + 1).text());
                    } else if (qq.get(i).toString().contains("График работы")) {
                        conditions = conditions + ";" + qq.get(i + 1).text();
                    } else if (qq.get(i).toString().contains("Условия")) {
                        conditions = conditions + ";" + qq.get(i + 1).text();
                    } else if (qq.get(i).toString().contains("Тип занятости")) {
                        jobType = jobType + ";" + qq.get(i + 1).text();
                    } else if (qq.get(i).toString().contains("Контакты")) {
                        for (int j = i + 1; j < qq.size(); j++) {
                            contacts = contacts + ";" + qq.get(j).text();
                        }
                    }
                }

                // tags

                ArrayList<String> allTags = new ArrayList<>();
                allTags = NatLangProcessing.readTagFile("d://IWTGAJ/java/info/IT.txt");
                allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/ED.txt"));
                allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/OTHER.txt"));

                ArrayList<String> a = NatLangProcessing.getTags(allTags, requirements);
                String tags = "";
                for (String s : a) {
                    tags = tags + s + ";";
                }

                //

                Vacancy vac1 = new Vacancy("https://www.rosneft.ru/about/career/", title, employer, location,
                        "", jobType, "", requirements, responsibilities, experience, tags,
                        conditions, "", contacts);
                Vacancies.add(vac1);

//                vac1.print();
            }
            else{

                String responsibilities = "", requirements = "", conditions = "", jobType = "", contacts = "";
                int experience = 0;

                String title = q.get(Q).getElementsByAttributeValue("class", "sl-item-title").text();
                String location = q.get(Q).getElementsByAttributeValue("class", "block").first().text().
                        replace("Город: ", "");
                String employer = q.get(Q).getElementsByAttributeValue("class", "block").last().text().
                        replace("Компания: ", "");

                Elements a1 = q.get(Q).getElementsByTag("strong");
                Elements a2=  q.get(Q).getElementsByTag("ul");

                if(a1.size()==a2.size()) {
                    for (int j = 0; j < a1.size(); j++) {
                        if (a1.get(j).toString().contains("Обязанности")) {
                            responsibilities = a2.get(j).text();
                        }
                        if (a1.get(j).toString().contains("Требования")) {
                            requirements = a2.get(j).text();
                        }
                        if (a1.get(j).toString().contains("Условия")) {
                            conditions = a2.get(j).text();
                        }
                    }
                }

                Elements a3 = q.get(Q).getElementsByTag("p");
                for (int j=0;j<a3.size();j++){
                    if(a3.get(j).text().contains("опыт")){
                        String[] experience_str = a3.get(j).nextSibling().toString().
                                replaceAll("\\D+"," ").trim().split(" ");
                        try {
                            experience = Integer.parseInt(experience_str[0]);
                        }catch (Exception e){
                            experience = 0;
                        }
                    }
                    if(a3.get(j).text().contains("Тип занятости")){
                        if(j<a3.size()-1) {
                            jobType = a3.get(j + 1).text();
                        }
                    }
//                    System.out.println(a3.get(j).nextSibling());
                }

                // tags

                ArrayList<String> allTags = new ArrayList<>();
                allTags = NatLangProcessing.readTagFile("d://IWTGAJ/java/info/IT.txt");
                allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/ED.txt"));
                allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/OTHER.txt"));

                ArrayList<String> a = NatLangProcessing.getTags(allTags, requirements);
                String tags = "";
                for (String s : a) {
                    tags = tags + s + ";";
                }

                //


                Vacancy vac1 = new Vacancy("https://www.rosneft.ru/about/career/", title, employer, location,
                        "", jobType, "", requirements, responsibilities, experience, tags,
                        conditions, "", contacts);
                Vacancies.add(vac1);

//                System.out.println(Q);
//                vac1.print();

            }

        }

        System.out.println("rosneft call finished at: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
        return Vacancies;
    }
}
