package kz.altel;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by anatoliyvazhenin on 10/27/14.
 */
public class Tools{
    String getCurrentDate(String format){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);

        Date date = new Date();
        return df.format(date).toString();
    }

    String getDateMinus(String format, int minute){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat(format);

        // initiate Date variable
        Date date = new Date();

        // subtract certain amount of minutes
        date.setTime(date.getTime()-(minute*60*1000));

        return df.format(date).toString();
    }
}
