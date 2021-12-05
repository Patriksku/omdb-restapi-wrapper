package errorhandling;

import org.json.JSONObject;
import spark.Request;
import spark.Response;
import utility.JSONtoXMLFormatter;

import java.util.Set;

import static spark.Spark.internalServerError;
import static spark.Spark.notFound;

/**
 * Responsible for handling errors in a client's request, such as wrong parameter types (400) or 404/500 errors.
 * - 404 or 500 errors are handled by displaying an HTML information page.
 * - 400 errors are handled by returning JSON or XML containing an indication of an error, accompanied by a message.
 */
public class ErrorHandler {

    private final JSONtoXMLFormatter jsonToXMLFormatter;

    private Set<String> queryKeys;
    private String acceptHeader;
    private Request request;
    private Response response;
    private String responseDocument;

    public ErrorHandler() {
        this.jsonToXMLFormatter = new JSONtoXMLFormatter();
        init();
    }

    /**
     * Checks the client's request for parameter-based errors.
     * @param queryKeys Client's query keys.
     * @param acceptHeader Client's HTTP Accept header.
     * @param request Client's request.
     * @param response Wrapper APIs response object.
     * @return "0" if no errors are identified, otherwise JSON or XML with the specified errors.
     */
    public String checkRequestForErrors(Set<String> queryKeys, String acceptHeader, Request request, Response response) {

        this.responseDocument = "0";
        this.queryKeys = queryKeys;
        this.acceptHeader = acceptHeader;
        this.request = request;
        this.response = response;

        if (!noParameters().equals("0")) {
            return noParameters();
        }

        if (!wrongParameters().equals("0")) {
            return wrongParameters();
        }

        return responseDocument;
    }

    /**
     * Checks if a Client's request contains parameters.
     * @return "0" if no errors are identified, otherwise JSON or XML with the specified errors.
     */
    private String noParameters() {

        if (queryKeys.isEmpty()) {
            responseDocument = new JSONObject()
                    .put("error", "true")
                    .put("errorMessage", "At least one parameter has to be specified in order to " +
                            "process this request.")
                    .toString();

            response.status(400);

            if (acceptHeader.equals("json")) {
                response.type("application/json");

            } else {
                response.type("application/xml");
                return jsonToXMLFormatter.getXMLfromJSON(responseDocument);
            }
        }

        return responseDocument;
    }

    /**
     * Checks if a Client's request contains faulty parameter keys and/or values.
     * @return "0" if no errors are identified, otherwise JSON or XML with the specified errors.
     */
    private String wrongParameters() {

        for (String key : queryKeys) {

            // If the parameter keys are incorrect.
            if (!key.equals("movieTitle") && !key.equals("movieYear") && !key.equals("moviePlot")) {
                responseDocument = new JSONObject()
                        .put("error", "true")
                        .put("errorMessage", "Make sure that your query parameter keys are correct.")
                        .toString();

                response.status(400);

                if (acceptHeader.equals("json")) {
                    response.type("application/json");

                } else {
                    response.type("application/xml");
                    return jsonToXMLFormatter.getXMLfromJSON(responseDocument);
                }

                return responseDocument;

            } else {
                if (key.equals("movieYear")) {

                    // If the movieYear parameter contains a non-numerical character.
                    if (!request.queryParams("movieYear").matches("[0-9]+")) {
                        responseDocument = new JSONObject()
                                .put("error", "true")
                                .put("errorMessage", "The 'movieYear' parameter only accepts numerical values.")
                                .toString();

                        response.status(400);

                        if (acceptHeader.equals("json")) {
                            response.type("application/json");

                        } else {
                            response.type("application/xml");
                            return jsonToXMLFormatter.getXMLfromJSON(responseDocument);
                        }

                        return responseDocument;

                    }
                }

                if (key.equals("moviePlot")) {

                    // If the moviePlot parameter does not contain "full" or "short".
                    if (!request.queryParams("moviePlot").equals("full") && !request.queryParams("moviePlot").equals("short")) {
                        responseDocument = new JSONObject()
                                .put("error", "true")
                                .put("errorMessage", "The 'moviePlot' parameter only accepts the following, " +
                                        "case-sensitive values: 'short' or 'full'.")
                                .toString();

                        response.status(400);

                        if (acceptHeader.equals("json")) {
                            response.type("application/json");

                        } else {
                            response.type("application/xml");
                            return jsonToXMLFormatter.getXMLfromJSON(responseDocument);
                        }

                        return responseDocument;
                    }
                }
            }
        }

        return responseDocument;
    }

    /**
     * Displays a designated HTML page on 404 encounters.
     */
    public void handlingNotFound() {
        notFound("<html><body><h1>404 Not Found</h1>" +
                "<p>Make sure that you are trying to access the correct resource at URL: <br>" +
                "http://localhost:4567/api/v1/movies</p></body></html>");
    }

    /**
     * Displays a designated HTML page on 500 encounters.
     */
    public void handlingInternalServerError() {
        internalServerError("<html><body><h1>500 Internal Server Error</h1>" +
                "<p>The server encountered an internal error or misconfiguration and was unable to" +
                " complete your request. <br> <br>" +
                "Please contact the server administrator at admin@example.com and inform the admin" +
                " of the time this error occurred, and the actions you performed" +
                " just before this error.</p></body></html>");
    }

    /**
     * Initializes the designated HTML pages to be displayed when needed.
     */
    public void init() {
        handlingNotFound();
        handlingInternalServerError();
    }
}
