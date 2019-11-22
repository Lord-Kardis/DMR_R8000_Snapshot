package R8000_DMR;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvFileWriter {

    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private String FILE_HEADER;

    public static void writeCsvFile(String fileName, String data, String header) {

        FileWriter fileWriter = null;
        //BufferedWriter bw = null;

        try {
            boolean newFile = false;

            File file = new File(fileName);


            if (!file.exists()){
                file.createNewFile();
                newFile = true;
            }

            fileWriter = new FileWriter(file.getAbsoluteFile(), true);

            //Write file header
            if (newFile) {
                fileWriter.append(header + NEW_LINE_SEPARATOR);
            }
          fileWriter.append(data);
          fileWriter.append(NEW_LINE_SEPARATOR);
            //bw = new BufferedWriter(fileWriter);
            //bw.write(data + NEW_LINE_SEPARATOR);



        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            try {
                if (fileWriter != null) {
                    fileWriter.flush();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }


        }
    }
}
