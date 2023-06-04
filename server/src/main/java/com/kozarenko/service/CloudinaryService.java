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
import static com.kozarenko.util.Constants.Cloudinary.POSTS_FOLDER;
import static com.kozarenko.util.Constants.Cloudinary.PROFILE_PICS_FOLDER;

@Service
public class CloudinaryService {

  private static final Dotenv dotenv = Dotenv.load();

  private final Cloudinary cloudinary = new Cloudinary(CLOUDINARY_SCHEME + dotenv.get(API_KEY) + ":" +
          dotenv.get(API_SECRET) + "@" + dotenv.get(CLOUD_NAME));

  public String uploadProfilePic(String base64Img, String publicId) {
    return upload(base64Img, PROFILE_PICS_PRESET, publicId);
  }

  public String uploadPostPic(String base64Img, String publicId) {
    return upload(base64Img, POSTS_PRESET, publicId);
  }

  public void deletePostPic(String publicId) throws IOException {
    deletePic(POSTS_FOLDER + publicId);
  }

  public void deleteProfilePic(String publicId) throws IOException {
    deletePic(PROFILE_PICS_FOLDER + publicId);
  }

  private void deletePic(String publicId) throws IOException {
    cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("invalidate", true));
  }

  private String upload(String base64Img, String uploadPreset, String publicId) {
    try {
      return cloudinary.uploader().upload(
              base64Img,
              ObjectUtils.asMap(
                      "public_id", publicId,
                      "upload_preset", uploadPreset,
                      "overwrite", true,
                      "invalidate", true)
      ).get("secure_url").toString();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
    return null;
  }
}
