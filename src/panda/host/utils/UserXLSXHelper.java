package panda.host.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import panda.host.model.models.Post;
import panda.host.model.models.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class UserXLSXHelper {
    public static ArrayList<User> getUserList(String pathToExcelFile, int permissions) {
        ArrayList<User> users = new ArrayList<>();
        try {
            FileInputStream file = new FileInputStream(new File(pathToExcelFile));
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            // Indicating the number of rows in the Excel file
            int nbRows = sheet.getPhysicalNumberOfRows();
            System.out.println(String.format("[UserXLSXHelper, main()] | The Excel file returned %d row(s).", nbRows));

            // Iterating through each rows of the Excel file, one by one
            for (Row row : sheet) {
                // Now for each row, iterating through all the columns
                Iterator<Cell> cellIterator = row.cellIterator();
                int activeColumnIndex = 0;
                int activeRowIndex = 0;

                while (cellIterator.hasNext()) {
                    // Considering that the first row doesn't contain valuable data
                    Cell cell = cellIterator.next();

                    if (cell.getRowIndex() != 0) {

                        // According to the format [USERNAME | PASSWORD]
                        if (activeColumnIndex == 0) {
                            String username = cell.getStringCellValue();
                            System.out.print("username: " + username + "\t");

                            users.add(new User(username, permissions));

                            // Same row, column index 1
                            activeRowIndex = cell.getRowIndex();
                            activeColumnIndex = 1;

                        } else {
                            String password = cell.getStringCellValue();
                            System.out.print("password: " + password + "\t");

                            // Since the current user was created above in the list, with his username
                            users.get(activeRowIndex - 1).setPassword(password);

                            // Next row, column index 0
                            activeColumnIndex = 0;
                        }
                    }
                }
                System.out.println("|");
            }
            file.close();
            return users;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
