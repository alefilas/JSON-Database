package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Test {


    public static void main(String[] args) {
        Gson gson = new Gson();
        String db = "{\"1\":\"Hello world!\",\"person\":{\"name\":\"Elon Musk\",\"car\":{\"model\":\"Tesla Roadster\",\"year\":\"2018\"},\"rocket\":{\"name\":\"Falcon 9\",\"launches\":\"87\"}}}";
        String qu = "{\"type\":\"get\",\"key\":[\"person\",\"name\"]}";

        JsonObject query = gson.fromJson(qu, JsonObject.class);
        JsonArray ar = query.get("key").getAsJsonArray();

        JsonObject db1 = gson.fromJson(db, JsonObject.class);
        JsonObject obj = db1.get(ar.get(0).getAsString()).getAsJsonObject();
        System.out.println(db1.get("person").toString());
        System.out.println(db);

        for (int i = 1; i < ar.size() - 1; i++) {
            obj = obj.get(ar.get(i).getAsString()).getAsJsonObject();
        }

        System.out.println(obj.get(ar.get(ar.size() - 1).getAsString()).getAsString());
    }

}
