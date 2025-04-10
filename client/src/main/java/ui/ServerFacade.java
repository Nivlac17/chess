package ui;


import model.UserData;

public class ServerFacade {
    private final String serverUrl;


    public ServerFacade(String url) {
        serverUrl = url;

    }

    public String register(String[] params) {
        String username = params[0];
        String password = params[1];
        String email    = params[2];
        UserData userData = new UserData(username,password,email);

//        System.out.println("This is it boys: " + ", " + username + ", " + password + ", " + email);

            var path = "/user";
            return this.makeRequest("POST", path, userData, UserData.class);
    }



//    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
//        try {
//            URL url = (new URI(serverUrl + path)).toURL();
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();
//            http.setRequestMethod(method);
//            http.setDoOutput(true);
//
//            writeBody(request, http);
//            http.connect();
//            throwIfNotSuccessful(http);
//            return readBody(http, responseClass);
//        } catch (ResponseException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }


}