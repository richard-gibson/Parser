package com.ninecy.util;


import java.util.Optional;

/**
 * User: Richard
 * Date: 21.03.15
 *
 */

public class ArgsUtil {


    public static Optional<String> getProperty(String property, String[] args) {
        if (args == null)
            return null;
        for (String arg: args) {
            String[] split = arg.split("=");
            if (split.length==2 && split[0].equals(property)){
                return Optional.of(split[1]);
            }
        }
        return Optional.empty();
    }

    public static String getMandatoryProperty(String property,String[] args) {
        return getProperty(property, args)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Mandatory Arg [%s] missing", property)));
    }


}
