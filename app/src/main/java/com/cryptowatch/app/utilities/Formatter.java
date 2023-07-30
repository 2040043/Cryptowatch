package com.cryptowatch.app.utilities;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Formatter {
    public static String formatDate(long timestamp) {
        Timestamp stamp = new Timestamp(timestamp * 1000);
        Date date = new Date(stamp.getTime());
        DateFormat format = new SimpleDateFormat("dd MMM YYYY, HH:mm");
        return format.format(date);
    }

    public static String formatPrice(float price) {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        return String.format("%s %s", Constants.CONVERSION_SYMBOL, df.format(price));
    }
}
