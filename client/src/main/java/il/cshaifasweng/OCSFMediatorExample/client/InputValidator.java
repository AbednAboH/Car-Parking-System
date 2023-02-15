package il.cshaifasweng.OCSFMediatorExample.client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static final String PHONE_REGEX = "^(\\+972|05|0)[2-9]\\d{7,8}$";
    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private static final String PLATE_REGEX = "^(\\d{2}-\\d{3}-\\d{2}|\\d{3}-\\d{2}-\\d{3}|\\d{3}-\\d{3}|\\d{2}\\d{3}\\d{2}|\\d{3}\\d{2}\\d{3}|\\d{5}\\d{3})$";
    private static final String POSITIVE_REGEX = "^\\d+(\\.\\d+)?$";
    private static final String ID_REGEX = "^[0-9]{9}$";
    private static final String DateOfBirth_REGEX ="^(3[01]|[12][0-9]|0[1-9])/(1[0-2]|0[1-9])/[0-9]{4}$";
    private static final String TIME_REGEX ="^(2[0-3]|[01]?[0-9]):([0-5]?[0-9])$";
    private static final String CARD_REGEX= "/^4580+[0-9]{12}$|^4557+[0-9]{12}$/";
    private static final String NAME_REGEX="^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    private static final Pattern PLATE_PATTERN = Pattern.compile(PLATE_REGEX);
    private static final Pattern POSITIVE_PATTERN = Pattern.compile(POSITIVE_REGEX);
    private static final Pattern ID_PATTERN= Pattern.compile(ID_REGEX);
    private static final Pattern DateOfBirth_PATTERN = Pattern.compile(DateOfBirth_REGEX);
    private static final Pattern TIME_PATTERN = Pattern.compile(TIME_REGEX);
    private static final Pattern CARD_PATTERN = Pattern.compile(CARD_REGEX);
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
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

    public static boolean isValidPlateNumber(String plateNum) {
        Matcher matcher = PLATE_PATTERN.matcher(plateNum);
        return matcher.matches();
    }

    public static boolean isValidPositiveNumber(String PositiveNumber) {
        Matcher matcher = POSITIVE_PATTERN.matcher(PositiveNumber);
        return matcher.matches();
    }

    public static boolean isValidId(String id){
        Matcher matcher = ID_PATTERN.matcher(id);
        return matcher.matches();
    }
    public static boolean isValidDateOfBirth(String date){
        Matcher matcher = DateOfBirth_PATTERN.matcher(date);
        return matcher.matches();
    }
    public static boolean isValidTime(String time){
        Matcher matcher = TIME_PATTERN.matcher(time);
        return  matcher.matches();
    }
    public static boolean isValidCard(String card){
        Matcher matcher = CARD_PATTERN.matcher(card);
        return  matcher.matches();
    }
    public static boolean isValidName(String name){
        Matcher matcher = NAME_PATTERN.matcher(name);
        return  matcher.matches();
    }


    public static boolean checkAll(String email, String phone, String password, String plateNum, String PositiveNumber, String id ,String date, String time ,String card) {
        return isValidEmail(email) && isValidPhone(phone) && isValidPassword(password) && isValidPlateNumber(plateNum) && isValidCard(id) &&
                isValidTime(time) && isValidPositiveNumber(PositiveNumber) && isValidCard(card);
    }


    public static void main(String[] args) {
        String num = "4580123414571234";
        System.out.println(isValidCard(num));

    }
}