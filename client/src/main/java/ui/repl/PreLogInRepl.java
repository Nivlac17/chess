package ui.repl;

import ui.client.PreLogInClient;
import ui.repl.PostLogInRepl;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PreLogInRepl {
    private final PreLogInClient client;
    private final String serverUrl;

    public PreLogInRepl(String serverUrl) {
        client = new PreLogInClient(serverUrl /*, this ----- notification handler*/);
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("Welcome to Calvin's Chess Client. Sign in or Register to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_WHITE + result);
                if (result.equals("Successful registration!!") || result.equals("Successful login!!")) {
                    PostLogInRepl postLoginRepl = new PostLogInRepl(this.serverUrl, PreLogInClient.getAuthToken());
                    postLoginRepl.run();
//                    break;
                }


            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

//    public void notify(Notification notification) {
//        System.out.println(RED + notification.message());
//        printPrompt();
//    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
