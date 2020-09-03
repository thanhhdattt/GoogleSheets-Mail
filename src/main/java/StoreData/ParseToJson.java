package StoreData;

import Model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public class ParseToJson {
    /**
     * Parse an object array into Json String
     * @return Object JsonString
     **/
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static String parse(List<User> users){
        String content = gson.toJson(users);
        System.out.println();
        System.out.println("All course: ");
        System.out.println(users.toString());
        return content;
    }
}
