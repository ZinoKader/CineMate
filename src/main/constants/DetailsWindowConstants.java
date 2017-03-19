package main.constants;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;

public class DetailsWindowConstants {

    private static final double BLUR_AMOUNT = 25;
    private static final int BLUR_FINENESS = 3;
    public static final Effect FROSTED_GLASS_EFFECT = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, BLUR_FINENESS);

}
