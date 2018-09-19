package lunaticaliens.com.customuserswitcher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ogaclejapan.arclayout.Arc;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * This application demonstrate the quick switching of users with password authentication.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /* Array of 5 users have been stored hardcodely to show the demo*/
    private DummyUser[] users;

    private ImageView avatarImageView; // avatarimageview that shows the picture of current user
    private View menuLayout; // layout that opens the users in arc layout
    private View loginLayout; // login popup that has been implemented using layout instead of alert dialog
    private ArcLayout arcLayout; // arc layout is a library that shows the imageviews in arc

    private TextView emailAddressTextView; // email textview
    private EditText nameEditText; // shows the name of the user logged in
    private EditText passwordEditText; // shows the password of the user logged in
    private EditText phoneNumberEditText; // shows the phone number of the user logged in
    private EditText addressEditText; // shows the address of the user logged in

    EditText confirmEmailEditText; // when the user wants to switch to another user, email and password authentication is required
    EditText confirmPasswordEditText; // when the user wants to switch to another user, email and password authentication is required
    ImageView userPictureImageView; // switch the user picture on the popup while switching user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();

        avatarImageView = findViewById(R.id.avatarImageView);
        menuLayout = findViewById(R.id.menu_layout);
        loginLayout = findViewById(R.id.loginLayout);
        arcLayout = findViewById(R.id.ArcLayout);
        arcLayout.setArc(Arc.TOP_RIGHT);

        emailAddressTextView = findViewById(R.id.emailTextView);
        nameEditText = findViewById(R.id.nameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        addressEditText = findViewById(R.id.addressEditText);

        emailAddressTextView.setText(users[0].getEmail());
        nameEditText.setText(users[0].getName());
        passwordEditText.setText(users[0].getPassword());
        phoneNumberEditText.setText(users[0].getPhoneNumber());
        addressEditText.setText(users[0].getAddress());


        //sets the arc Layout for each user
        for (int i = 0, size = arcLayout.getChildCount(); i < size; i++) {
            arcLayout.getChildAt(i).setOnClickListener(this);
        }

        avatarImageView.setOnClickListener(this);

        /* Sets the picture of the users */
        ImageView picture1 = findViewById(R.id.picture1);
        picture1.setImageURI(users[1].getImageUri());

        ImageView picture2 = findViewById(R.id.picture2);
        picture2.setImageURI(users[2].getImageUri());

        ImageView picture3 = findViewById(R.id.picture3);
        picture3.setImageURI(users[3].getImageUri());

        ImageView picture4 = findViewById(R.id.picture4);
        picture4.setImageURI(users[4].getImageUri());


    }

    /**
     * Loads the data of the users already stored in string.xml
     */
    private void loadData() {
        users = new DummyUser[5];
        String[] emails = getResources().getStringArray(R.array.emails);
        String[] names = getResources().getStringArray(R.array.names);
        String[] phoneNumbers = getResources().getStringArray(R.array.phone_numbers);
        String[] passwords = getResources().getStringArray(R.array.passwords);
        String[] addresses = getResources().getStringArray(R.array.addresses);

        /* parse the image uris of the users */
        for (int i = 0; i < 5; i++) {
            String uriLocation = "android.resource://lunaticaliens.com.customfloatingactionbutton/drawable/face" + i;
            Uri uri = Uri.parse(uriLocation);

            users[i] = new DummyUser(uri, emails[i], names[i], passwords[i], phoneNumbers[i], addresses[i]);
        }
    }

    /**
     * This method is called whenever any widget is clicked.
     * @param v is the view that gets the element which is clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == avatarImageView) {
            onAvatarImageViewClicked(v);
            return;
        }

        if (v instanceof ImageView) {
            showLoginPopup((ImageView) v);
        }
    }

    private void showLoginPopup(final ImageView imageView) {

        /* conditional statements which check which button is clicked*/
        if (imageView.getContentDescription().equals(getResources().getString(R.string.user0))) {
            authenticateLogin(imageView, 0);
        } else if (imageView.getContentDescription().equals(getResources().getString(R.string.user1))) {
            authenticateLogin(imageView, 1);
        } else if (imageView.getContentDescription().equals(getResources().getString(R.string.user2))) {
            authenticateLogin(imageView, 2);
        } else if (imageView.getContentDescription().equals(getResources().getString(R.string.user3))) {
            authenticateLogin(imageView, 3);
        } else if (imageView.getContentDescription().equals(getResources().getString(R.string.user4))) {
            authenticateLogin(imageView, 4);
        }
    }

    /**
     * keeps a check if the arc is open on closed
     * @param v is the view
     */
    private void onAvatarImageViewClicked(View v) {
        if (v.isSelected()) {
            hideMenu();
        } else {
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }

    /**
     * This method opens the menu of users
     */
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);
        menuLayout.setBackground(null);
        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    /**
     * This method hides the menu of users
     */
    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        /* add all the users in the animation array */
        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }
        /* if the login layout is already visible animate it to close. */
        if (loginLayout.getVisibility() == View.VISIBLE) {
            Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disappear);
            loginLayout.startAnimation(slideDown);
            loginLayout.setVisibility(View.INVISIBLE);
        }

        /* animate the users to open while rotating */
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    /**
     * This method implements the show animation of the avatar images.
     * @param item the item on which the animation has to be applied
     * @return Animator object
     */
    private Animator createShowItemAnimator(View item) {

        float dx = avatarImageView.getX() - item.getX();
        float dy = avatarImageView.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

        return ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(0f, 720f),
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );
    }

    /**
     * This method implements the hide animation of the avatar images.
     * @param item the item on which the animation has to be applied
     * @return Animator object
     */
    private Animator createHideItemAnimator(final View item) {
        float dx = avatarImageView.getX() - item.getX();
        float dy = avatarImageView.getY() - item.getY();

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.rotation(7200f, 0f),
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }

    /**
     * Method that auuthenticate the user when the credentials are entered on the pop up screen.
     * @param imageView the imageview which is being clicled
     * @param index index of the users array
     */
    private void authenticateLogin(final ImageView imageView, final int index) {

        if (loginLayout.getVisibility() == View.INVISIBLE) {
            menuLayout.setBackground(getResources().getDrawable(R.color.scrim));
            loginLayout.setVisibility(View.VISIBLE);
            Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.appear);
            loginLayout.startAnimation(slideUp);
        }
        userPictureImageView = findViewById(R.id.userPictureImageView);
        userPictureImageView.setImageURI(users[index].getImageUri());

        confirmEmailEditText = findViewById(R.id.confirmEmailEditText);
        confirmEmailEditText.setText(users[index].getEmail());

        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (confirmPasswordEditText.getText().toString().trim().equals(users[1].getPassword())) {
                    String avatarContentDesc = (String) avatarImageView.getContentDescription();

                    Drawable myDrawable = avatarImageView.getDrawable();
                    avatarImageView.setImageDrawable(imageView.getDrawable());
                    imageView.setImageDrawable(myDrawable);

                    avatarImageView.setContentDescription(imageView.getContentDescription());
                    imageView.setContentDescription(avatarContentDesc);

                    emailAddressTextView.setText(users[index].getEmail());
                    nameEditText.setText(users[index].getName());
                    passwordEditText.setText(users[index].getPassword());
                    phoneNumberEditText.setText(users[index].getPhoneNumber());
                    addressEditText.setText(users[index].getAddress());

                    hideMenu();
                    findViewById(R.id.avatarImageView).setSelected(false);
                    confirmPasswordEditText.setText("");
                    Toast.makeText(MainActivity.this, "Welcome " + users[index].getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    hideMenu();
                    findViewById(R.id.avatarImageView).setSelected(false);
                    confirmPasswordEditText.setText("");
                }

                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disappear);
                loginLayout.startAnimation(slideDown);
                loginLayout.setVisibility(View.INVISIBLE);
                menuLayout.setBackground(null);
            }
        });

        /* Fake email toast that lets user know that theh email has been generated for reseting the password. */
        TextView forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.disappear);
                loginLayout.startAnimation(slideDown);
                loginLayout.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Password reset link has been sent to email: "
                        + users[index].getEmail(), Toast.LENGTH_LONG).show();
                menuLayout.setBackground(null);
            }
        });
    }
}
