package com.company;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scala.util.parsing.combinator.testing.Str;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class gazprom{
    public static ArrayList<Vacancy> call() throws IOException {

        System.out.println("gazprom call started at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        //array for jobitems

        ArrayList<JobItemClass> items = new ArrayList<>();
        ArrayList<Vacancy> Vacancies = new ArrayList<>();

        Document doc = Jsoup.connect("http://www.gazpromvacancy.ru/jobs").get();

        // get number of pages

        int numberOfPages = Integer.parseInt(doc.getElementsByClass("last").first().toString().replaceAll("\\D+",""));

        for(int i = 0;i<=numberOfPages;i++){

            // establish connection to i-th page

            String pageURL = "http://www.gazpromvacancy.ru/jobs/page/"+Integer.toString(i);
            Document docI = Jsoup.connect(pageURL).get();

            // get list of item's parameters

            Elements jobListItems = docI.getElementsByAttributeValue("class","job-list-item");

            // get first item
            for (int j = 0; j <jobListItems.size(); j++) {

                Element jobItem = jobListItems.get(j);
                Element jobParams = jobItem.child(0);

                //get parameters from item
                String url = "http://gazpromvacancy.ru/" + jobItem.getElementsByTag("a").last().attr("href");
                String datePub = jobParams.getElementsByAttribute("datetime").text();
                String jobLocation = jobParams.getElementsByClass("job-location").text();
                String jobEmployer = jobParams.getElementsByClass("job-employer").text();
                String title = jobItem.getElementsByTag("a").last().text();

                // add item to list
                JobItemClass item1 = new JobItemClass(url, title, datePub, jobLocation, jobEmployer);
                items.add(item1);
            }

        }

        //begin parsing item's pages

        // load taglist for parsing

        ArrayList<String> allTags = new ArrayList<>();
        ArrayList<String> itTags = new ArrayList<>();
        ArrayList<String> edTags = new ArrayList<>();
        ArrayList<String> otherTags = new ArrayList<>();
        allTags = NatLangProcessing.readTagFile("d://IWTGAJ/java/info/IT.txt");
        allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/ED.txt"));
        allTags.addAll(NatLangProcessing.readTagFile("d://IWTGAJ/java/info/OTHER.txt"));

        // parse vacancies' pages

        for (int i = 0; i<items.size(); i++) {

            JobItemClass item = items.get(i);

            String currentItemURL = item.url;
            Document docPage = Jsoup.connect(currentItemURL).get();

            // get requirements and responsibilities

            Elements jobInfo = docPage.getElementsByAttributeValue("class","job-info plain");
            String responsibilities = jobInfo.get(0).text();
            String requirements = jobInfo.get(1).text();
            responsibilities = responsibilities.replace("Обязанности","");
            requirements = requirements.replace("Требования","");

            // analyze natural language

            // change splitting for semicolon

            String[] answer = NatLangProcessing.splitter(responsibilities);
            responsibilities = String.join(";",answer);
            answer = NatLangProcessing.splitter(requirements);
            requirements = String.join(";",answer);

            // parse for experience

            int experience = NatLangProcessing.experience(requirements);

            //parse for tags

            ArrayList<String> a  = NatLangProcessing.getTags(allTags,requirements);
            String tags = "";
            for(String s:a){
                tags = tags + s + ";";
            }

            System.out.println("####################################################");
            System.out.println(requirements);
            System.out.println(a);
//            System.out.println(b);
//            System.out.println(c);

            // parse job-params inline class for region, job type

            Elements jobParams = docPage.getElementsByAttributeValue("class", "job-params inline");
            Elements fields = jobParams.select("dt");
            Elements values = jobParams.select("dd");
            String region = "";
            String jobType = "";
            int j = 0;
            for (Element elem1 : fields) {
                if (elem1.text().equals("Регион")) {
                    region = values.get(j).text();
                }
                if (elem1.text().equals("Режим работы")) {
                    jobType = values.get(j).text();
                }
                j++;
            }

            // create vacancy

            Vacancy vac1 = new Vacancy(item.url, item.title, item.employer, item.location, region, jobType, item.publicationDate, requirements, responsibilities, experience, tags);
            Vacancies.add(vac1);
        }

        // write to xlsx

        String myfile = "d://IWTGAJ/java/database/gazprom.xls";
        Workbook wb = new HSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("sheet1");

        for(int i=0;i<Vacancies.size();i++) {
            Row row = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(i);
            row.createCell(0).setCellValue(Vacancies.get(i).url);
            row.createCell(1).setCellValue(Vacancies.get(i).title);
            row.createCell(2).setCellValue(Vacancies.get(i).employer);
            row.createCell(3).setCellValue(Vacancies.get(i).location);
            row.createCell(4).setCellValue(Vacancies.get(i).region);
            row.createCell(5).setCellValue(Vacancies.get(i).jobType);
            row.createCell(6).setCellValue(Vacancies.get(i).publicationDate);
            row.createCell(7).setCellValue(Vacancies.get(i).requirements);
            row.createCell(8).setCellValue(Vacancies.get(i).responsibilities);
            row.createCell(9).setCellValue(Vacancies.get(i).tags);
            row.createCell(10).setCellValue(Vacancies.get(i).experience);
        }
        wb.write(new FileOutputStream(myfile));
        wb.close();

        System.out.println("gazprom call finished at: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));

        return Vacancies;
    }

}

class JobItemClass{

    public String url, title, publicationDate, location, employer;

    public JobItemClass(String url, String title, String publicationDate, String location, String employer){
        this.url = url;
        this.title = title;
        this.publicationDate = publicationDate;
        this.location = location;
        this.employer = employer;
    }

    public void print(){
        System.out.println("url = " + url);
        System.out.println("title = " + title);
        System.out.println("published on: "+ publicationDate);
        System.out.println("located: "+ location);
        System.out.println("employer: "+ employer);
        System.out.println("\n");
    }
}
