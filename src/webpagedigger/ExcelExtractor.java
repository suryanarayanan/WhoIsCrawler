
package webpagedigger;
import com.moyosoft.connector.com.*;
import com.moyosoft.connector.exception.*;
import com.moyosoft.connector.ms.excel.*;
import java.io.File;

/**
 *
 * @author Surya
 */

public class ExcelExtractor {
    /*dummy Constructor*/
    public ExcelExtractor(){}

    public String[] extractExcel(int startingRow,int endingRow){
        String[] domainList = new String[endingRow - startingRow +1];
        try
        {
            /* Create the Excel application object */
            Excel excel = new Excel();
            try
            {
                /*load the excel book*/
                Workbook workbook = excel.openWorkbook(new File("ICUsers.xlsx"));
                /* Get the active worksheet in the Excel workbook */
                Worksheet worksheet = workbook.getActiveWorksheet();
                String domain;
                for(int i=startingRow-1;i<endingRow;i++){
                    domain = worksheet.getCell(i,"I").getValue();
                    domainList[i-startingRow+1]=domain;
                }
            }
            finally
            {
                /* Dispose the library */
                excel.dispose();
            }
        }
        catch (ComponentObjectModelException ex)
        {
            System.out.println("COM error has occured: ");
            ex.printStackTrace();
        }
        catch (LibraryNotFoundException ex)
        {
            System.out.println("The Java Excel Library hasn't been found.");
            ex.printStackTrace();
        }
        return domainList;
    }
}
