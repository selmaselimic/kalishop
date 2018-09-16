package ba.kalishop.kalishop;

/**
 * Created by Korisnik on 12/02/2018.
 */

public class Product {

    private String name;
    private String description;
    private int thumbnail;

    public Product() {
    }

    public Product(String name, String description, int thumbnail) {
        this.name = name;
        this.description = description;
        this.thumbnail = thumbnail;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

}
