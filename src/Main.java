import adapters.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import http.KVServer;
import task.*;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        // Спринт 8
        new KVServer().start();
    }
}












