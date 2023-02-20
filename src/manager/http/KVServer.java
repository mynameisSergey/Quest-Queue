package manager.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
    public static final int PORT = 8078;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {

        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", this::register);
        server.createContext("/save", this::save);
        server.createContext("/load", this::load);
        apiToken = generateApiToken();
    }



        private void load(HttpExchange httpExchange) {
            // TODO Добавьте получение значения по ключу
            try {
                System.out.println("\n/load");
                if (!hasAuth(httpExchange)) {
                    System.out.println("Запрос не авторизован, нужен параметр в query API_TOKEN со значением API-ключа");
                    httpExchange.sendResponseHeaders(403, 0);
                    return;
                }
                if ("GET".equals(httpExchange.getRequestMethod())) {
                    String key = httpExchange.getRequestURI().getPath().substring("/load/".length());
                    if (key.isEmpty()) {
                        System.out.println("Key для сохранения пустой. Key указывается в пути: /load/{key}");
                        httpExchange.sendResponseHeaders(400, 0);
                        return;
                    }
                    if (data.get(key) == null) {
                        System.out.println("Не могу достать данные для ключа '" + key + "', данные отсутствуют");
                        httpExchange.sendResponseHeaders(404, 0);
                        return;
                    }
                    String response = data.get(key);
                    httpExchange.sendResponseHeaders(200, 0);
                    sendText(httpExchange, response);
                    System.out.println("Значение для ключа " + key + " успешно отправлено в ответ на запрос!");

                } else {
                    System.out.println("/load ждет GET-запрос, а получил: " + httpExchange.getRequestMethod());
                    httpExchange.sendResponseHeaders(405, 0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpExchange.close();
            }
        }

    private void save(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(h)) {
                System.out.println("Запрос не авторизован, нужен параметр в query API_TOKEN со значением API ключа");
                h.sendResponseHeaders(403, 0);
                return;
            }
            if ("POST".equals(h.getRequestMethod())) {
                var key = h.getRequestURI().getPath().substring("/save/".length());
                if (key.isEmpty()) {
                    System.out.println("Ключ(key) для сохранения пустой. Ключ(key) указывается в пути: /save/{key}");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                var value = readText(h);
                if (value.isEmpty()) {
                    System.out.println("Значение(value) для сохранения пустое. Значение(value) указывается в теле запроса");
                    h.sendResponseHeaders(400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                h.sendResponseHeaders(200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    private void register(HttpExchange h) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(h.getRequestMethod())) {
                sendText(h, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                h.sendResponseHeaders(405, 0);
            }
        } finally {
            h.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("На " + PORT + " порту сервер остановлен!");
    }

    private String generateApiToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange httpExchange) {
        String rawQuery = httpExchange.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(200, resp.length);
        httpExchange.getResponseBody().write(resp);
    }
}
