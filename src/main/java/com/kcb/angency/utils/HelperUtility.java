package com.kcb.angency.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.codec.binary.Base64;

public class HelperUtility {
    public static Logger LOGGER = LogManager.getLogger(HelperUtility.class);

    public static String getUniqueTransactionNumber() {
        RandomStringGenerator randomStringGenerator = new RandomStringGenerator.Builder().withinRange('0', 'z')
                .filteredBy(CharacterPredicates.LETTERS, CharacterPredicates.DIGITS).build();
        String transactioNumber = randomStringGenerator.generate(12).toLowerCase();
        // log here
        LOGGER.info("======================================[" + transactioNumber
                + "]======================================");
        return transactioNumber;

    }

    public static String getTransactionTimestamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return simpleDateFormat.format(new Date());
    }

    public static String encodeUserCredentials() {
        String username = "AgencyAdministrator01";
        String Password = "V5*90[#T0?Admin@7ibY3l!R";
        String pair = username + ":" + Password;

        final byte encodedPair [] = Base64.encodeBase64(pair.getBytes());

        return new String(encodedPair);
    }

}
