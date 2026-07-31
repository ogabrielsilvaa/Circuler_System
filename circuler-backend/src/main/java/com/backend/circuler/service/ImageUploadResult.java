package com.backend.circuler.service;

public class ImageUploadResult {

    private final String url;
    private final String publicId;

    public ImageUploadResult(String url, String publicId) {
        this.url = url;
        this.publicId = publicId;
    }

    public String getUrl() { return url; }

    public String getPublicId() { return publicId; }
}
