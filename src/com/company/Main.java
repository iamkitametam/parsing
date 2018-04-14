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

    }

    public static void save(ArrayList<Vacancy> Vacancies, String myfile) throws IOException{

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

