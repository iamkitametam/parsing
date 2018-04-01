package com.company;

import java.io.IOException;
import java.util.ArrayList;

public class Main{
    public static void main(String[] args) throws IOException {
        ArrayList<Vacancy> Vacancies = new ArrayList<>();
        Vacancies = gazprom.call();
    }
}

class Vacancy{
    public String url, title, employer, location, region, jobType, publicationDate, requirements, responsibilities, tags;
    public int experience;


    public Vacancy(String url, String title, String employer, String location, String region, String jobType, String publicationDate, String requirements, String responsibilities, int experience, String tags){
        this.url = url;
        this.title = title;
        this.employer = employer;
        this.location = location;
        this.region = region;
        this.jobType = jobType;
        this.publicationDate = publicationDate;
        this.requirements = requirements;
        this.responsibilities = responsibilities;
        this.experience = experience;
        this.tags = tags;
    }

    public void print(){
        System.out.println("url: " + url);
        System.out.println("title: " + title);
        System.out.println("employer: " + employer);
        System.out.println("location: " + location);
        System.out.println("region: " + region);
        System.out.println("jobType: " + jobType);
        System.out.println("published on: "+ publicationDate);
        System.out.println("requirements: " + requirements);
        System.out.println("responsibilities: " + responsibilities);
        System.out.println("experience: " + experience);
        System.out.println("tags: " + tags);
        System.out.println("\n");
    }
}



/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////


//package com.company;
//
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.sl.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.jsoup.*;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.apache.poi.*;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.URLDecoder;
//import java.util.ArrayList;

