import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpQuery {

    private Map<String, String> parameters = new LinkedHashMap<>();

    public HttpQuery(String query) {
        for(String parameter : query.split("&")) {
            int equalsPos = parameter.indexOf("=");
            String parameterName = urlDecode(parameter.substring(0, equalsPos));
            String parameterValue = urlDecode(parameter.substring(equalsPos+1));
            parameters.put(parameterName, parameterValue);
        }
    }

    private String urlDecode(String substring) {
        try {
            return URLDecoder.decode(substring, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Can't happen - UTF-8 is always supported", e);
        }
    }

    private String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Can't happen - UTF-8 is always supported", e);
        }
    }

    public String getParameter(String parameterName) {
        return parameters.get(parameterName);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Entry<String, String> entry : parameters.entrySet()) {
            if(result.length() > 0) result.append("&");
            result.append(urlEncode(entry.getKey()) + "=" + urlEncode(entry.getValue()));
        }
        return result.toString();
    }


}
