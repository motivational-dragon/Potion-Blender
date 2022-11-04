package mod.motivationaldragon.potionblender.utils;

import java.security.InvalidParameterException;
import java.util.List;

public class ModUtils {

    ModUtils(){
        throw new IllegalStateException("Utility class");
    }

    /**
     * Calculate the average of a list of color in hexadecimal rgb form
     * @param colors the list of hexadecimal rgb color
     * @return the computed color blend in hexadecimal rgb form
     * @throws AssertionError if the list is empty
     */
    public static int calculateColorBlend(List<Integer> colors) {

        if (colors.isEmpty()){
            throw new InvalidParameterException("Colors list must not be empty");
        }

        int red = 0;
        int green = 0;
        int blue = 0;

        //average each color channel separately
        for (Integer color : colors) {
            red += (color & 0xFF0000) >> 16;
            green += (color & 0xFF00) >> 8;
            blue += (color & 0xFF);
        }
        //then write them back as an hexadecimal string
        return (0xff << 24) | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
    }
}
