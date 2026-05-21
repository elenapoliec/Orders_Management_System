package Sstart;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Clasa folosita pentru operatii de introspectie si analiza dinamica.
 * Ofera metode helper bazate pe Java Reflection API pentru inspectarea structurii obiectelor.
 * @author Poliec Elena
 * @version 1.0
 */

public class Reflection {

    /**
     * Extrage si afiseaza in consola standard (System.out) toate campurile
     * unui obiect primit ca parametru, impreuna cu valorile lor actuale.
     * Metoda forteaza accesul la campurile private prin setAccessible(true) si utilizeaza
     * Java Streams pentru procesarea fluida a metadatelor.
     * @param object obiectul care urmeaza sa fie inspectat dinamic prin reflexie.
     */
    public static void retrieveProperties(Object object) {
        if (object == null) return;

        Arrays.stream(object.getClass().getDeclaredFields())
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        Object value = field.get(object);
                        System.out.println(field.getName() + "=" + value);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                });
    }

}
