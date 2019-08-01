package com.xinlan.imageeditlibrary.editimage.model;

public class InfoModel {

    private String imagePath;
    private String age;
    private String id;
    private String name;
    private String bottom_image;

    public InfoModel(String imagePath, String age, String id, String name, String bottom_image) {
        this.imagePath = imagePath;
        this.age = age;
        this.id = id;
        this.name = name;
        this.bottom_image = bottom_image;
    }

    public String getBottom_image() {
        return bottom_image;
    }

    public void setBottom_image(String bottom_image) {
        this.bottom_image = bottom_image;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
