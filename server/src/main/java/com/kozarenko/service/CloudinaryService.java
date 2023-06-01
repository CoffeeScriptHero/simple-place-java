package com.kozarenko.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.kozarenko.util.Constants.Cloudinary.CLOUDINARY_SCHEME;
import static com.kozarenko.util.Constants.Cloudinary.CLOUD_NAME;
import static com.kozarenko.util.Constants.Cloudinary.API_KEY;
import static com.kozarenko.util.Constants.Cloudinary.API_SECRET;

@Service
public class CloudinaryService {

  private static final Dotenv dotenv = Dotenv.load();

  private final Cloudinary cloudinary = new Cloudinary(CLOUDINARY_SCHEME + dotenv.get(API_KEY) + ":" +
          dotenv.get(API_SECRET) + "@" + dotenv.get(CLOUD_NAME));

  public void upload(String base64Img) {
    try {
      cloudinary.uploader().upload(base64Img, ObjectUtils.emptyMap());
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }
}
