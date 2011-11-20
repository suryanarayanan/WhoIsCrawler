package webpagedigger;
/**
 *
 * @author Surya
 */
import java.io.*;
import java.util.Scanner;
import org.apache.commons.net.WhoisClient;

public class Webdigger {

  public static void main(String[] args) {
    String hostname;
    if(args.length != 0)hostname = args[0];
    else hostname = WhoisClient.DEFAULT_HOST;
    try {
        int endingRow;
        int startingRow;
        Scanner scan = new Scanner(System.in);
        System.out.print("Starting row number : ");
        startingRow = Integer.parseInt(scan.nextLine());
        System.out.print("Ending row number : ");
        endingRow = Integer.parseInt(scan.nextLine());
        ExcelExtractor ex = new ExcelExtractor();
        
        Parser parser = new Parser();
        String domainList[] = ex.extractExcel(startingRow, endingRow);
        String company[]=new String[endingRow-startingRow+1];
        String address[]=new String[endingRow-startingRow+1];
        String phone[]=new String[endingRow-startingRow+1];
        
 /* My own whois client. It is a bit slow so commented it. */
/*
      WhoisClient whois;
      String details=null;
      for(int i=0;i< domainLength;i++){
          whois = new WhoisClient();
          try {
            whois.connect(hostname);
            details = whois.query(domainList[i]);
            whois.disconnect();
          } catch(IOException e) {
            System.err.println("Error I/O exception: " + e.getMessage());
          }
         String pattern = "^(?:[^/]+://)?([^/:]+)";
         System.out.println(details);
          String mypattern = "((whois)[\\.][\\w+]\\.[\\w+])";
          Matcher matcher = Pattern.compile(mypattern,Pattern.CASE_INSENSITIVE).matcher(details);
          if (matcher.find()) {
            int start = matcher.start(1);
            int end = matcher.end(1);
            System.out.println(details.substring(start, end));
          }
          else System.out.println("no match");
*/

  /* Using a 3rd party whois client */
      
      String s;
      Runtime run = Runtime.getRuntime();
      Process proc;
      BufferedReader in;
      String client = "whois.exe";
      try{
         for(int i=0;i<domainList.length;i++){
            StringBuffer details = new StringBuffer("");
            /* Running a process  but it is faster than manually trying whois servers */
            proc = run.exec(client+" "+domainList[i]);
            in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((s = in.readLine()) != null) {
                details.append(s);
            }
            /* company , address, phone details */
            company[i]=parser.parse(domainList[i],details.toString(),"company");
            address[i]=parser.parse(domainList[i],details.toString(),"address");
            phone[i]="No: "+parser.parse(domainList[i],details.toString(),"phone");
            System.out.println(i+1 + "  " +domainList[i]);
/*
            System.out.println(company[i]);
            System.out.println("............");
            System.out.println(address[i]);
            System.out.println("............");
            System.out.println(phone[i]);
            System.out.println("***********");
*/
            in.close();
            proc.destroy();
         }
      }catch(Exception e){
           System.out.println(e.getMessage());
      }
      /* Fill the Excel sheet with the extracted details */

      CreateExcel ce = new CreateExcel();
      ce.fillExcel(company, startingRow, "C");
      ce.fillExcel(address, startingRow, "D");
      ce.fillExcel(phone, startingRow, "E"); 
 
      System.out.println("Excel Successfully created. Open temp.xlsx to view the imported contents.");
      System.out.println("Close the Moyosoft dialog box to end the program !"); 
    }
    catch (Exception e) {
        System.err.println(e);
    }
  }
}

