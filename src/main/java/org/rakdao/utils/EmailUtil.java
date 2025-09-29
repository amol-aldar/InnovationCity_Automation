package org.rakdao.utils;


import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    private static final String HOST = "imap.gmail.com"; // or your mail server
    private static final String USERNAME = "your-test-email@gmail.com";
    private static final String PASSWORD = "your-app-password"; // Use App Password if Gmail/Outlook

    public static String getLatestOtp() {
        String otp = null;
        try {
            // Mail server properties
            Properties props = new Properties();
            props.put("mail.store.protocol", "imaps");

            // Connect to mailbox
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect(HOST, USERNAME, PASSWORD);

            // Open inbox
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // Get unread messages
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));

            if (messages.length == 0) {
                System.out.println("⚠️ No new OTP email found!");
                return null;
            }

            // Pick latest email
            Message message = messages[messages.length - 1];
            String content = message.getContent().toString();

            // Regex to extract OTP (assume 6 digits)
            Pattern otpPattern = Pattern.compile("\\b\\d{6}\\b");
            Matcher matcher = otpPattern.matcher(content);

            if (matcher.find()) {
                otp = matcher.group();
                System.out.println("✅ OTP extracted: " + otp);
            }

            // Mark as read
            message.setFlag(Flags.Flag.SEEN, true);

            inbox.close(false);
            store.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return otp;
    }
}

