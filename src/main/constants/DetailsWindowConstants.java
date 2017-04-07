package main.constants;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;

public final class DetailsWindowConstants {

    private static final double BLUR_AMOUNT_NORMAL = 25;
    private static final double BLUR_AMOUNT_HIGH = 200;
    private static final int BLUR_FINENESS_NORMAL = 3;

    public static final Effect FROSTED_GLASS_EFFECT_NORMAL = new BoxBlur(BLUR_AMOUNT_NORMAL, BLUR_AMOUNT_NORMAL, BLUR_FINENESS_NORMAL);
    public static final Effect FROSTED_GLASS_EFFECT_HIGH = new BoxBlur(BLUR_AMOUNT_HIGH, BLUR_AMOUNT_HIGH, BLUR_FINENESS_NORMAL);

    //We override our constructor as private, to disallow creating instances of this class
    //This can also be acheived with an abstract class, but that's ill-fitting since no classes are going to extend this class
    private DetailsWindowConstants() {}

}
