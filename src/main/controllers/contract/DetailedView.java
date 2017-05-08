package main.controllers.contract;

/**
 * Interface for basic implementation of a detailed (movie/person/series) controller
 */
@SuppressWarnings("unused")
public interface DetailedView {

    void delegateSetData();
    void setBaseDetails();

}
