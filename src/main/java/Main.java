import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException {
        URL url = new URL("http://localhost:3000/test/login/post");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
//        String jsonInputString = "{\"login\": \"admin\", \"pass\": \"admin123\"}";
//        String jsonInputString = "{login: admin, pass: admin123}";

        TestUser user = new TestUser("admin", "admin123");

//        System.out.println(jsonInputString);

        Gson g = new Gson();
        String jsonInputString = g.toJson(user);

        System.out.println(jsonInputString);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }

            String json = response.toString();

            Response res = g.fromJson(json, Response.class);

            System.out.println(res.getMessage());
            System.out.println(res.getRole());
        }
    }
}
