package group4.programmingproject1;

        import android.graphics.Bitmap;


public class SelectUser {
    String name;

    public Bitmap getThumb() {
        return thumb;
    }

    public void setThumb(Bitmap thumb) {
        this.thumb = thumb;
    }

    Bitmap thumb;

    public String getThumbURI() {return thumbURI;}

    public void setThumbURI(String thumbURI) {this.thumbURI =thumbURI;}

    String thumbURI;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}