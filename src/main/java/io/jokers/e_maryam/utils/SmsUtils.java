package io.jokers.e_maryam.utils;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import static com.twilio.rest.api.v2010.account.Message.creator;
import static java.lang.System.*;

public class SmsUtils {

    public static final String FROM_NUMBER = "+16592225575";
    public static final String SID_KEY = "";
    public static final String TOKEN_KEY = "";

    public static void sendSMS(String to, String messageBody){
        Twilio.init(SID_KEY, TOKEN_KEY);
        // Création et envoi du message SMS
        Message message = creator(
                        new PhoneNumber("+223" + to),  // Numéro de téléphone du destinataire
                        new PhoneNumber(FROM_NUMBER),  // Numéro Twilio enregistré
                        messageBody)  // Contenu du SMS
                .create();

        // Affichage de l'ID du message pour vérification
        out.println("Message envoyé avec succès, SID : " + message.getSid());
    }


}
