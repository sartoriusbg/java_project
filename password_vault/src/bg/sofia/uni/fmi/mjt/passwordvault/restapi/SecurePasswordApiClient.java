package bg.sofia.uni.fmi.mjt.passwordvault.restapi;

import bg.sofia.uni.fmi.mjt.passwordvault.converters.StringHasher;
import bg.sofia.uni.fmi.mjt.passwordvault.errorhandler.ErrorHandler;
import bg.sofia.uni.fmi.mjt.passwordvault.exceptions.ApiConnectionException;
import bg.sofia.uni.fmi.mjt.passwordvault.exceptions.InvalidApiResponseException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;

public class SecurePasswordApiClient {
    private static final String API_ENDPOINT = "https://api.enzoic.com/passwords?";
    private static final String REQUEST_TEXT = "sha1=%s&md5=%s&sha256=%s";
    private static final String HEADER_PARAMETER = "authorization";
    private static final String AUTH_STRING =
            "basic Y2ZjMGQwMTRmZTFkNDU0Mjk1ZDVmMmFiNTY3NjhiMWQ6VzclJG0kNT04S2VzZ1JDeXhDXlhCblo9OThHeXY0cnc=";
    private static final String SHA_1 = "SHA-1";
    private static final String SHA_256 = "SHA-256";
    private static final String MD_5 = "MD5";
    private final HttpClient passwordHttpClient;

    public SecurePasswordApiClient(HttpClient passwordsHttpClient) {
        this.passwordHttpClient = passwordsHttpClient;
    }

    public boolean checkSecurePassword(String password) throws URISyntaxException, NoSuchAlgorithmException {
        HttpResponse<String> response;
        URI uri = new URI(API_ENDPOINT + String.format(REQUEST_TEXT, StringHasher.toHash(password, SHA_1),
                StringHasher.toHash(password, MD_5), StringHasher.toHash(password, SHA_256)));
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(uri).header(HEADER_PARAMETER, AUTH_STRING).build();

            response = passwordHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            ApiConnectionException ex = new ApiConnectionException();
            ErrorHandler.writeLogs(ex);
            throw ex;
        }
        if (response.statusCode() == HttpURLConnection.HTTP_OK) {
            return false;
        } else if (response.statusCode() == HttpURLConnection.HTTP_NOT_FOUND) {
            return true;
        } else {
            InvalidApiResponseException ex = new InvalidApiResponseException();
            ErrorHandler.writeLogs(ex);
            throw ex;
        }
    }

}
