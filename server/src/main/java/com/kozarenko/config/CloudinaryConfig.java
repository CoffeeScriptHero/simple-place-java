package com.kozarenko.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import java.util.HashMap;
import java.util.Map;

@PropertySource("classpath:application.properties")
public class CloudinaryConfig {

  @Value("${cloudinary.cloud.name}")
  private static String cloudName;

  @Value("${cloudinary.api.key}")
  private static String apiKey;

  @Value("${cloudinary.api.secret}")
  private static String apiSecret;

  public static Map<String, String> getConfig() {
    return new HashMap<>() {{
      put("cloud_name", cloudName);
      put("api_key", apiKey);
      put("api_secret", apiSecret);
    }};
  }
}
