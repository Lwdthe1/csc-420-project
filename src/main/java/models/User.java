package models;

import utils.AppUtils;
import utils.ImageUtils;

import java.awt.image.BufferedImage;

/**
 * Created by lwdthe1 on 12/4/16.
 */
public class User {
    private String username;
    private String imageUrl;
    private BufferedImage image;

    public User(String username, String imageUrl) {
        this.username = username;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public BufferedImage getImage(int width, int height) {
        if(image == null) image = ImageUtils.loadImage(imageUrl);
        ImageUtils.resizeBufferedImage(image, width, height);
        return image;
    }

    public BufferedImage getImage() {
        if(image == null) image = ImageUtils.loadImage(imageUrl);
        return image;
    }
}
