package com.myweb.mavenproject1.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Utilidad para cálculos de días hábiles (excluye fines de semana).
 * Compatible con Java 7+ y Hibernate estándar.
 */
public class DiasHabilesUtil {

    private DiasHabilesUtil() {} // Evita instanciación

    public static int calcularDiasHabiles(Date fechaInicio, Date fechaFin) {
        if (fechaInicio == null || fechaFin == null) return 0;
        
        Calendar ini = Calendar.getInstance();
        Calendar fin = Calendar.getInstance();
        ini.setTime(fechaInicio);
        fin.setTime(fechaFin);

        if (ini.after(fin)) return 0;

        int dias = 0;
        while (ini.before(fin) || ini.equals(fin)) {
            int diaSemana = ini.get(Calendar.DAY_OF_WEEK);
            if (diaSemana != Calendar.SATURDAY && diaSemana != Calendar.SUNDAY) {
                if (!esFestivo(ini.getTime())) dias++;
            }
            ini.add(Calendar.DATE, 1);
        }
        return dias;
    }

    public static Date sumarDiasHabiles(Date fechaInicio, int dias) {
        if (fechaInicio == null || dias <= 0) return fechaInicio;

        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        int sumados = 0;

        while (sumados < dias) {
            cal.add(Calendar.DATE, 1);
            int dia = cal.get(Calendar.DAY_OF_WEEK);
            if (dia != Calendar.SATURDAY && dia != Calendar.SUNDAY) {
                if (!esFestivo(cal.getTime())) sumados++;
            }
        }
        return cal.getTime();
    }

    /**
     * Festivos básicos (ajusta según tu país o usa tabla BD)
     */
    public static boolean esFestivo(Date fecha) {
        Calendar c = Calendar.getInstance();
        c.setTime(fecha);
        int mes = c.get(Calendar.MONTH);
        int dia = c.get(Calendar.DAY_OF_MONTH);

        // Ejemplo Colombia (agrega los que apliquen)
        return (mes == Calendar.JANUARY && dia == 1) ||
               (mes == Calendar.MAY && dia == 1) ||
               (mes == Calendar.JULY && dia == 20) ||
               (mes == Calendar.AUGUST && dia == 7) ||
               (mes == Calendar.DECEMBER && dia == 25);
    }
}