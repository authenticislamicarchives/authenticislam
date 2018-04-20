package authenticislam.util;


/**
 * Created by abdoulbasith on 27/05/2017.
 */

public class Oeuvre {

    protected String id, name, author, house, category, type, date, link;


    public Oeuvre() {
    }

    public Oeuvre(String id, String name, String author, String house, String category, String type, String link, String date) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.house = house;
        this.category = category;
        this.type = type;
        this.link = link;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getHouse() {
        return house;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getLink() {
        return link;
    }

    public String getDate() {
        return date;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Oeuvre{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", house='" + house + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", link='" + link + '\'' +
                ", date=" + date +
                '}';
    }

}
