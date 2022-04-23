package red.fizz.roseapi;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import red.fizz.roseapi.database.MySQLConnection;
import red.fizz.roseapi.database.SQLiteConnection;
import spark.Service;

import java.util.List;
import java.util.UUID;

import static spark.Service.ignite;

class SparkWrapper {
    Service http;

    public boolean validateKey(String key, boolean usingMySQL) {
        if (usingMySQL) {
            return MySQLConnection.validateKey(key);
        }
        else {
            return SQLiteConnection.validateKey(key);
        }
    }

    void create(int port, List<String> placeholders, boolean usingMySQL) {

        http = ignite().port(port);
        http.get("/:uuid/:placeholder", (request, response) -> {
            if (request.headers("token") == null) {
                response.type("application/json");
                response.status(401);
                return "{\"status\":\"401\",\"message\":\"Unauthorized. Please obtain an api key by running /api ingame.\"}";
            } else if (!validateKey(request.headers("token"), usingMySQL)) {
                response.type("application/json");
                response.status(401);
                return "{\"status\":\"401\",\"message\":\"Unauthorized. You are using an invalid API key.\"}";
            } else {
                response.type("application/json");
                UUID specifiedUUID;
                try {
                    specifiedUUID = UUID.fromString(request.params(":uuid"));
                }
                catch(Exception e) {
                    response.type("application/json");
                    response.status(400);
                    return "{\"status\":\"400\",\"message\":\"Invalid UUID\"}";
                }
                if (Bukkit.getOfflinePlayer(specifiedUUID).hasPlayedBefore()) {
                    response.type("application/json");
                    response.status(200);

                    String placeholderRaw = request.params(":placeholder");
                    if (!placeholders.contains(placeholderRaw)) {
                        return "{\"status\":\"406\",\"message\":\"Invalid Placeholder\"}";
                    }

                    String Placeholder = "{\"status\":\"200\",\"message\":\"" + PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(UUID.fromString(request.params(":uuid"))), "%" + request.params(":placeholder") + "%") + "\"}";

                    if (Placeholder.equals("%" + request.params(":placeholder") + "%")) {

                        response.type("application/json");
                        response.status(406);
                        return "{\"status\":\"406\",\"message\":\"Invalid Placeholder\"}";

                    } else {

                        return Placeholder;

                    }
                } else {

                    response.type("application/json");
                    response.status(400);
                    return "{\"status\":\"400\",\"message\":\"Player Has Not Played Before\"}";

                }
            }

        });
        http.get("/*", (request, response) -> {

            response.status(404);
            response.type("application/json");
            return "{\"status\":\"404\",\"message\":\"Invalid URI\"}";

        });
        http.get("/*/*/*", (request, response) -> {

            response.status(404);
            response.type("application/json");
            return "{\"status\":\"404\",\"message\":\"Invalid URI\"}";

        });
        http.get("/*/*/*/*", (request, response) -> {

            response.status(404);
            response.type("application/json");
            return "{\"status\":\"404\",\"message\":\"Invalid URI\"}";

        });
        http.notFound((request, response) -> {

            response.status(404);
            response.type("application/json");
            return "{\"status\":\"404\",\"message\":\"Invalid URI\"}";

        });
    }

    void destroy() {
        http.stop();
    }

}