package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getDataDiferencaDias(Integer quantidadeDias) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, quantidadeDias);

        return getDataFormatado(cal.getTime());
    }
    public static String getDataFormatado(Date data) {
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return format.format(data);
    }
}
