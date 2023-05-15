package dropit.exercise.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dropit.exercise.model.dto.Address;
import dropit.exercise.model.geoapify.AddressResult;
import dropit.exercise.model.request.AddressRequest;
import dropit.exercise.config.Config;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class AddressesService {

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient = HttpClient.newHttpClient();
    public Address resolveAddress(AddressRequest addressUnstructured) {
        String address = getAddressFromGeoapify(addressUnstructured);
        return convertAddressStringIntoObject(address);
    }

    private String getAddressFromGeoapify(AddressRequest addressUnstructured) {
        try {
            String encodedAddress = UriUtils.encode(addressUnstructured.getSearchTerm(), StandardCharsets.UTF_8);
            String url = "https://api.geoapify.com/v1/geocode/search?text=" + encodedAddress + "&apiKey=" + Config.API_KEY;

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Content-Type", "application/json")
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Error while resolving address: " + e.getMessage());

        }
    }

    private Address convertAddressStringIntoObject(String address) {
        try {
            AddressResult addressResult = objectMapper.readValue(address, AddressResult.class);
            return objectMapper.convertValue(addressResult.getFeatures().get(0).getProperties(), Address.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while Deserializing address: " + address + " /n error message: " + e.getMessage());
        }
    }
}
