package client.JSON;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

public class Query {

    @Parameter(names = "-in", description = "fileName")
    private String fileName;

    @Parameter(names = "-t", description = "type")
    private String type;

    @Parameter(names = "-k", description = "key")
    private String key;

    @Parameter(names = "-v", description = "value")
    private String value;

    public static String buildQuery(String[] args) {

        Query query = new Query();
        Gson gson = new Gson();

        JCommander.newBuilder()
                .addObject(query)
                .build()
                .parse(args);

        if (query.fileName == null) {
            return gson.toJson(query);
        } else {
            return query.readFile();
        }
    }

    private String readFile() {
        //  JSON Database/task/

        try (Reader reader = Files.newBufferedReader(Path.of("src/client/data/" + fileName))) {
            return JsonParser.parseReader(reader).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsonObject().toString();
    }
}
