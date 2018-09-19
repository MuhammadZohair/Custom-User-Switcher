package lunaticaliens.com.customuserswitcher;

import android.net.Uri;

/**
 * The type DummyUser.
 */
public class DummyUser {

    private Uri ImageUri;
    private String email;
    private String name;
    private String password;
    private String phoneNumber;
    private String address;


    DummyUser(Uri imageUri, String email, String name, String password, String phoneNumber, String address) {
        ImageUri = imageUri;
        this.email = email;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public Uri getImageUri() {
        return ImageUri;
    }


    public String getEmail() {
        return email;
    }


    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

}
