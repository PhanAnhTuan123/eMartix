package com.eMartix.authservice.util;

public class GenerateRandomOTP {
    public static String generateOTP(int length) {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random() * 10);
            otp.append(digit);
        }
        return otp.toString();
    }

    public static void main(String[] args) {
        System.out.println("Generated OTP: " + generateOTP(6));
    }
}
