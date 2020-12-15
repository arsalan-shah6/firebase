package com.app.firebase.Model;

public class PostModel {
    String pTitel,pImage,pDescription;

    public PostModel() {
    }

    public PostModel(String pTitel, String pImage, String pDescription) {
        this.pTitel = pTitel;
        this.pImage = pImage;
        this.pDescription = pDescription;
    }

    public String getpTitel() {
        return pTitel;
    }

    public void setpTitel(String pTitel) {
        this.pTitel = pTitel;
    }

    public String getpImage() {
        return pImage;
    }

    public void setpImage(String pImage) {
        this.pImage = pImage;
    }

    public String getpDescription() {
        return pDescription;
    }

    public void setpDescription(String pDescription) {
        this.pDescription = pDescription;
    }
}
