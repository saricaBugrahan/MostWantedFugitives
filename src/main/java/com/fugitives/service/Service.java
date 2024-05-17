package com.fugitives.service;

import java.util.Optional;

public interface Service {
    Optional<String> sendPostRequest(String apiPath);
    Optional<String> sendGetRequest(String apiPath);
}
