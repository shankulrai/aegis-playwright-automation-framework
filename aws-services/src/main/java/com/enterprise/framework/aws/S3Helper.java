package com.enterprise.framework.aws;

import com.enterprise.framework.utilities.CsvUtils;
import com.enterprise.framework.utilities.JsonUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * S3 helper with common file operations.
 */
public class S3Helper {
    private final S3Client client;
    private final String bucket;

    public S3Helper(S3Client client, String bucket) {
        this.client = client;
        this.bucket = bucket;
    }

    public void uploadFile(Path file, String key) {
        client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), RequestBody.fromFile(file));
    }

    public Path downloadFile(String key, Path destination) {
        client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build(), ResponseTransformer.toFile(destination));
        return destination;
    }

    public <T> T readJson(String key, Class<T> type) throws IOException {
        byte[] bytes = client.getObjectAsBytes(GetObjectRequest.builder().bucket(bucket).key(key).build()).asByteArray();
        return JsonUtils.read(bytes, type);
    }

    public List<Map<String, String>> readCsv(String key) throws IOException {
        Path temp = Path.of(System.getProperty("java.io.tmpdir"), key.replace('/', '_'));
        downloadFile(key, temp);
        return CsvUtils.read(temp);
    }

    public List<String> listBucketContents(String prefix) {
        return client.listObjectsV2Paginator(ListObjectsV2Request.builder().bucket(bucket).prefix(prefix).build())
            .stream()
            .flatMap(page -> page.contents().stream())
            .map(object -> object.key())
            .toList();
    }

    public void deleteFile(String key) {
        client.deleteObject(DeleteObjectRequest.builder().bucket(bucket).key(key).build());
    }
}
