import Model.User;
import Services.SendEmail;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.google.common.collect.ImmutableList;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tool for automatically send email to user's response when they finished the survey.
 * @Created by thanhhdattt on 3.9.2020.
 * @Updated by thanhhdattt on 3.9.2020.
 */

public class GSheetsAPI {
    /**
     * Initialize global instances and status
     */
    private static final String APPLICATION_NAME = "Google Sheets API TEST #1";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/Sheets";
    private static String SPREADSHEET_ID = "";
    private static NetHttpTransport HTTP_TRANSPORT;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = ImmutableList.of(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/Sheets/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    public synchronized static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GSheetsAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        /**
         * Build flow and trigger user authorization request.
         */
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**Get sheets ID from the link*/
    public static String getSheetsID(String path){
        String out = path.substring(39, 39+44);
        return out;
    }

    /**
     * Prints the names and email info of users in a sample spreadsheet:
     * https://docs.google.com/spreadsheets/d/1KIp6iOqBZEvY3A-q5oBzgLVim1Pb-VCYpgFdD2O39iE/
     */
    public static void main(String... args) throws IOException, InterruptedException {

        /**Load initial parameters*/
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("config.properties");
        Properties p = new Properties();
        p.load(input);
        String path = p.getProperty("path");
        String range = p.getProperty("range");
        Long sleepTime = Long.parseLong(p.getProperty("sleepTime"));
        SPREADSHEET_ID = getSheetsID(path);
        Set<User> users = new HashSet<>();
        Set<String> mails = new HashSet<>();
        Set<String> temp = new HashSet<>();
        while(true){
            /**Build a new authorized API client service.*/
            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
            ValueRange response = service.spreadsheets().values()
                    .get(SPREADSHEET_ID, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            String regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
            Pattern pattern = Pattern.compile(regex);
            final int[] meowIndex = new int[1];
            final int[] index = { 0 };
            final Matcher[] matcher = new Matcher[1];
            values.get(0).forEach((tmp) -> {
                matcher[0] = pattern.matcher(tmp.toString());
                if(matcher[0].matches()){
                    meowIndex[0] = index[0];
                }
                index[0]++;
            });
            /**Get values from sheet and parse into list objects*/
            values.forEach(row -> {
                if(row.isEmpty()) return;
                User user = new User();
                user.setUsername(row.get(1).toString());
                user.setEmail(row.get(5).toString());
                if(!users.contains(user)) users.add(user);
            });
            users.forEach(user -> {
//                System.out.println(user.toString());
                mails.add(user.getEmail());
            });
            System.out.println("\n");
            /**Get list of meow meow to send message*/
            mails.forEach(mail -> {
                System.out.println(mail);
                if(!temp.contains(mail)) {
                    try {
                        SendEmail.send(mail);
                        temp.add(mail);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println(mails + "\n" + temp);
            /**Sleep to prevent over 100 requests per second and be banned for 10 minutes*/
            Thread.sleep(sleepTime);
        }
    }
}
/**
 * message=N\u01A1i t\u1EDB nhi\u1EC1u n\u1EAFng gi\u00F3 \nC\u1EADu b\u00EAn \u0111\u00F3 s\u1ED1ng sao \nT\u1EDB th\u00EC \u0111ang lao \u0111ao \nV\u00EC n\u00F4n nao nh\u1EDB c\u1EADu
 * */
