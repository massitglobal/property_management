package com.faisal.backend.dto;

import com.faisal.backend.model.UserProperty;

import java.io.Serializable;
import java.util.List;

public record PropertyListing(List<UserProperty> userProperties) implements Serializable {
}
