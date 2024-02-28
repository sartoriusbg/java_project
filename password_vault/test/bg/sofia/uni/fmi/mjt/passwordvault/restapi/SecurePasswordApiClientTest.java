package bg.sofia.uni.fmi.mjt.passwordvault.restapi;

import bg.sofia.uni.fmi.mjt.passwordvault.exceptions.InvalidApiResponseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SecurePasswordApiClientTest {

    private SecurePasswordApiClient test;

    @Mock
    private HttpClient passwordsHttpClientMock;

    @Mock
    private HttpResponse<String> httpPasswordsResponseMock;

    @BeforeEach
    void setup() throws IOException, InterruptedException {
        when(passwordsHttpClientMock.send(Mockito.any(HttpRequest.class), ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
                .thenReturn(httpPasswordsResponseMock);
        test = new SecurePasswordApiClient(passwordsHttpClientMock);
    }

    @Test
    void checkSecurePasswordTrue() throws URISyntaxException, NoSuchAlgorithmException {
        when(httpPasswordsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
         assertTrue(test.checkSecurePassword("utyiukybvu"), "Should return true for safe password");
    }

    @Test
    void checkSecurePasswordFalse() throws URISyntaxException, NoSuchAlgorithmException {
        when(httpPasswordsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
        assertFalse(test.checkSecurePassword("password"), "Should return false for unsafe password");
    }

    @Test
    void checkSecurePassword() throws URISyntaxException, NoSuchAlgorithmException {
        when(httpPasswordsResponseMock.statusCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
        assertThrows(InvalidApiResponseException.class, () -> test.checkSecurePassword("password"),
                "Should throw InvalidApiResponseException if the status code is non 200 or 404");
    }
}
