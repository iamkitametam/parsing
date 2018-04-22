package com.company;

import org.jsoup.Jsoup;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.jetty.html.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import javax.lang.model.util.Elements;
import javax.print.Doc;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sberbank {
    public static ArrayList<Vacancy> call() throws IOException {

        System.out.println("sberbank call started at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        ArrayList<Vacancy> Vacancies = new ArrayList<>();

        ArrayList<String> links = getLinks();

        ArrayList<String> allTags = new ArrayList<>();
        allTags = NatLangProcessing.readTagFile("d://IWTGAJ/java/info/IT.txt");
        allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/ED.txt"));
        allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/OTHER.txt"));


        for (int Q=0;Q<links.size();Q++){
            String link = "https://career18.sapsf.com" + links.get(Q);
            System.out.println("Link: " + Q + " of " + links.size());
//        for (String link: links) {

//        String link = "https://career18.sapsf.com/career?career%5fns=job%5flisting&company=paosberbanP2&navBarLevel=" +
//                "JOB%5fSEARCH&rcm%5fsite%5flocale=ru%5fRU&career_job_req_id=85375&selected_lang=ru_RU&jobAlertController" +
//                "_jobAlertId=&jobAlertController_jobAlertName=&_s.crb=nOeHYgRJnW4%2beXXzv1v%2f8XlA4sE%3d";

            Document doc = Jsoup.connect(link).get();
            String title = doc.title().replace("Карьерные возможности:", "");
            title = title.substring(0, title.indexOf("("));

            String line = doc.select("div > div[tabIndex=\"0\"]").toString();
            line = line.substring(line.indexOf("Номер вакансии"), line.length() - 1);
            line = line.substring(0, line.indexOf("</div>"));
            org.jsoup.select.Elements lineEl = Jsoup.parse(line).getElementsByTag("b");

//            System.out.println(lineEl);

            String publicationDate = "";
            String fieldOfActivity = "";
            String region = "";
            String location = "";

            if(lineEl.size() > 1) {
                publicationDate = lineEl.get(1).text();
            }
            if(lineEl.size() > 2) {
                fieldOfActivity = lineEl.get(2).text();
            }
            if(lineEl.size() > 3) {
                region = lineEl.get(3).text();
            }
            if(lineEl.size() > 4) {
                location = lineEl.get(4).text();
            }

            String description = doc.select("div[class=\"joqReqDescription\"]").toString();

            Integer ind_begin = 0;
            Integer ind_end = description.length() - 1;

            // split text for 2 parts - responsibilities and requirements

            if (description.indexOf("Функции") > 0)
                ind_begin = description.indexOf("Функции");
            else if (description.indexOf("Обязанности") > 0) {
                ind_begin = description.indexOf("Обязанности");
            }
            description = description.substring(ind_begin, ind_end);

            if (description.indexOf("</ul>") > 0) {
                ind_end = description.indexOf("</ul>");
            }

            //get responsibilities

            if(ind_end > description.length()-1){
                ind_end = description.length()-1;
            }

            String responsibilities1 = description.substring(0, ind_end);
            Document doc2 = Jsoup.parse(responsibilities1);
            org.jsoup.select.Elements responsibilities2 = doc2.getElementsByTag("li");

            String responsibilities = "";
            for (Element tmp : responsibilities2) {
                responsibilities = responsibilities + ";" + tmp.text();
            }
//        System.out.println(responsibilities);

            // get requirements

            String requirements1 = description.substring(ind_end, description.length() - 1);
            Document doc3 = Jsoup.parse(requirements1);
            org.jsoup.select.Elements requirements2 = doc3.getElementsByTag("li");

            String requirements = "";
            for (Element tmp : requirements2) {
                requirements = requirements + "; " + tmp.text();
            }

//        System.out.println(requirements);

            int experience = NatLangProcessing.experience(requirements);

            ArrayList<String> a = NatLangProcessing.getTags(allTags, requirements);
            String tags = "";
            for (String s : a) {
                tags = tags + s + ";";
            }

            Vacancy vac1 = new Vacancy(link, title, "ПАО Сбербанк", location, region, "", publicationDate,
                    requirements, responsibilities, experience, tags, "", fieldOfActivity, "");
            Vacancies.add(vac1);

//            vac1.print();
        }

        System.out.println("sberbank call finished at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        return Vacancies;
    }

    public static ArrayList<String> getLinks(){

        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--test-type");
        System.setProperty("webdriver.chrome.driver", "d:/IWTGAJ/java/LIB/chromedriver_win32 (2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver(opts);
        driver.get( "https://career18.sapsf.com/career?company=paosberbanP2&career_ns=job_listing_summary&navBarLevel=" +
                "JOB_SEARCH&_s.crb=gE5NiqqfqEqHiIrLGt4fwSvJIrw%3d");

        try {
            Thread.sleep(5000);
        }
        catch (Exception e){
            System.out.println("Sleep error");
        }

        // select max number o page
        Select s1 = new Select(((ChromeDriver) driver).findElement(By.id("38:")));
        s1.selectByValue("150");

        try {
            Thread.sleep(5000);
        }
        catch (Exception e){
            System.out.println("Sleep error");
        }

        // get links
        ArrayList<String> links = new ArrayList<String>();
        Document doc = Jsoup.parse(driver.getPageSource());
        org.jsoup.select.Elements pageVac = doc.getElementsByAttributeValue("class","jobTitle");
        for (Element tmp:pageVac){
            links.add(tmp.attr("href"));
//            System.out.println(tmp.attr("href"));
        }
        String prev1 = links.get(links.size()-1);
        String prev2 = "";
        while (true){
            try{
                ((ChromeDriver) driver).findElementById("37:_next").click();
                try {
                    Thread.sleep(2000);
                }
                catch (Exception e){
                    System.out.println("Sleep error");
                }
                doc = Jsoup.parse(driver.getPageSource());
                pageVac = doc.getElementsByAttributeValue("class","jobTitle");
                for (Element tmp:pageVac){
                    links.add(tmp.attr("href"));
//                    System.out.println(tmp.attr("href"));
                }
                prev2 = links.get(links.size()-1);
                if(prev1.equals(prev2)){
                    break;
                }
                else{
                    prev1 = prev2;
                }
            }
            catch (Exception e){
                break;
            }

        }

        driver.quit();
        return links;
    }
}
