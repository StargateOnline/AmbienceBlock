package com.sekai.ambienceblock.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.function.Predicate;

public class ParsingUtil {
    public static int tryParseInt(String value, int defaultVal) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static int tryParseInt(String value) {
        return tryParseInt(value, 0);
    }

    public static float tryParseFloat(String value, float defaultVal) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static float tryParseFloat(String value) {
        return tryParseFloat(value, 0f);
    }

    public static double tryParseDouble(String value, double defaultVal) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    public static double tryParseDouble(String value) {
        return tryParseDouble(value, 0D);
    }

    public static boolean isNumericWithDecimal(String str)
    {
        if ( str == null )
        {
            return false;
        }
        int strSize = str.length();
        for (int i = 0; i < strSize; i++)
        {
            if (!Character.isDigit(str.charAt(i)) || str.charAt(i) != 46)
                return false;
        }
        return true;
    }

    public static int nthLastIndexOf(int nth, String ch, String string)
    {
        if (nth <= 0) return string.length();
        return nthLastIndexOf(--nth, ch, string.substring(0, string.lastIndexOf(ch)));
    }

    public static boolean isNumberOrDot(char c) {
        return (c >= '0' && c <= '9') || c == '.';
    }

    public static boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    public static int countChar(String s, char c) {
        int count = 0;
        int strSize = s.length();
        for (int i = 0; i < strSize; i++)
        {
            if(s.charAt(i) == c)
                count++;
        }
        return count;
    }

    public static Vec3d blockPosToVec3d(BlockPos pos) {
        return new Vec3d(pos.getX(), pos.getY(), pos.getZ());
    }

    //Predicate
    public static final Predicate<String> numberFilter = (stringIn) -> {
        if (!net.minecraft.util.StringUtils.isNullOrEmpty(stringIn)) {
            int strSize = stringIn.length();
            for (int i = 0; i < strSize; i++) {
                if (!ParsingUtil.isNumber(stringIn.charAt(i)))
                    return false;
            }

        }
        return true;
    };

    public static final Predicate<String> negativeNumberFilter = (stringIn) -> {
        if (net.minecraft.util.StringUtils.isNullOrEmpty(stringIn)) {
            return true;
        } else {
            int strSize = stringIn.length();
            for (int i = 0; i < strSize; i++)
            {
                if (ParsingUtil.isNumber(stringIn.charAt(i)) || (i == 0 && stringIn.charAt(i) == '-'))
                    continue;

                return false;
            }

            return true;
        }
    };

    public static final Predicate<String> decimalNumberFilter = (stringIn) -> {
        if (net.minecraft.util.StringUtils.isNullOrEmpty(stringIn)) {
            return true;
        } else {
            int strSize = stringIn.length();
            for (int i = 0; i < strSize; i++)
            {
                if (!ParsingUtil.isNumberOrDot(stringIn.charAt(i)))
                    return false;
            }

            if(ParsingUtil.countChar(stringIn, '.') > 1)
                return false;

            return true;
        }
    };

    public static final Predicate<String> negativeDecimalNumberFilter = (stringIn) -> {
        if (net.minecraft.util.StringUtils.isNullOrEmpty(stringIn)) {
            return true;
        } else {
            int strSize = stringIn.length();
            for (int i = 0; i < strSize; i++)
            {
                if (ParsingUtil.isNumberOrDot(stringIn.charAt(i)) || (i == 0 && stringIn.charAt(i) == '-'))
                    continue;

                return false;
            }

            if(ParsingUtil.countChar(stringIn, '.') > 1)
                return false;

            return true;
        }
    };
}
