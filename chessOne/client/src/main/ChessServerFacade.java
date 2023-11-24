import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ChessServerFacade {

    private Map response;

    public Map talkToServer(String urlString, String method, String header, String body) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);

            http.setDoOutput(true);
            http.addRequestProperty("authorization", header);
            //http.addRequestProperty("Content-Type", "application/json");
            if (!body.isEmpty()) {
                var outputStream = http.getOutputStream();
                outputStream.write(body.getBytes());
            }

            http.connect();

            int statusCode = http.getResponseCode();
            String statusMessage = http.getResponseMessage();
            if (statusCode != 200) {
                response = new HashMap();
                response.put("statusCode", statusCode);
                response.put("statusMessage", statusMessage);
                return response;
            }
            InputStream inputStream = http.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);
            response = new Gson().fromJson(reader, Map.class);
            response.put("statusCode", statusCode);
            response.put("statusMessage", statusMessage);
            return response;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
