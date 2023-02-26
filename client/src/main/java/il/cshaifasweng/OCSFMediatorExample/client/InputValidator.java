package il.cshaifasweng.OCSFMediatorExample.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static final String PHONE_REGEX = "^(\\+972|05|0)[2-9]\\d{7,8}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String eightCaharachtersOneLetter = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";

    private static final String PLATE_REGEX = "^(\\d{2}-\\d{3}-\\d{2}|\\d{3}-\\d{2}-\\d{3}|\\d{3}-\\d{3}|\\d{2}\\d{3}\\d{2}|\\d{3}\\d{2}\\d{3}|\\d{5}\\d{3}|\\d{9})$";
    private static final String NUMBERS_REGEX = "^\\d+$";
    private static final String NAME_REGEX = "^[a-z ,.'-]+$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern PASSWORD_PATTERN_eightCaharachtersOneLetter = Pattern.compile(eightCaharachtersOneLetter);
    private static final Pattern PLATE_PATTERN = Pattern.compile(PLATE_REGEX);
    private static final Pattern NUMBER_PATTERN = Pattern.compile(NUMBERS_REGEX);
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);
    public static boolean isValidPassRegular(String password) {
        Matcher matcher = PASSWORD_PATTERN_eightCaharachtersOneLetter.matcher(password);
        return matcher.matches();
    }
    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
    public static boolean isValidName(String name) {
            Matcher matcher = NAME_PATTERN.matcher(name);
            return matcher.matches();
        }

    public static boolean isValidPhone(String phone) {
        Matcher matcher = PHONE_PATTERN.matcher(phone);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.matches();
    }
    public static boolean isValidPlateNumber(String plateNum){
        Matcher matcher = PLATE_PATTERN.matcher(plateNum);
        return matcher.matches();
    }
     public static boolean isValidNumber(String plateNum){
            Matcher matcher = NUMBER_PATTERN.matcher(plateNum);
            return matcher.matches();
        }

    public static boolean checkAll(String email, String phone, String password,String plateNum){
        return isValidEmail(email) && isValidPhone(phone) && isValidPassword(password) && isValidPlateNumber(plateNum);
    }

}
