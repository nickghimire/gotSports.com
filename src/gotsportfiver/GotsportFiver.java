/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotsportfiver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author apple
 */
public class GotsportFiver {

    private final static String base_url = "https://events.gotsport.com/events/Default.aspx?EventID=52227";
    private final static String main_url = "http://events.gotsport.com/events/";
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static void main(String[] args) {
        int count = 1;
        int linker = 1;
        String maindata = "";
        String empty = " ";
        String Title ="";
        String path="/users/apple/desktop/gotsport/EventID=52227.csv";
        
        RegexMatcher regexMatcher = new RegexMatcher();
        
        try {
             FileWriter writer = new FileWriter(path);
            //  td[valign=top][width=25%][style=border-style: solid; border-width:1px;border-color:#888888; background-color:white;]
            Document docs = Jsoup.connect(base_url).timeout(0).get();

            Elements allelements = docs.select("div[align=right][style=margin-left:25px;padding-left:6px;padding-bottom:6px;border: solid 1px #E9E9E9;] td[nowrap]:eq(0) a ");

            for (Element links : allelements) {

                String innerlink = links.attr("href");
                 System.out.println(innerlink);
                String link = main_url + innerlink;
                // System.out.println(link);
                Document innerdoc = Jsoup.connect(link).timeout(0).get();
                // System.out.println(innerdoc.toString());
                Elements rawLink = innerdoc.select("table[width=95%] td[nowrap] a");

                for (Element dataLink : rawLink) {
                    String rawdata = dataLink.attr("href");
                    String dataUrl = main_url + rawdata;

                    Document doc = Jsoup.connect(dataUrl).timeout(0).get();
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++");
                    Elements data = doc.select("table[bgcolor=#F5F5F5][width=100%],font[class=SubHeading]");

                    for (Element info : data) {

                        StringBuilder builder = new StringBuilder();
                        String content = info.html();
                        Scanner scan = new Scanner(content);
                        while (scan.hasNextLine()) {
                            String line = scan.nextLine();
                            builder.append(line);
                        }
                        // System.out.println(builder.toString());
                        String regex = "(.*?)Team.*?<td>(.*?)</td>.*?Coach.*?\">(.*?)</font>.*?Text\">(.*?)</font>.*?href.*?\">(.*?)</a>.*?Club.*?<td>(.*?)</td>.*?Manager.*?Text\">(.*?)</font>.*?href=.*?>(.*?)</a>";

                        Matcher matcher = regexMatcher.match(regex, builder.toString());
                        
                        
                        while (matcher.find()) {
                            
                            String division = matcher.group(1);
                            String divregex = "SubHeading\">(.*?)</font>";
                            Matcher matcher1 = regexMatcher.match(divregex, division.toString());

                            if (matcher1.find()) {
                                Title = matcher1.group(1);
                                
                                maindata= Title;
                                
                                System.out.println("Division: " + Title);

                            }else{
                                maindata = empty;
                            }
                           
                            
                            
                            String Team = matcher.group(2);
                            
                          
                            if(!Team.isEmpty()){
                            maindata = maindata + "," + Team;
                            }else{
                                maindata= maindata + ","+ empty;
                            }
                            
                            String Coach = matcher.group(3);
                            if(!Coach.isEmpty()){
                            maindata = maindata + "," + Coach;
                            }else{
                                maindata= maindata +","+ empty;
                            }
                            
                            String Phone = matcher.group(4);
                            if(!Phone.isEmpty()){
                             maindata = maindata + "," + Phone;
                            }
                             else{
                                maindata= maindata + ","+empty;
                            }
                            
                            String Email = matcher.group(5);
                            if(!Email.isEmpty()){
                            maindata = maindata + "," + Email;
                            }else{
                                maindata= maindata +","+ empty;
                            }
                            
                            String Club = matcher.group(6);
                            if(!Club.isEmpty()){
                            maindata = maindata + "," + Club;
                            }else{
                                maindata= maindata + empty;
                            }
                            
                            String Manager = matcher.group(7);
                            if(!Manager.isEmpty()){
                              maindata = maindata + "," + Manager;  
                            }
                            else{
                                maindata= maindata + ","+empty;
                            }
                            
                            String  ManagerEmail = matcher.group(8);
                            if(!ManagerEmail.isEmpty()){
                            maindata = maindata + "," + ManagerEmail +"\n";
                            }else{
                                maindata= maindata + ","+empty +"\n";
                            }
                            
                            System.out.println("Team : " + Team);
                            System.out.println("Coach: " + Coach);
                            System.out.println("Phone: " + Phone);
                            System.out.println("Email: " + Email);
                            System.out.println("Club: " + Club);
                            System.out.println("Manager: " + Manager);
                            System.out.println("Email: " + ManagerEmail);

                             writer.write(maindata);
                          
                         
                        }
                        
                        
                        
                         
                    }
                    
                           
                           
                }

                System.out.println(count);
                count++;
                linker++;
                 
                System.out.println("================================================");
            }
            writer.close();
          

        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

}
