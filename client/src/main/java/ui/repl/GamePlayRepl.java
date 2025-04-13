package ui.repl;

import ui.client.GamePlayClient;
import ui.client.PreLogInClient;

import java.util.Scanner;

import static ui.EscapeSequences.*;
import static ui.client.PreLogInClient.authToken;

public class GamePlayRepl {

    private final GamePlayClient client;
    private final String serverUrl;

    public GamePlayRepl(String serverUrl) {
        client = new GamePlayClient(serverUrl /*, this ----- notification handler*/);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println(SET_TEXT_COLOR_MAGENTA + "Welcome to Game Play");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line, authToken);
                System.out.print(SET_TEXT_COLOR_MAGENTA + result);
                if (result.equals("finished gameplay 09k")) {
                    break;
                }


            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }



    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
