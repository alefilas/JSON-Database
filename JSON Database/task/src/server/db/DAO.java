package server.db;


import com.google.gson.*;
import server.JSON.Answer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class DAO {

    private final String fileName;
    private final Gson gson;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    public DAO() {
        this.fileName = "src\\server\\data\\db.json";
        //this.fileName = "JSON Database/task/src/server/data/db.json";
        this.gson = new Gson();
    }

    public String delete(JsonElement key) {

        writeLock.lock();

        JsonObject db = getJsonObject();
        JsonElement result;

        if (key.isJsonArray()) {

            JsonObject obj = db;

            for (int i = 0; i < key.getAsJsonArray().size() - 1; i++) {
                obj = obj.get(key.getAsJsonArray().get(i).getAsString()).getAsJsonObject();
            }

            result = obj.remove(key
                    .getAsJsonArray()
                    .get(key.getAsJsonArray().size() - 1)
                    .getAsString());

        } else {
            result = db.remove(key.getAsString());
        }

        Answer answer;

        if (result == null) {
            answer =  new Answer("ERROR");
            answer.setReason("No such key");
        } else {
            writeJson(db);
            answer = new Answer("OK");
        }

        writeLock.unlock();
        return gson.toJson(answer);
    }


    public String set(JsonElement key, JsonElement value) {

        writeLock.lock();

        JsonObject db = getJsonObject();

        if (key.isJsonArray()) {

            addProperty(key.getAsJsonArray(), value, db, 0);

        } else {
            db.add(key.getAsString(), value);
        }

        writeJson(db);

        writeLock.unlock();
        return gson.toJson(new Answer("OK"));
    }

    private void addProperty(JsonArray jsonArray, JsonElement value, JsonObject db, int i) {

        if (db.has(jsonArray.get(jsonArray.size() - 1).getAsString())) {
            db.add(jsonArray.get(jsonArray.size() - 1).getAsString(), value);
        } else {
            db = db.get(jsonArray.get(i).getAsString()).getAsJsonObject();
            addProperty(jsonArray, value, db, i + 1);
        }
        db = new JsonObject();
        db.add(jsonArray.get(i).getAsString(), db);
    }


    public String get(JsonElement key) {

        readLock.lock();


        JsonElement result;
        JsonObject db = getJsonObject();
        if (key.isJsonArray() && key.getAsJsonArray().size() > 1) {

            JsonObject obj = db.get(key.getAsJsonArray().get(0).getAsString()).getAsJsonObject();

            for (int i = 1; i < key.getAsJsonArray().size() - 1; i++) {
                obj = obj.get(key.getAsJsonArray().get(i).getAsString()).getAsJsonObject();
            }

            result = obj.get(key.getAsJsonArray().get(key.getAsJsonArray().size() - 1).getAsString());

        } else if (key.isJsonArray()){

            result = db.get(key.getAsJsonArray().get(0).getAsString()).getAsJsonObject();

        } else {

            result = db.get(key.getAsString());

        }

        Answer answer;
        if (result == null) {
            answer =  new Answer("ERROR");
            answer.setReason("No such key");
        } else {
            answer = new Answer("OK");
            answer.setValue(result.isJsonObject() ? result : result);
        }

        readLock.unlock();
        return gson.toJson(answer);
    }

    private JsonObject getJsonObject() {

        try (Reader reader = Files.newBufferedReader(Path.of(fileName))) {
            return JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JsonObject();
    }

    private void writeJson(JsonObject object) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(object.toString());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
