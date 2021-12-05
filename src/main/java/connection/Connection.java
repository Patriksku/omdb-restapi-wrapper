package connection;

import utility.PropertiesReader;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import spark.Response;

import java.util.Map;
import java.util.Objects;

/**
 * Responsible for sending a request to OMDbAPI as specified by the client,
 * and returning the response as JSON or XML, based on the client's HTTP Accept header.
 */
public class Connection {

    private final String OMDb_URL = "http://www.omdbapi.com/?";

    public Connection() {}

    /**
     * Sends a request to OMDbAPI and returns the response.
     * @param parameters Client's query parameters.
     * @param acceptHeader Client's HTTP Accept header (Either JSON or XML).
     * @param thisResponse Response object for specifying status code and content type of the response.
     * @return OMDbAPI response as JSON or XML, represented as a String.
     */
    public String makeCallToOMDb(Map<String, Object> parameters, String acceptHeader, Response thisResponse) {
        HttpResponse<String> response = null;

        if (acceptHeader.equals("xml")) {
            try {
                response = Unirest.get(OMDb_URL)
                        .queryString("apikey", PropertiesReader.getAPIKey())
                        .queryString(parameters)
                        .asString();

                thisResponse.type("application/xml");
                thisResponse.status(response.getStatus());

                return response.getBody();

            } catch (UnirestException e) {
                e.printStackTrace();
            }

        } else {
            try {
                response = Unirest.get(OMDb_URL)
                        .queryString("apikey", PropertiesReader.getAPIKey())
                        .queryString(parameters)
                        .asString();

                thisResponse.type("application/json");
                thisResponse.status(response.getStatus());

            } catch (UnirestException e) {
                e.printStackTrace();
            }

        }

        return Objects.requireNonNull(response).getBody();
    }
}
