package StoreData;

import Model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WriteToFile implements Runnable{
    private String path;
    private List<User> users;

    public WriteToFile(String path, List<User> users) {
        this.path = path;
        this.users = users;
    }

    /**
     * Write object array into text file for future used
     * @throws IOException if writer is wrong
     **/
    @Override
    public void run() {
        try{
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            FileWriter myWriter = new FileWriter(path);
            myWriter.write("All courses updated at time: " + dtf.format(now) + "\n \n");
            for(int i=0; i<users.size(); i++){
                myWriter.write("Name: " + users.get(i).getUsername() + "\n");
                myWriter.write("Email: " + users.get(i).getEmail() + "\n");
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the text file!");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
