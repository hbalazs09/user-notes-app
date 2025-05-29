package com.horvath.usernotesapp.api.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
public class ValidationErrorResponse extends ErrorResponse {

    private String error = "Validation Error";

    @JsonProperty("field_errors")
    private Map<String, String> fieldErrors;
}
