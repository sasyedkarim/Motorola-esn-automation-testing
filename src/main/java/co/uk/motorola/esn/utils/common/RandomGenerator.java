package co.uk.motorola.esn.utils.common;

import org.apache.commons.lang.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RandomGenerator {

    public RandomGenerator() {
        throw new UnsupportedOperationException();
    }

    /**
     * This will return a random Integer. The length is specified when the
     * method is called.
     */
    public static String randomInteger(Integer numberAmount) {
        return RandomStringUtils.randomNumeric(numberAmount);
    }

    /**
     * This will return a random String. The length is specified when the
     * method is called.
     */
    public String randomString(Integer characterAmount) {
        return RandomStringUtils.random(characterAmount, true, false);
    }

    /**
     * This will return random Alphanumeric characters. The length is
     * specified when the method is called.
     */
    public static String randomAlphanumeric(Integer alphaNumericAmount) {
        return RandomStringUtils.randomAlphanumeric(alphaNumericAmount);
    }

    /**
     * This will return random Alphanumeric characters. The length is
     * specified when the method is called.
     */
    public static String randomAlphabetic(Integer charactersCount) {
        return RandomStringUtils.randomAlphabetic(charactersCount);
    }

    /**
     * This will return random Email Address to '@example.com'. The length is
     * specified when the method is called.
     */
    public static String randomEmailAddress(Integer emailAddress) {
        String email = randomAlphanumeric(emailAddress) + "@example.com";
        return email.toLowerCase();
    }
    public String randomEmail(Integer characterCount)
    {
        return(RandomStringUtils.random(characterCount,true,false)+"@onetest.com");
    }

    private static final long ONE_YEAR_AS_MILLISECONDS = 365L * 24L * 60L * 60L * 1000L;
    private static final long TWENTY_FIVE_YEARS_AS_MILLISECONDS = 25 * ONE_YEAR_AS_MILLISECONDS;
    private static final long FIFTY_YEARS_AS_MILLISECONDS = 50 * ONE_YEAR_AS_MILLISECONDS;

    public static String randomDOB(String dateFormat) {
        long someTimeBetween25And50YearsInMilliSeconds = TWENTY_FIVE_YEARS_AS_MILLISECONDS
                + (long) (Math.random() * ((FIFTY_YEARS_AS_MILLISECONDS - TWENTY_FIVE_YEARS_AS_MILLISECONDS) + 1));

        Date d = new Date(System.currentTimeMillis() - someTimeBetween25And50YearsInMilliSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        return sdf.format(d);
    }

    public static String stringPlusCurrentDate(String prefix) {
        Date d = new Date(System.currentTimeMillis());
        return prefix + " " + d.toString();
    }

}