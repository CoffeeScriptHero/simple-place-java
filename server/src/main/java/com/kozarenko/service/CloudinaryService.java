package com.kozarenko.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.kozarenko.util.Constants.Cloudinary.CLOUDINARY_SCHEME;
import static com.kozarenko.util.Constants.Cloudinary.CLOUD_NAME;
import static com.kozarenko.util.Constants.Cloudinary.API_KEY;
import static com.kozarenko.util.Constants.Cloudinary.API_SECRET;
import static com.kozarenko.util.Constants.Cloudinary.POSTS_PRESET;
import static com.kozarenko.util.Constants.Cloudinary.PROFILE_PICS_PRESET;

@Service
public class CloudinaryService {

  private static final Dotenv dotenv = Dotenv.load();

  private final Cloudinary cloudinary = new Cloudinary(CLOUDINARY_SCHEME + dotenv.get(API_KEY) + ":" +
          dotenv.get(API_SECRET) + "@" + dotenv.get(CLOUD_NAME));

  public Map<String, Object> uploadProfilePic(String base64Img, String publicId) {
    return upload(base64Img, publicId, PROFILE_PICS_PRESET);
  }

  public Map<String, Object> uploadPostPic(String base64Img, String publicId) {
    return upload(base64Img, publicId, POSTS_PRESET);
  }

  private Map<String, Object> upload(String base64Img, String uploadPreset, String publicId) {
    try {
      return cloudinary.uploader().unsignedUpload(
              base64Img,
              uploadPreset,
              ObjectUtils.asMap("public_id", publicId)
      );
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    return null;
  }
}
