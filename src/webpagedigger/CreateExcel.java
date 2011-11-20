package webpagedigger;
/**
 *
 * @author Surya
 */
import com.moyosoft.connector.com.*;
import com.moyosoft.connector.exception.*;
import com.moyosoft.connector.ms.excel.*;
import java.io.File;
public class CreateExcel
{
    /* Constructor initializing the new Excel sheet */
    public CreateExcel(){
        try{
        /* Create the Excel application object */
            Excel oldExcel = new Excel();
            Excel newExcel = new Excel();
            File excel = new File("temp");
            try{
                /* Create a new Excel workbook */
                Workbook newWorkbook = newExcel.getWorkbooks().add();
                Workbook oldWorkbook = oldExcel.openWorkbook(new File("ICUsers.xlsx"));

                /* Get the active worksheet in the Excel workbook */
                Worksheet newWorksheet = newWorkbook.getActiveWorksheet();
                Worksheet oldWorksheet = oldWorkbook.getActiveWorksheet();
/*
                for(int i=0;i<=50;i++){
                    String temp1 = oldWorksheet.getCell(i,"B").getValue();
                    newWorksheet.getCell(i,"A").setValue(temp1);
                    String temp2 = oldWorksheet.getCell(i,"I").getValue();
                    newWorksheet.getCell(i,"B").setValue(temp2);
                }
                newWorksheet.getCell(1, "C").setValue("Company Name");
                newWorksheet.getCell(1, "D").setValue("Address");
                newWorksheet.getCell(1, "E").setValue("Phone Number  ");
 */
                /* Save the created Excel worksheet */
                newWorkbook.saveAs(excel,FileFormat.EXCEL7);
            }
            finally{
                /* Dispose the library */
                oldExcel.dispose();
                newExcel.dispose();
            }
        }
        catch (ComponentObjectModelException ex){
            System.out.println("COM error has occured: ");
            ex.printStackTrace();
        }
        catch (LibraryNotFoundException ex){
            System.out.println("The Java Excel Library hasn't been found.");
            ex.printStackTrace();
        }
    }

    /* function to fill excel sheet */
    public void fillExcel(String value[],int startingRow,String column)
    {
        int valueLength = value.length;
        try{
            Excel excel = new Excel();
            try{
                /* Create a new Excel workbook */
                Workbook workbook = excel.openWorkbook(new File("temp"));
                /* Get the active worksheet in the Excel workbook */
                Worksheet worksheet = workbook.getActiveWorksheet();
                
                for(int i=startingRow-1;i< startingRow+valueLength-1;i++){
                    worksheet.getCell(i, column).setValue(value[i-startingRow+1]);
                }
                workbook.save();
            }
            finally{
             /* Dispose the library */
                excel.dispose();
            }
        }
        catch(ComponentObjectModelException ex){
            System.out.println("COM error has occured: ");
            ex.printStackTrace();
        }
        catch (LibraryNotFoundException ex){
            System.out.println("The Java Excel Library hasn't been found.");
            ex.printStackTrace();
        }
    }
}

