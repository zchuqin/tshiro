package stoner.tshiro.common;

import stoner.tshiro.security.KickoutSessionFilter;

public class CommonConstants {
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];

    public static final String KICKOUT_KEY = KickoutSessionFilter.class.getCanonicalName() + "_KICKOUT_KEY";

    public static final String TRUE = String.valueOf(true);


}
