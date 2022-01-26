package com.aghs.mobilevaccination.service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.messaging.v1.service.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class TwilioMessagingService {
    private final String MESSAGING_SID;
    private String message;

    public TwilioMessagingService() {
        String TWILIO_SID = System.getenv("TWILIO_SID");
        String AUTH_TOKEN = System.getenv("AUTH_TOKEN");
        MESSAGING_SID = System.getenv("MESSAGING_SID");
        this.message = "Your Mobile Vaccination OTP for %sxxxxxx is %s";
        Twilio.init(TWILIO_SID, AUTH_TOKEN);
    }

    public String sendOtpMessage(String mobileNumber, String otp) {
        try {
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber(mobileNumber),
                    MESSAGING_SID,
                    String.format(this.message, mobileNumber.substring(0, 7), otp)
            ).create();
            System.out.println("OTP Status: " + message.getStatus() + "\nBody: " + message.getBody());
            System.out.println("OTP sent for +91" + mobileNumber);
        }
        catch (ApiException exception) {
            System.out.println("MessageError: " + exception.getMessage());
            return new String(exception.getMessage());
        }
        return null;
    }
}
