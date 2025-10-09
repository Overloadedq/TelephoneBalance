package org.billing.util;

import java.util.Set;
import java.util.regex.Pattern;

public final class PhoneNumber {
    private final String number;
    private static final Pattern PHONE_NUMBER_PATTERN =  Pattern.compile("^\\d{5}-\\d{2}-\\d{7}$");
    private static final String BELARUS_CODE="00375";
    private static final String OUR_CODE_OPERATOR="55";
    private static final Set<String> CODE_OPERATOR= Set.of("25","29","33","44");
    private PhoneNumber( String raw) {
        number = raw;
    }
    public static PhoneNumber of( String raw) {
        if(raw == null)
        {
            throw new NullPointerException("Phone number is null");
        }
        String cleanedRaw = raw.trim();
        if(!PHONE_NUMBER_PATTERN.matcher(cleanedRaw).matches())
        {
            throw new IllegalArgumentException("Invalid phone number: " + cleanedRaw);
        }
        return new PhoneNumber(cleanedRaw);
    }
    public String getCountryCode() {
        return number.substring(0,number.indexOf('-'));
    }
    public String getOperatorCode() {
        int firstDash = number.indexOf('-');
        int secondDash = number.indexOf('-',firstDash+1);
        return number.substring(firstDash+1,secondDash);
    }
    public String getNumber() {
        return number;
    }
    boolean isRoaming() {
        return !getCountryCode().equals(BELARUS_CODE);
    }
    public boolean isInNetwork() {
        return BELARUS_CODE.equals(getCountryCode()) && OUR_CODE_OPERATOR.equals(getOperatorCode());
    }

    public boolean isOtherMobile() {
        return BELARUS_CODE.equals(getCountryCode()) && CODE_OPERATOR.contains(getOperatorCode());
    }

    public boolean isFixedLine() {
        return BELARUS_CODE.equals(getCountryCode()) && !isInNetwork() && !isOtherMobile();
    }
}
