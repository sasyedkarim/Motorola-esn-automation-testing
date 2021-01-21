package co.uk.motorola.esn.utils;

import co.uk.motorola.esn.context.ScenarioContext;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static java.util.Calendar.getInstance;


public class CommonUtil {

    private static final String PARSE_EXCEPTION = "Parse Exception:{}";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Retrieve Integration Header Field for Excel Reporting from Integration.Properties
     *
     * @param integrationName which is received from feature file
     * @return List of String from the property file.
     */
    public List<String> getIntegrationProperties(String integrationName, ScenarioContext scenarioContext) {
        Properties prop = new Properties();
        List<String> propertyList = new ArrayList<>();

        try (InputStream input = new FileInputStream("src/test/resources/Integration/Integration.properties")) {
            prop.load(input);
            String integrationFieldHeader = prop.getProperty(integrationName);
            String[] arrIntegrationFieldHeader = integrationFieldHeader.split("##");
            propertyList = Arrays.asList(arrIntegrationFieldHeader);
        } catch (IOException ex) {
            scenarioContext.getLOG().info(ex.toString());
        }
        return propertyList;
    }

    /**
     * Convert the Unformatted Date into Julian Date
     *
     * @param unformattedDate which is date in unformatted
     * @param format          which is date format for unformatted date.
     * @return String.
     */
    public String convertToJulian(String unformattedDate, String format, ScenarioContext scenarioContext) {
        SimpleDateFormat fromUser = new SimpleDateFormat(format);
        SimpleDateFormat calDateFormat = new SimpleDateFormat("yyyyMMdd");
        String dayS = "";
        String monthS = "";
        String yearS = "";
        String formattedDate = "";
        try {
            formattedDate = calDateFormat.format(fromUser.parse(unformattedDate));
        } catch (ParseException e) {
            scenarioContext.getLOG().info(PARSE_EXCEPTION, e.getMessage());
        }
        dayS = formattedDate.substring(6, 8);
        monthS = formattedDate.substring(4, 6);
        yearS = formattedDate.substring(0, 4);
        String julianDate = "";

        if (formattedDate.length() > 0) {
            /*Days of month*/
            int[] monthValues = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
            /*Convert to Integer*/
            int day = Integer.parseInt(dayS);
            int month = Integer.parseInt(monthS);
            int year = Integer.parseInt(yearS);

            //Leap year check
            if (year % 4 == 0) {
                monthValues[1] = 29;
            }
            //Start building Julian date
            //last two digit of year: 2012 ==> 12
            julianDate += yearS.substring(2, 4);

            int julianDays = 0;
            for (int i = 0; i < month - 1; i++) {
                julianDays += monthValues[i];
            }
            julianDays += day;

            if (String.valueOf(julianDays).length() < 2) {
                julianDate += "00";
            }
            if (String.valueOf(julianDays).length() < 3) {
                julianDate += "0";
            }
            julianDate += String.valueOf(julianDays);
        }
        return julianDate;
    }

    /**
     * Convert the Unformatted Date into mentioned format
     *
     * @param unformattedDate which is unformatted date
     * @param iFormat         which is format of user given date
     * @param oFormat         which is format of converted date
     * @return String.
     */
    public String convertToDateFormat(String unformattedDate, String iFormat, String oFormat, String timeStampFlag) {
        String formattedDate;
        LocalDate date;
        LocalDateTime dateTime;

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(iFormat, Locale.ENGLISH);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(oFormat, Locale.ENGLISH);

        if (!unformattedDate.trim().equals("")) {
            if (timeStampFlag.equalsIgnoreCase("y")) {
                dateTime = LocalDateTime.parse(unformattedDate, inputFormatter);
                formattedDate = outputFormatter.format(dateTime);
            } else {
                date = LocalDate.parse(unformattedDate, inputFormatter);
                formattedDate = outputFormatter.format(date);
            }
        } else {
            formattedDate = "";
        }
        return formattedDate;
    }

    public static String[] splitToNChar(String text, int size) {
        List<String> parts = new ArrayList<>();
        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }

    public int calculateDays(String fromDate, String toDate, ScenarioContext scenarioContext) {
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        int diffDays = 0;
        try {
            Date dateFrom = df.parse(fromDate);
            Date dateTo = df.parse(toDate);
            long diff = dateTo.getTime() - dateFrom.getTime();
            diffDays = (int) (diff / (1000 * 60 * 60 * 24));
            scenarioContext.getLOG().info("Calculated Days------------{}", diffDays);

        } catch (ParseException e) {
            scenarioContext.getLOG().info(PARSE_EXCEPTION, e.getMessage());
        }
        return diffDays;
    }

