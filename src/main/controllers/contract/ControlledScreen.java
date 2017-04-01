package main.controllers.contract;


import main.controllers.ScreenController;

/**
 * Helps us keep a common superclass between all controllers for easy switching between
 * contract and controllers.
 */
public interface ControlledScreen {

    public void setScreenParent(ScreenController screenController);

}