//
//public class Main{
//    public static void main(String[] args) throws IOException {
//
//        //array for jobitems
//        ArrayList<JobItemClass> items = new ArrayList<>();
//        ArrayList<Vacancy> Vacancies = new ArrayList<>();
//
//        Document doc = Jsoup.connect("http://www.gazpromvacancy.ru/jobs").get();
//
//        // get number of pages
//        int numberOfPages = Integer.parseInt(doc.getElementsByClass("last").first().toString().replaceAll("\\D+",""));
//        System.out.println(numberOfPages);
//
//        for(int i = 0;i<=numberOfPages;i++){
//
//            // establish connection to i-th page
//            String pageURL = "http://www.gazpromvacancy.ru/jobs/page/"+Integer.toString(i);
//            Document docI = Jsoup.connect(pageURL).get();
//            System.out.println(pageURL);
//
//            // get list of item's parameters
//            Elements jobListItems = docI.getElementsByAttributeValue("class","job-list-item");
//
//            // get first item
//            for (int j = 0; j <jobListItems.size(); j++) {
//                Element jobItem = jobListItems.get(j);
//                Element jobParams = jobItem.child(0);
//
//                //get parameters from item
//                String url = "http://gazpromvacancy.ru/" + jobItem.getElementsByTag("a").last().attr("href");
//                String datePub = jobParams.getElementsByAttribute("datetime").text();
//                String jobLocation = jobParams.getElementsByClass("job-location").text();
//                String jobEmployer = jobParams.getElementsByClass("job-employer").text();
//                String title = jobItem.getElementsByTag("a").last().text();
//
//                // add item to list
//                JobItemClass item1 = new JobItemClass(url, title, datePub, jobLocation, jobEmployer);
//                items.add(item1);
////                item1.print();
//            }
//
//        }
//
//        //begin parsing item's pages
//
//        for (int i = 0; i<items.size(); i++) {
//
//            JobItemClass item = items.get(i);
//
//            String currentItemURL = item.url;
//            Document docPage = Jsoup.connect(currentItemURL).get();
//
//            // get requirements and responsibilities
//
//            Elements jobInfo = docPage.getElementsByAttributeValue("class","job-info plain");
//            String responsibilities = jobInfo.get(0).text();
//            String requirements = jobInfo.get(1).text();
//            responsibilities = responsibilities.replace("Обязанности","");
//            requirements = requirements.replace("Требования","");
//
//            // analyze natural language
//            NatLangProcessing NLP = new NatLangProcessing();
////            System.out.println("NLP");
//            // change splitting for semicolon
//            String[] answer = NatLangProcessing.splitter(responsibilities);
//            responsibilities = String.join(";",answer);
//            answer = NatLangProcessing.splitter(requirements);
//            requirements = String.join(";",answer);
//
////            System.out.println(requirements);
//            int experience = NatLangProcessing.experience(requirements);
//
//            // parse job-params inline class for region, job type
//
//            Elements jobParams = docPage.getElementsByAttributeValue("class", "job-params inline");
//            Elements fields = jobParams.select("dt");
//            Elements values = jobParams.select("dd");
//            String region = "";
//            String jobType = "";
//            int j = 0;
//            for (Element elem1 : fields) {
//                if (elem1.text().equals("Регион")) {
//                    region = values.get(j).text();
//                }
//                if (elem1.text().equals("Режим работы")) {
//                    jobType = values.get(j).text();
//                }
//                j++;
//            }
//
//            // create vacancy
//            Vacancy vac1 = new Vacancy(item.url, item.title, item.employer, item.location, region, jobType, item.publicationDate, requirements, responsibilities, experience);
//            Vacancies.add(vac1);
//            vac1.print();
//        }
//
//        // write to xlsx
//        String myfile = "d://IWTGAJ/java/database/gazprom.xls";
//        Workbook wb = new HSSFWorkbook();
//        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("sheet1");
////        System.out.println(Vacancies.size());
//        for(int i=0;i<Vacancies.size();i++) {
//            Row row = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(i);
//            row.createCell(0).setCellValue(Vacancies.get(i).url);
//            row.createCell(1).setCellValue(Vacancies.get(i).title);
//            row.createCell(2).setCellValue(Vacancies.get(i).employer);
//            row.createCell(3).setCellValue(Vacancies.get(i).location);
//            row.createCell(4).setCellValue(Vacancies.get(i).region);
//            row.createCell(5).setCellValue(Vacancies.get(i).jobType);
//            row.createCell(6).setCellValue(Vacancies.get(i).publicationDate);
//            row.createCell(7).setCellValue(Vacancies.get(i).requirements);
//            row.createCell(8).setCellValue(Vacancies.get(i).responsibilities);
//        }
//        wb.write(new FileOutputStream(myfile));
//        wb.close();
//    }
//}
//
//
//class Vacancy{
//    public String url, title, employer, location, region, jobType, publicationDate, requirements, responsibilities;
//    public int experience;
//
//
//    public Vacancy(String url, String title, String employer, String location, String region, String jobType, String publicationDate, String requirements, String responsibilities, int experience){
//        this.url = url;
//        this.title = title;
//        this.employer = employer;
//        this.location = location;
//        this.region = region;
//        this.jobType = jobType;
//        this.publicationDate = publicationDate;
//        this.requirements = requirements;
//        this.responsibilities = responsibilities;
//        this.experience = experience;
//    }
//
//    public void print(){
//        System.out.println("url: " + url);
//        System.out.println("title: " + title);
//        System.out.println("employer: " + employer);
//        System.out.println("location: " + location);
//        System.out.println("region: " + region);
//        System.out.println("jobType: " + jobType);
//        System.out.println("published on: "+ publicationDate);
//        System.out.println("requirements: " + requirements);
//        System.out.println("responsibilities: " + responsibilities);
//        System.out.println("experience: " + experience);
//        System.out.println("\n");
//    }
//}
//
//class JobItemClass{
//
//    public String url, title, publicationDate, location, employer;
////    public String publicationDate;
////    public String location;
////    public String employer;
//
//    public JobItemClass(String url, String title, String publicationDate, String location, String employer){
//        this.url = url;
//        this.title = title;
//        this.publicationDate = publicationDate;
//        this.location = location;
//        this.employer = employer;
//    }
//
//    public void print(){
//        System.out.println("url = " + url);
//        System.out.println("title = " + title);
//        System.out.println("published on: "+ publicationDate);
//        System.out.println("located: "+ location);
//        System.out.println("employer: "+ employer);
//        System.out.println("\n");
//    }
//}
//
//
