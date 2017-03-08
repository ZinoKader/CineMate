package main.helpers;

public class FontHelper {

    public static String getWebFont(String fontFamily, int fontSize) {
        return "-fx-font-family:" + fontFamily + "; -fx-font-size:" + fontSize;
    }

}
