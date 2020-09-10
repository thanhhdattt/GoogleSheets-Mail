package Services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.common.collect.ImmutableList;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.security.GeneralSecurityException;

import java.util.List;
import java.util.Properties;

public class SendEmail {
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/Gmail";
    private static String message;

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = ImmutableList.of(GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_COMPOSE, GmailScopes.GMAIL_SEND);
    private static final String CREDENTIALS_FILE_PATH = "/Gmail/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        /**Load client secrets.*/
        InputStream in = SendEmail.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        /**Build flow and trigger user authorization request.*/
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8080).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static void send(String email) throws IOException, GeneralSecurityException, MessagingException {
        /**Build a new authorized API client service.*/
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        /**Load initial parameters*/
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream input = loader.getResourceAsStream("config.properties");
        Properties p = new Properties();
        p.load(input);
        message = p.getProperty("message");
        message = "GOOGLE APIs TEST #1. \nCó con mèo be bé\n" +
                "Ngồi dưới gốc cây me\n" +
                "Này cái cậu cuteee..\n" +
                "Yêu bản thân đi nhé. :3 \n[THIS IS AN AUTOMATED MESSAGE - PLEASE DO NOT REPLY DIRECTLY TO THIS EMAIL].";

        String[] poems = new String[10];
        poems[0]="Có con mèo be bé\n" +
                "Ở dưới gốc cây me\n" +
                "Này cái cậu cutee\n" +
                "Làm người yêu tôi nhé.";
        poems[1] = "Cực bắc là Hà Giang\n" +
                "Cực nam là Cà Mau\n" +
                "Cực đáng iuu là cậu\n" +
                "Cực iuu cậu không phải tớ. :3";
        poems[2] = "Có một đống củi nhỏ\n" +
                "Cậu cũng đốt thành tro\n" +
                "Chỉ một tình yêu nhỏ\n" +
                "Cậu cũng chẳng dành cho.";
        poems[3] = "Có vài đốm lửa nhỏ\n" +
                "Bỗng bùng cháy thật to\n" +
                "Vẫn mấy câu hỏi nhỏ\n" +
                "When will I be yours?";
        poems[4] = "Tôi muốn tắt nắng đi\n" +
                "Cho màu đừng nhạt mất\n" +
                "Tôi muốn buộc cậu lại\n" +
                "Cho cậu đừng chạy điii";
        poems[5] = "Theo đuổi cậu và học\n" +
                "Thực ra rất giống nhau\n" +
                "Dù cho tớ cố gắng\n" +
                "Vẫn bỏ lại phía sau.";
        poems[6] = "Có một đàn chim nhỏ\n" +
                "Bay nhảy chẳng âu lo\n" +
                "Chỉ ước mình như nó\n" +
                "Thích cậu chẳng đắn đo";
        poems[7] = "Hoàng hôn thì màu tím\n" +
                "Nhưng tớ thích màu hồng\n" +
                "Tim tớ vẫn còn trống\n" +
                "Cậu muốn chui vào không?";
        poems[8] = "Có con chim nho nhỏ\n" +
                "Đậu trên bãi cỏ xanh\n" +
                "Có tình yêu nho nhỏ\n" +
                "Không dành tặng cho anh";
        poems[9] = "Bên tớ đầy nắng gió\n" +
                "Cậu nơi đó sống sao\n" +
                "Tớ thì đang lao đao\n" +
                "Vì nôn nao nhớ cậu :3";
        int poemIndex = getRandomNumber(0, 9);
        /**Create MimeMessage to send*/
        MimeMessage msg = createEmail(email, "me", "GOOGLE APIs TEST #1", "Test #1\n\n".concat(poems[poemIndex].concat("\n\n[THIS IS AN AUTOMATED MESSAGE - PLEASE DO NOT REPLY DIRECTLY TO THIS EMAIL].\n")));
        sendMessage(service, "me", msg);
    }

    /**
     * Create a MimeMessage using the parameters provided.
     *
     * @param to email address of the receiver
     * @param from email address of the sender, the mailbox account
     * @param subject subject of the email
     * @param bodyText body text of the email
     * @return the MimeMessage to be used to send email
     * @throws MessagingException
     */
    public static MimeMessage createEmail(String to,
                                          String from,
                                          String subject,
                                          String bodyText)
            throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    /**
     * Create a message from an email.
     *
     * @param emailContent Email to be set to raw of message
     * @return a message containing a base64url encoded email
     * @throws IOException
     * @throws MessagingException
     */
    public static Message createMessageWithEmail(MimeMessage emailContent)
            throws MessagingException, IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    /**
     * Send an email from the user's mailbox to its recipient.
     *
     * @param service Authorized Gmail API instance.
     * @param userId User's email address. The special value "me"
     * can be used to indicate the authenticated user.
     * @param emailContent Email to be sent.
     * @return The sent message
     * @throws MessagingException
     * @throws IOException
     */
    public static Message sendMessage(Gmail service,
                                      String userId,
                                      MimeMessage emailContent)
            throws MessagingException, IOException {
        Message message = createMessageWithEmail(emailContent);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
        return message;
    }
}
