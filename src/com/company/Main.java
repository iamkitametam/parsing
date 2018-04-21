package com.company;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Main{
    public static void main(String[] args) throws IOException {

        ArrayList<Vacancy> Vacancies = new ArrayList<>();

        Vacancies = gazprom.call();
        save(Vacancies,  "d://IWTGAJ/java/database/gazprom.xls");

        Vacancies = lukoil.call();
        save(Vacancies,"d://IWTGAJ/java/database/lukoil.xls");

        Vacancies = rosneft.call();
        save(Vacancies, "d://IWTGAJ/java/database/rosneft.xls");

        Vacancies = novatek.call();
        save(Vacancies, "d://IWTGAJ/java/database/novatek.xls");
    }

    public static void save(ArrayList<Vacancy> Vacancies, String myfile) throws IOException{

        Workbook wb = new HSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("sheet1");

        //headers
        Row row = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(0);
        row.createCell(0).setCellValue("URL");
        row.createCell(1).setCellValue("TITLE");
        row.createCell(2).setCellValue("EMPLOYER");
        row.createCell(3).setCellValue("LOCATION");
        row.createCell(4).setCellValue("REGION");
        row.createCell(5).setCellValue("JOBTYPE");
        row.createCell(6).setCellValue("PUBLICATION DATE");
        row.createCell(7).setCellValue("REQUIREMENTS");
        row.createCell(8).setCellValue("RESPONSIBILITIES");
        row.createCell(9).setCellValue("EXPERIENCE");
        row.createCell(10).setCellValue("TAGS");
        row.createCell(11).setCellValue("CONDITIONS");
        row.createCell(12).setCellValue("FIELD OF ACTIVITY");
        row.createCell(13).setCellValue("CONTACTS");

        for(int i=1;i<Vacancies.size()+1;i++) {
            row = ((org.apache.poi.ss.usermodel.Sheet) sheet).createRow(i);
            row.createCell(0).setCellValue(Vacancies.get(i-1).url);
            row.createCell(1).setCellValue(Vacancies.get(i-1).title);
            row.createCell(2).setCellValue(Vacancies.get(i-1).employer);
            row.createCell(3).setCellValue(Vacancies.get(i-1).location);
            row.createCell(4).setCellValue(Vacancies.get(i-1).region);
            row.createCell(5).setCellValue(Vacancies.get(i-1).jobType);
            row.createCell(6).setCellValue(Vacancies.get(i-1).publicationDate);
            row.createCell(7).setCellValue(Vacancies.get(i-1).requirements);
            row.createCell(8).setCellValue(Vacancies.get(i-1).responsibilities);
            row.createCell(9).setCellValue(Vacancies.get(i-1).experience);
            row.createCell(10).setCellValue(Vacancies.get(i-1).tags);
            row.createCell(11).setCellValue(Vacancies.get(i-1).conditions);
            row.createCell(12).setCellValue(Vacancies.get(i-1).fieldOfActivity);
            row.createCell(13).setCellValue(Vacancies.get(i-1).contacts);
        }
        wb.write(new FileOutputStream(myfile));
        wb.close();
    }
}

class Vacancy{
    public String url, title, employer, location, region, jobType,
            publicationDate, requirements, responsibilities, tags,
            conditions, fieldOfActivity, contacts;
    public int experience;


    public Vacancy(String url, String title, String employer, String location, String region,
                   String jobType, String publicationDate, String requirements,
                   String responsibilities, int experience, String tags, String conditions,
                   String fieldOfActivity, String contacts){

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
        this.conditions = conditions;
        this.fieldOfActivity = fieldOfActivity;
        this.contacts = contacts;

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
        System.out.println("conditions: " + conditions);
        System.out.println("fieldOfActivity: " + fieldOfActivity);
        System.out.println("contacts: " + contacts);
        System.out.println("\n");
    }
}

