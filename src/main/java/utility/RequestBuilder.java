package utility;

import spark.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Responsible for mapping a client's request to the REST API wrapper, to work with the OMDb API.
 */
public class RequestBuilder {
    private final String titleKey = "t";
    private final String yearKey = "y";
    private final String plotKey = "plot";
    private final String mediaType = "r";

    private final String movieTitle = "movieTitle";
    private final String movieYear = "movieYear";
    private final String moviePlot = "moviePlot";

    /**
     * Maps the client requests to the REST API wrapper, to match the parameters needed to make a valid
     * request to OMDb API.
     * @param queryKeys Client's query parameter keys.
     * @param acceptHeader Client's HTTP Accept header.
     * @param request The REST API wrapper's request object.
     * @return A valid OMDb API-mapping between parameter keys and values.
     */
    public Map<String, Object> getParametersMap(Set<String> queryKeys, String acceptHeader, Request request) {
        Map<String, Object> parametersMap = new HashMap<>();

        if (queryKeys.contains(movieTitle)) {
            parametersMap.put(titleKey, request.queryParams(movieTitle));
        }

        if (queryKeys.contains(movieYear)) {
            parametersMap.put(yearKey, request.queryParams(movieYear));
        }

        if (queryKeys.contains(moviePlot)) {
            parametersMap.put(plotKey, request.queryParams(moviePlot));
        }

        parametersMap.put(mediaType, acceptHeader);

        return parametersMap;
    }

    /**
     * Returns either json or xml, based on the sequential ordering of media types as specified
     * in the Accept header by the client. This method will return json if neither
     * application/json nor application/xml is specified.
     *
     * 'Sequential ordering' means that the first media type in the HTTP Accept header has the highest priority, and so on.
     *
     * @param acceptHeader String of all specified HTTP Accept headers by the client (comma-separated).
     * @return Media type format for the API's response.
     */
    public String getHeader(String acceptHeader) {
        if (acceptHeader.isEmpty()) {
            return "json";

        } else {

            String[] headersArray = acceptHeader.split(", ");

            for (String header : headersArray) {

                if (header.contains("application/json")) {
                    return "json";

                } else if (header.contains("application/xml")) {
                    return "xml";
                }
            }
        }

        return "json";
    }
}
