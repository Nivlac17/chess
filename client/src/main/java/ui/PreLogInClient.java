package ui;

import java.util.Arrays;

public class PreLogInClient {

    private ServerFacade server;
    private String serverUrl;

    public  PreLogInClient(String serverUrl){
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
//        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register","r" -> ServerFacade.register(params);


                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }



    public String help(){
     return   """
                        Available commands:
                         - login 
                         - register, r <Username> <Password> <Email>
                         - quit
                                
                """;

    }
}
