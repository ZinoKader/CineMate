package main.controllers.contract;
import main.controllers.ScreenController;

/**
 * Helps us keep a common superclass between all controllers for easy switching between
 * contract and controllers.
 */
@SuppressWarnings("unused")
public interface ControlledScreen {

    void delayLogin();
    void setScreenParent(ScreenController screenController);

}
