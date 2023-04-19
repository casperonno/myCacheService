package org.example.myCacheService;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.RestController;

public class GetResponse<T> {
    @JsonProperty("status")
    private String status;

    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private T data;


    public GetResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> GetResponse<T> success(T data) {
        return new GetResponse<>("success", null, data);
    }

    public static <T> GetResponse<T> error(String message) {
        return new GetResponse<>("error", message, null);
    }
}
