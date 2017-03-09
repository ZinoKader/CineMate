package main.controllers;


/**
 * Helps us keep a common superclass between all controllers for easy switching between
 * screens and controllers.
 */
public interface ControlledScreen {

    public void setScreenParent(ScreenController screenController);

}
