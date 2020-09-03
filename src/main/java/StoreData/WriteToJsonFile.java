package StoreData;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WriteToJsonFile implements Runnable{
    private String path;
    private String context;

    public WriteToJsonFile(String path, String context) {
        this.path = path;
        this.context = context;
    }

    /**
     * Write object array into json file for future used
     * @throws IOException if writer is wrong
     **/
    @Override
    public void run() {
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(context);
            myWriter.close();
            System.out.println("Successfully wrote to the Json file!");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(1);
        }
    }

}
