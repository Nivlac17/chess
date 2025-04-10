package ui;

import java.io.PrintStream;

import static ui.EscapeSequences.*;

public class ServerFacade {
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;

    }

    public static String register(String[] params) {
        String username = params[0];
        String password = params[1];
        String email    = params[2];

        System.out.println("This is it boys: " + ", " + username + ", " + password + ", " + email);


        return null;
    }
}