package com.internly.integration.storage;

import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service; import org.springframework.web.multipart.MultipartFile; import java.io.IOException; import java.nio.file.*; import java.util.UUID;

@Service public class LocalFileStorageService implements FileStorageService {
    private final Path root;
    public LocalFileStorageService(@Value("${storage.local-dir:uploads}") String directory) throws IOException { root=Path.of(directory).toAbsolutePath().normalize(); Files.createDirectories(root); }
    public String store(MultipartFile file) throws IOException { String key=UUID.randomUUID()+"-"+safe(file.getOriginalFilename()); Files.copy(file.getInputStream(),root.resolve(key),StandardCopyOption.REPLACE_EXISTING); return key; }
    public void delete(String storageKey) throws IOException { if(storageKey!=null&&!storageKey.contains("..")) Files.deleteIfExists(root.resolve(storageKey).normalize()); }
    private String safe(String filename) { String clean=filename==null?"resume":filename.replaceAll("[^a-zA-Z0-9._-]","_"); return clean.length()>120?clean.substring(clean.length()-120):clean; }
}
