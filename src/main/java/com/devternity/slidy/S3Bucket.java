package com.devternity.slidy;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jcabi.s3.Bucket;
import com.jcabi.s3.Ocket;
import com.jcabi.s3.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public class S3Bucket implements Storage {

    private final String key;
    private final String secret;
    private final String bucket;

    public S3Bucket(@Value("${s3_bucket}") String bucket, @Value("${s3_key}") String key, @Value("${s3_secret}") String secret) {
        this.key = key;
        this.secret = secret;
        this.bucket = bucket;
    }

    @Override
    public Output out(File file) {

        Region region = new Region.Simple(key, secret);

        Bucket buck = region.bucket(bucket);

        Ocket ocket = buck.ocket(file.getName());
        ObjectMetadata meta = new ObjectMetadata();

        return new Output() {
            @Override
            public void write() throws IOException {
                ocket.write(new FileInputStream(file), meta);
            }

            @Override
            public Url writtenTo() {
                return new Url("https://s3.eu-central-1.amazonaws.com/" + bucket + "/" + file.getName(), "Bucket item");
            }
        };
    }
}
