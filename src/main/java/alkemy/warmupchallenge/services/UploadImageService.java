package alkemy.warmupchallenge.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.entity.ContentType.*;

@Service
public class UploadImageService {

    public void isAnImage(MultipartFile file) {
        if(!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("The file must be a valid image (JPEG, JPG, PNG, GIF).");
        }
    }

    public void fileIsEmpty(MultipartFile file) {
        if(file.isEmpty()) {
            throw new IllegalStateException("Can't upload a empty file [" + file.getSize() + "]");
        }
    }

    public Map<String, String> extractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

}
