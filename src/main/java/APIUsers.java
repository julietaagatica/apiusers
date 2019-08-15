import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import static spark.Spark.*;

public class APIUsers {
    public static void main(String[] args) {

        final IUserService service = new UserServiceMapImpl();
        service.setUsersTest();

        post("/token", (request,response) -> {
            response.type("application/json");

            String username = request.headers("username");
            String password = request.headers("password");

            if(username.equals("") || password.equals("")){
                response.status(403);
                return new Gson().toJson(new StandardResponse(StatusResponse.FORBIDEN, "Debe ingresar usuario y contraseña"));
            }

            User user = service.getUser(username);
            if(user == null){
                response.status(404);
                return new Gson().toJson(new StandardResponse(StatusResponse.NOT_FOUND, "El user "+username+" no existe"));
            } else if (!(user.getPassword().equals(password))){
                response.status(403);
                return new Gson().toJson(new StandardResponse(StatusResponse.FORBIDEN, "Password Incorrecto"));
            } else {
                Token token = user.getToken();
                if(token == null){
                    token = new Token(UUID.randomUUID().toString());
                    user.setToken(token);
                    service.updateUser(user.getUsername(),user);
                }
                response.status(202);
                return new Gson().toJson(new StandardResponse(StatusResponse.ACCEPTED, new Gson().toJsonTree(user.getToken())));
            }

        });


        get("/sites", (request, response) -> {
            response.type("application/json");

            String username = request.headers("username");
            String token = request.headers("token");

            if(username.equals("") || token.equals("")){
                response.status(403);
                return new Gson().toJson(new StandardResponse(StatusResponse.FORBIDEN, "Debe ingresar usuario y token"));
            }

            User user = service.getUser(username);
            if(user == null){
                response.status(404);
                return new Gson().toJson(new StandardResponse(StatusResponse.NOT_FOUND, "El user "+username+" no existe"));
            } else if (user.getToken() == null || !(user.getToken().getToken().equals(token))){
                response.status(403);
                return new Gson().toJson(new StandardResponse(StatusResponse.FORBIDEN, "Token Incorrecto"));
            } else {
                try {

                    URL url = new URL("http://localhost:8081/sites");
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

                    HttpURLConnection connection = (HttpURLConnection) urlConnection;
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    Gson gson = new Gson();
                    Site[] sites = gson.fromJson(in, Site[].class);

                    response.status(200);
                    return new Gson().toJson(new StandardResponse(StatusResponse.OK, new Gson().toJsonTree(sites)));

                }catch (IOException e){
                    System.out.println(e.getMessage());
                    response.status(500);
                    return new Gson().toJson(new StandardResponse(StatusResponse.INTERNAL_SERVER_ERROR, "Error al conectar a la api de sites"));
                }
            }

        });

        get("/sites/:site_id/categories", (request, response) -> {
            response.type("application/json");

            String username = request.headers("username");
            String token = request.headers("token");
            String site_id = request.params(":site_id");

            if(username.equals("") || token.equals("")){
                response.status(403);
                return new Gson().toJson(new StandardResponse(StatusResponse.FORBIDEN, "Debe ingresar usuario y token"));
            }

            User user = service.getUser(username);

            if(user == null){
                response.status(404);
                return new Gson().toJson(new StandardResponse(StatusResponse.NOT_FOUND, "El user "+username+" no existe"));
            } else if (user.getToken() == null || !(user.getToken().getToken().equals(token))){
                response.status(403);
                return new Gson().toJson(new StandardResponse(StatusResponse.FORBIDEN, "Token Incorrecto"));
            } else {
                try {
                    URL url = new URL("http://localhost:8081/sites/"+site_id+"/categories");
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0");

                    HttpURLConnection connection = (HttpURLConnection) urlConnection;
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    Gson gson = new Gson();
                    Category[] categories = gson.fromJson(in, Category[].class);

                    response.status(200);

                    return new Gson().toJson(new StandardResponse(StatusResponse.OK, new Gson().toJsonTree(categories)));
                }catch (IOException e){
                    System.out.println(e.getMessage());
                    response.status(500);
                    return new Gson().toJson(new StandardResponse(StatusResponse.INTERNAL_SERVER_ERROR, "Error al conectar a la api de sites"));
                }
            }

        });

    }

}
