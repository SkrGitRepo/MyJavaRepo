package com.app;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ApnsServiceBuilder;

public class PushNotifications {

    public static void main(String [] args) {
        System.out.println("Sending an iOS push notification...");

        String token = "";
        String type = "dev";
        String message = "Cisco IBPM notification";

        try {
            token = args[0];
        } catch (Exception e) {
            System.out.println("Usage: PushNotifications devicetoken [message] [type prod|dev]");
            System.out.println("example: PushNotifications 1testdevicetoken3eb414627e78991ac5a615b4a2a95454c6ba5d18930ac137 'hi there!' prod");
            return;
        }
        try {
            message = args[1];
        } catch (Exception e) {
            System.out.println("no message defined, using '"+message+"'");
        }
        try {
            type = args[2];
        } catch (Exception e) {
            System.out.println("no API type defined, using "+type);
        }

        System.out.println("The target token is "+token);

        ApnsServiceBuilder serviceBuilder = APNS.newService();

        if (type.equals("prod")) {
            System.out.println("using prod API");
            String certPath = PushNotifications.class.getResource("/CiscoJars/ibpmcertificate.p12").getPath();
            serviceBuilder.withCert(certPath, "cisco123")
                    .withProductionDestination();
        } else if (type.equals("dev")) {
            System.out.println("using dev API");
            String certPath = PushNotifications.class.getResource("/CiscoJars/ibpmcertificate.p12").getPath();
            serviceBuilder.withCert(certPath, "cisco123")
                    .withSandboxDestination();
        } else {
            System.out.println("unknown API type "+type);
            return;
        }

        ApnsService service = serviceBuilder.build();


        //Payload with custom fields
        
        String payload = APNS.newPayload()
                .alertBody(message)
                //.alertTitle("test alert title")
                .sound("default")
                .customField("custom", "custom value").build();

        ////Payload with custom fields
        //String payload = APNS.newPayload()
        //        .alertBody(message).build();

        ////String payload example:
        //String payload = "{\"aps\":{\"alert\":{\"title\":\"My Title 1\",\"body\":\"My message 1\",\"category\":\"Personal\"}}}";


        System.out.println("payload: "+payload);
        service.push(token, payload);

        System.out.println("The message has been hopefully sent...");
    }
}
