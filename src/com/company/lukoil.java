package com.company;
//package org.openqa.selenium.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class lukoil  {
    public static ArrayList<Vacancy> call() throws IOException {

        ArrayList<Vacancy> Vacancies = new ArrayList<>();

        // connect and expand page
        String url = "http://www.lukoil.ru/Company/Career/Vacancies";
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--test-type");
        System.setProperty("webdriver.chrome.driver", "d:/IWTGAJ/java/LIB/chromedriver_win32 (2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver(opts);
        driver.get(url);
//        Integer a = 1;
        while (true){
            try {
                driver.findElement(By.className("button-loadmore")).click();
//                System.out.println(a);
                Thread.sleep(1000);
//                a++;
            }
            catch (Exception e){
                break;
            }
        }

        // get page

        Document doc = Jsoup.parse(driver.getPageSource());
        driver.quit();

//        System.out.println(doc);

        // get list of vacancies items

        Elements jobs = doc.getElementsByAttributeValue("class","panel-default panel-collapsible panel-vacancies");

        for (int i=0;i<jobs.size();i++) {

            Element job = jobs.get(i);
//            System.out.println(job);
            Elements q = job.getElementsByAttributeValue("data-bind", "text: City");
//            System.out.println("azaza");

//        System.out.println(q);
//        System.out.println(q.text());

            String location = job.getElementsByAttributeValue("data-bind", "text: City").text();
//            System.out.println(location);

            String region = job.getElementsByAttributeValue("data-bind", "text: Region").text();
//            System.out.println(region);

            String country = job.getElementsByAttributeValue("data-bind", "text: Country").text();
//            System.out.println(country);

            String title = job.getElementsByAttributeValue("data-bind", "text: Title, attr: {href: '/ru/Company/Career/Vacancies/Vacancy' + '?id=' + VacancyID }").text();
//            System.out.println(title);

            String employer = job.getElementsByAttributeValue("data-bind", "html: Organization").text();
//            System.out.println(employer);

            String requirements = job.getElementsByAttributeValue("data-bind", "html: Requirements").text();
//            System.out.println(requirements);

            String responsibilities = job.getElementsByAttributeValue("data-bind", "html: Responsibility").text();
//            System.out.println(responsibilities);

            ArrayList<String> allTags = new ArrayList<>();
            allTags = NatLangProcessing.readTagFile("d://IWTGAJ/java/info/IT.txt");
            allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/ED.txt"));
            allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/OTHER.txt"));

//         analyze natural language

//         change splitting for semicolon

//        String[] answer = NatLangProcessing.splitter(responsibilities);
//        responsibilities = String.join(";",answer);
//        answer = NatLangProcessing.splitter(requirements);
//        requirements = String.join(";",answer);

            String[] answer = responsibilities.split("•");
            responsibilities = String.join(";", answer);

            answer = requirements.split("•");
            requirements = String.join(";", answer);

//         parse for experience

            int experience = NatLangProcessing.experience(requirements);

//        parse for tags

            ArrayList<String> a = NatLangProcessing.getTags(allTags, requirements);
            String tags = "";
            for (String s : a) {
                tags = tags + s + ";";
            }

//            System.out.println("####################################################");
//            System.out.println(requirements);
//            System.out.println(tags);

            Vacancy vac1 = new Vacancy(url, title, employer, location, region, "", "", requirements, responsibilities, experience, tags);
            Vacancies.add(vac1);
//            vac1.print();
        }

        return Vacancies;
    }
}
