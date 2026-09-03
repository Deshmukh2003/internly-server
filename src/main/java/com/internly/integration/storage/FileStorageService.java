package com.internly.integration.storage;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
  String store(MultipartFile file) throws IOException;
  void delete(String storageKey) throws IOException;
}
