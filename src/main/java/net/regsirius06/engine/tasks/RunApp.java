package net.regsirius06.engine.tasks;

import net.regsirius06.engine.gui.MainMenu;

/**
 * The entry point for launching the application.
 * This class contains the {@link #main(String[])} method, which serves as the starting point
 * for running the application.
 * <p>
 * Upon execution, the {@link #main(String[])} method opens the main menu of the application
 * by invoking the {@link MainMenu#open()} method.
 * </p>
 */
public class RunApp {

    /**
     * The main method that is executed when the application is launched.
     * It triggers the opening of the main menu of the application.
     *
     * @param args command-line arguments passed to the application (not used in this case)
     */
    public static void main(String[] args) {
        MainMenu.open();
    }
}