    /*
     * Method is to validate whether give year is leap year or not.
     * @param timestamp : policy begin date and end date
     * @return boolean
     */
    public boolean validateIsLeapYear(String timestamp) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy");
        Date date = fmt.parse(timestamp);
        GregorianCalendar cal = (GregorianCalendar) getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        return cal.isLeapYear(year);
    }


    /**
     * Merge the delimited (~~) address into single string
     *
     * @param separateAddress which is string
     * @return String.
     */
    public String mergeString(String separateAddress, ScenarioContext scenarioContext) {
        String[] temp = separateAddress.split("~~");
        String mergedString = scenarioContext.getPropertyTransfer().get(temp[0]);
        for (int i = 1; i < temp.length; i++) {

            String tempVal = scenarioContext.getPropertyTransfer().get(temp[i]);
            if (!(tempVal.trim().isEmpty())) {
                mergedString = mergedString.concat(" ").concat(tempVal);
            }
        }
        return mergedString;
    }


    /**
     * Convert the Julian Date to Calendar Date in dd/MM/yyyy
     *
     * @param input which is date in Julian
     * @return String.
     */
    public String convertJulianToCalendar(String input, String outputFormat, ScenarioContext scenarioContext) {
        input = "20" + input;
        DateFormat fmt1 = new SimpleDateFormat("yyyyDDD");
        Date date = null;
        try {
            date = fmt1.parse(input);
        } catch (ParseException e) {
            scenarioContext.getLOG().info(PARSE_EXCEPTION, e.getMessage());
        }
        DateFormat fmt2 = new SimpleDateFormat(outputFormat);
        return fmt2.format(date);
    }

    /**
     * validating whether particular fields are present or not
     *
     * @param fieldAndValue which has field and value
     * @return value to be filled in the field.
     */
    public String valueToBeFilled(String[] fieldAndValue) {
        if (fieldAndValue.length > 2) {
            for (int i = 2; i < fieldAndValue.length; i++) {
                fieldAndValue[1] = fieldAndValue[1] + ":" + fieldAndValue[i];
            }
        }
        return fieldAndValue[1];
    }

    /**
     * Get Count of Key from the List of HashMap of HashMap
     *
     * @param map which is list of Hash map of Hash map.
     * @param key which is used identify from list of Hash map of Hash map.
     * @return integer which gives the count of key from the list of Hash map of Hash map.
     */
    public int getKeyCount(List<Map<String, Map<String, String>>> map, String key) {
        int count = 0;

        for (int i = 0; i < map.size(); i++) {
            if (map.get(i).containsKey(key)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get Count of Key from the HashMap of List
     *
     * @param map which is Hash map of List.
     * @param key which is used identify from Hash map of List.
     * @return integer which gives the count of key from the Hash map of List.
     */
    public int getKeyCount(Map<String, List<String>> map, String key) {
        int count = 0;

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String k = entry.getKey();
            if (k.contains(key)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get Count of Key from the List of HashMap
     *
     * @param map which is Hash map of Hash map.
     * @param key which is used identify from Hash map of Hash map.
     * @return integer which gives the count of key from the Hash map of Hash map.
     */
    public int getMapKeyCount(Map<String, Map<String, String>> map, String key) {
        int count = 0;

        for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
            String k = entry.getKey();
            if (k.contains(key)) {
                count++;
            }
        }

        return count;
    }

    /**
     * Get the Key identifier for each transaction record
     *
     * @param mapTransaction which is Hash map to get the key
     * @param strIdentifier  which is to identifier from the feature file to manage the key
     * @return String key for transaction record
     */
    public String getIdentifier(Map<String, String> mapTransaction, String strIdentifier) {
        StringBuilder returnID = new StringBuilder();
        String[] arrValues = strIdentifier.split("##");
        for (String arr : arrValues) {
            returnID.append(mapTransaction.get(arr).trim());
        }
        return returnID.toString();
    }

    /**
     * Get the Age based on years
     *
     * @param strDate    date for which age need to calculated
     * @param dateFormat date format of Parameter
     * @return String of Age.
     */
    public String getAge2(String strDate, String dateFormat, ScenarioContext scenarioContext) {
        String strAge = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            GregorianCalendar cal = new GregorianCalendar();
            int intYear;
            int intMonth;
            int intNoOfYears;
            intYear = cal.get(Calendar.YEAR);// current year ,
            intMonth = cal.get(Calendar.MONTH);// current month
            cal.setTime(sdf.parse(strDate));
            intNoOfYears = intYear - cal.get(Calendar.YEAR);
            if ((intMonth < cal.get(Calendar.MONTH))) {
                intNoOfYears--;
            }
            strAge = String.format("%03d", intNoOfYears);
        } catch (ParseException e) {
            scenarioContext.getLOG().info(PARSE_EXCEPTION, e.getMessage());
        }
        return strAge;
    }

    /**
     * Get the Age based on years
     *
     * @param strDate    date for which age need to calculated
     * @param dateFormat date format of Parameter
     * @return String of Age.
     */
    public String getAge(String strDate, String dateFormat, ScenarioContext scenarioContext) {

        String strAge = "";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        int age = 0;
        try {
            Date dateDOB = sdf.parse(strDate);
            Calendar now = getInstance();
            Calendar dob = getInstance();
            dob.setTime(dateDOB);
            if (dob.after(now)) {
                throw new IllegalArgumentException("Can't be born in the future");
            }
            int currYear = now.get(Calendar.YEAR);
            int dobYear = dob.get(Calendar.YEAR);
            age = currYear - dobYear;
            int currMonth = now.get(Calendar.MONTH);
            int dobMonth = dob.get(Calendar.MONTH);
            if (dobMonth > currMonth) {
                age--;
            } else if (currMonth == dobMonth) {
                int currDay = now.get(Calendar.DAY_OF_MONTH);
                int dobDay = dob.get(Calendar.DAY_OF_MONTH);
                if (dobDay > currDay) {
                    age--;
                }
            }
            strAge = String.format("%03d", age);
        } catch (ParseException e) {
            scenarioContext.getLOG().info(PARSE_EXCEPTION, e.getMessage());
        }
        return strAge;
    }

}
