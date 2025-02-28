package http.server;

import exception.ManagerSaveException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {

    String apiToken;
    String serverURL;

    public KVTaskClient(String serverURL) {

        this.serverURL = serverURL;
        try {
            urlWay1(this.serverURL);            // получаем токен
        } catch (InterruptedException | IOException e) {

            throw new ManagerSaveException("Can't do save request", e);
        }
    }


    private void urlWay1(String path) throws IOException, InterruptedException { // делает запрос на сервер
                                                                                // о регистрации и получения токена
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(path + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        apiToken = response.body();
    }

    public void put(String key, String json) { // должен сохранять состояние менеджера задач через запрос

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(this.serverURL + "/save/" + key + "?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers
                    .ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                System.out.println("Не удалось сохранить данные");
            }
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Can't do save request", e);
        }
    }

    public String load(String key) {

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(this.serverURL + "/load/" + key + "?API_TOKEN=" + apiToken))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers
                    .ofString(StandardCharsets.UTF_8));
            if (response.statusCode() != 200) {
                System.out.println("Не удалось получить данные.");
            }
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new ManagerSaveException("Can't do load request", e);
        }
    }
}














