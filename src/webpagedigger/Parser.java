
package webpagedigger;
import java.util.regex.*;
/**
 *
 * @author Surya
 */
public class Parser {
    /* dummy constructor */
    public Parser(){}

    public String parse(String domain,String toParse,String whatFor){
        String toReturn=" ";

        if(whatFor.equals("phone")){
            String myPatterns[] = new String[10];

/* regex for US phone numbers */

            myPatterns[0] = "(([\\+]1[\\.])[0-9]{10})";
/* India */
            myPatterns[1] = "([\\+]?(91)[\\s|-]+[0-9]{2,3}[\\s|\\-]+[1-9]{1}[0-9]{5,7}\\s)";
/* general numbers */
            myPatterns[2] = "((\\s|:)\\(?(\\d{3})\\)?[\\- ]?(\\d{3})[\\- ]?(\\d{4})\\s)";
            myPatterns[3] = "((\\s|:)[+]?\\d{2}\\.\\d{9,11}\\s)";
            myPatterns[4] = "((\\s|:)\\d{3}\\-\\d{8}\\s)";
            myPatterns[5] = "((\\s|:)[\\+]?\\d{2}\\.\\d{10}\\s)";
            myPatterns[6] = "((\\s|:)[+][0-9]\\d{2}\\-\\d{3}\\-\\d{4}\\s)";
/* general regex phone numbers */
            myPatterns[7] = "((\\+44\\s?7\\d{3}|\\(?07\\d{3}\\)?)\\s?\\d{3}\\s?\\d{3}\\s)";
            myPatterns[8] = "((\\s|:)[0-9]{2,3}[\\-]?[\\s]?[0-9]{6,7}\\s)";
            myPatterns[9] = "((\\s|:)0[234679]{1}[\\s]{0,1}[\\-]{0,1}[\\s]{0,1}[1-9]{1}[0-9]{5,7}\\s)";
           
            for(int i=0;i<10;i++){
               Matcher matcher = Pattern.compile(myPatterns[i],Pattern.CASE_INSENSITIVE).matcher(toParse);
               if (matcher.find()) {
                   int start = matcher.start(1);
                   int end = matcher.end(1);
                   toReturn = toParse.substring(start, end);
                   break;
               }
            }
        }
        
        else if(whatFor.equals("company")){
            String myPatterns[]=new String [2];
        /* Regex to extract the company name */
            myPatterns[0] = "([\\s|:][[\\w]+|\\s|\\,|\\-|!|&|#|\\(|\\)]+\\s(Inc|Ltd|Org|Corp|Company|Corporation)[\\.|\\s])";
            myPatterns[1] = "((Organization)([\\s|:]+[[\\w]+|\\s]+)\\s)";
//            myPatterns[1] = "([\\s|:]?[\\w]+[[\\w]+|\\s|\\,|\\-]+\\s(Inc|Ltd|Org|Corp|Company|Corporation)[\\.|\\s])";
/*            Recent Change space or : zero or one time in the front. */
            int flag=0;
            for(int i=0;i<2;i++){
                /* compile the regex */
                Matcher matcher = Pattern.compile(myPatterns[i]).matcher(toParse);

                if (matcher.find()) {
                    flag=1;
                    int start = matcher.start(1);
                    int end = matcher.end(1);
                    toReturn = toParse.substring(start, end);
                    break;
                }
            }
            if(flag==0){
                    toReturn = domain.substring(0, domain.indexOf(".")).toUpperCase();

             }
        }

        else{
            String myPatterns[] = new String[3];
            /* Regex to extract the address. Lots of room for improvement */
//            myPatterns[2] = "^[ \\w]{3,}([A-Za-z]\\.)?([ \\w]*\\#\\d+)?(\r\n| )[ \\w]{3,},\\x20[A-Za-z]{2}\\x20\\d{5}(-\\d{4})?$";
              myPatterns[0] = "((contact|address|communication|street[\\d]?|city|state)[\\s]?[\\.|:|\\n][\\s]?[\\w+|\\s|\\,|\\-|:|#|&|\\.|\\n|\\(|\\)|!]+([[a-zA-Z0-9._-]+@][a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3}[\\s])?)";
              myPatterns[1] = "([a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,3})";
//            myPatterns[2] = "([\\s|:][a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}[\\s])";

            /* compile the regex */
              Matcher matcher = Pattern.compile(myPatterns[0],Pattern.CASE_INSENSITIVE).matcher(toParse);
              if(matcher.find()) {
                  int start = matcher.start(1);
                  int end = matcher.end(1);
                  toReturn = toParse.substring(start, end);
              }
              Pattern emailPattern = Pattern.compile(myPatterns[1],Pattern.CASE_INSENSITIVE);
              matcher = emailPattern.matcher(toReturn);
             /* If address can't be extracted, then extract the email atleast */
              if(!matcher.find()){
                  matcher = emailPattern.matcher(toParse);
                  if(matcher.find()){
                    int start = matcher.start(1);
                    int end = matcher.end(1);
                    toReturn +=" "+ toParse.substring(start, end);
                  }
              }
        }
        if(!toReturn.equals(" "))
        return filter(toReturn);
        else return "No data";
        /* return No data if no luck */
    }

    /* Function to filter the parsed details. Can be improved a lot */
    public String filter(String toFilter){
        String filterString ="(((domain manager)|(domain administrator)|(domain admin)|([\\s]domain)|(administrator)|(admin)|(contact)|(address)|(communication)|(email)|(registrant)|(street[\\d])|(\\.\\.)|(\\s\\s\\s))[:|\\s]+)";
        Pattern pattern = Pattern.compile(filterString,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(toFilter);
        String finalString = matcher.replaceAll("").trim();
    /*replacing the ':' character from the company. but this makes execution a bit slower */
   //  if(whatFor.equals("company"))return finalString.replace(':',' ');
        return finalString;
    }
}
