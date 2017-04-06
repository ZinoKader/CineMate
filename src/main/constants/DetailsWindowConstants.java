package main.constants;

import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;

public final class DetailsWindowConstants {

    private static final double BLUR_AMOUNT = 25;
    private static final int BLUR_FINENESS = 3;
    public static final Effect FROSTED_GLASS_EFFECT = new BoxBlur(BLUR_AMOUNT, BLUR_AMOUNT, BLUR_FINENESS);

    //We override our constructor as private, to disallow creating instances of this class
    //This can also be acheived with an abstract class, but that's ill-fitting since no classes are going to extend this class
    private DetailsWindowConstants() {}

}
