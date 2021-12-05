package omdbwrapper;

import connection.Connection;
import errorhandling.ErrorHandler;
import logging.LoggerUnit;
import utility.RequestBuilder;

import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

/**
 * Responsible for handling requests made by a client to the REST API wrapper.
 */
public class Wrapper {
    private final Connection connection;
    private final RequestBuilder requestBuilder;
    private final ErrorHandler errorHandler;

    private final String path = "/api/v1";
    private final String fullPath = "http://localhost:4567/api/v1/";

    public Wrapper() {
        this.connection = new Connection();
        this.requestBuilder = new RequestBuilder();
        this.errorHandler = new ErrorHandler();
        LoggerUnit.logInfo("Server initialized.");
        init();
    }

    /**
     * Handles request made to /api/v1/movies by collecting any parameters and HTTP Accept headers,
     * and constructing a response based on these.
     */
    public void getMovie() {
        get(path + "/movies", ((request, response) -> {
            LoggerUnit.logInfo("GET request made to API @ [url=" + fullPath + "/movies].");

            Set<String> queryKeys = request.queryParams();

            String requestedAcceptHeaders = request.headers("Accept");
            String acceptHeader = requestBuilder.getHeader(requestedAcceptHeaders);
            String errorStatus = errorHandler.checkRequestForErrors(queryKeys, acceptHeader, request, response);

            // If an error has been found in the parameters specified by the client.
            if (!errorStatus.equals("0")) {
                return errorStatus;

            } else {

                // Builds a mapping of the specified parameters by the client, to work with the OMDb API.
                // Then returns the response from the OMDb API.
                Map<String, Object> queryValues = requestBuilder.getParametersMap(queryKeys, acceptHeader, request);
                return connection.makeCallToOMDb(queryValues, acceptHeader, response);
            }
        }));

    }

    /**
     * Initializes all resources for accepting requests.
     */
    public void init() {
        getMovie();
        LoggerUnit.logInfo("Resource " + path + "/movies" + " initialized for requests.");
    }
}
