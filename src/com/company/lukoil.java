package com.company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class lukoil  {
    public static ArrayList<Vacancy> call() throws IOException {

        System.out.println("lukoil call started at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        ArrayList<Vacancy> Vacancies = new ArrayList<>();

        // connect and expand page
//        String url = "http://www.lukoil.ru/Company/Career/Vacancies";
        ChromeOptions opts = new ChromeOptions();
        opts.addArguments("--test-type");
        System.setProperty("webdriver.chrome.driver", "d:/IWTGAJ/java/LIB/chromedriver_win32 (2)/chromedriver.exe");
        WebDriver driver = new ChromeDriver(opts);
        driver.get( "http://www.lukoil.ru/Company/Career/Vacancies");
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
//        for (int i=0;i<1;i++){
            Element job = jobs.get(i);
//            System.out.println(job);
            Elements q = job.getElementsByAttributeValue("data-bind", "text: City");


            String url = job.getElementsByAttribute("href").last().attr("href");
            url = "http://www.lukoil.ru/Company/Career/Vacancies" + url.substring(url.lastIndexOf("/"));

            String location = job.getElementsByAttributeValue("data-bind", "text: City").text();
            String region = job.getElementsByAttributeValue("data-bind", "text: Region").text();
            String country = job.getElementsByAttributeValue("data-bind", "text: Country").text();

            String title = job.getElementsByAttributeValue("data-bind", "text: Title, " +
                    "attr: {href: '/ru/Company/Career/Vacancies/Vacancy' + '?id=' + VacancyID }").text();
            String employer = job.getElementsByAttributeValue("data-bind", "html: Organization").text();

            String requirements = job.getElementsByAttributeValue("data-bind", "html: Requirements").text();
            String responsibilities = job.getElementsByAttributeValue("data-bind", "html: Responsibility").text();
            String conditions = job.getElementsByAttributeValue("data-bind", "html: Conditions").text();
            String fieldOfActivity = job.getElementsByAttributeValue("data-bind", "text: FieldOfActivity").text();

            ArrayList<String> allTags = new ArrayList<>();
            allTags = NatLangProcessing.readTagFile("d://IWTGAJ/java/info/IT.txt");
            allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/ED.txt"));
            allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/OTHER.txt"));

//         analyze natural language

//         change splitting for semicolon

            String[] answer = responsibilities.split("•");
            responsibilities = String.join(";", answer);

            answer = requirements.split("•");
            requirements = String.join(";", answer);

            answer = conditions.split("•");
            conditions = String.join(";", answer);

//         parse for experience

            int experience = NatLangProcessing.experience(requirements);

//        parse for tags

            ArrayList<String> a = NatLangProcessing.getTags(allTags, requirements);
            String tags = "";
            for (String s : a) {
                tags = tags + s + ";";
            }

            Vacancy vac1 = new Vacancy(url, title, employer, location, region, "", "",
                    requirements, responsibilities, experience, tags, conditions, fieldOfActivity, "");
            Vacancies.add(vac1);
//            vac1.print();
        }

        System.out.println("lukoil call finished at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        return Vacancies;
    }
}
