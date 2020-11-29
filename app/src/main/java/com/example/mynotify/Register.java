package com.example.mynotify;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private CallbackManager mCallbackManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private AccessTokenTracker accessTokenTracker;
    private TextView textViewUser;
    private LoginButton loginButton;
    private SignInButton signInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private int RC_SIGN_IN = 1;
    private static final String TAG = "FacebookAuthentication";
    EditText mFullName, mEmail, mPassword, mPhone, mpassword1;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mFullName = findViewById(R.id.fullName);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mpassword1 = findViewById(R.id.cpassword);
        //mPhone = findViewById(R.id.phone);
        mRegisterBtn = findViewById(R.id.Rbutton);
        mLoginBtn = findViewById(R.id.createText);
        fAuth = FirebaseAuth.getInstance();
        //progressBar = findViewById(R.id.progressBar);


        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(v -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String confirmPassword = mpassword1.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is Required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required");
                return;
            }
            if (password.length() < 6) {
                mPassword.setError("Password must be at least 6 characters long");
                return;
            }
            if (password.equals(confirmPassword)) {
                password = confirmPassword;
            } else {
                mpassword1.setError("Enter the same password as above");
                return;
            }
            progressDialog = new ProgressDialog(Register.this);
            progressDialog.show();
            progressDialog.setContentView(R.layout.progress_dialog);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            //progressBar.setVisibility(View.VISIBLE);
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener((Task<AuthResult> task) -> {
                //to check if the user is registered successfully or not
                //////////////verification mail/////////////
                FirebaseUser fuser = fAuth.getCurrentUser();
                fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Register.this, "Verification Email has been sent" , Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Email not sent" + e.getMessage());
                    }
                });
                /////////////////verification mail end//////////////////
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                    Register.this.startActivity(new Intent(Register.this.getApplicationContext(), OtpActivity.class));
                } else {
                    Toast.makeText(Register.this, "Error!" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            });


        });


        mLoginBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), Login.class)));
//The code from below is for facebook login

        mFirebaseAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError" + error);

            }
        });

       /* authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    updateUI(user);
                }
                else {
                    updateUI(null);
                }

            }
        };*/


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken == null)
                {
                    mFirebaseAuth.signOut();
                }

            }
        };


        //gmail sign-in code
        //Adding buttons to the OnClickListener
        findViewById(R.id.gsignin).setOnClickListener(this);

        //Building Google sign-in and sign-up option.
// Configuring Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
// for the requestIdToken, use getString(R.string.default_web_client_id), this is in the values.xml file that
                // is generated from your google-services.json file (data from your firebase project), uses the google-sign-in method
                // web api key
                .requestIdToken(getString(R.string.default_web_client_id))//Default_web_client_id will be matched with the
                .requestEmail()
                .build();

// Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

// Set the dimensions of the sign-in button.
       // Button signInButton = findViewById(R.id.gsignin);

// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();




    }

   /* @Override
    public void onBackPressed() {
        progressDialog.dismiss();
    }*/

    //////////////////////////////////////////////////GMAIL//////////////////////////////////////////////////////////////////
//facebook///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void handleFacebookToken(AccessToken token) {
        Log.d(TAG, "handleFacebookToken" + token);
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "sign in with credential: successful");
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    //updateUI(user);
                } else {
                    Log.d(TAG, "sign in with credential: failure", task.getException());
                    Context context = getApplicationContext();
                    CharSequence text = "Authentication Failed!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();



                }
            }
        });

    }



    //handle sign in result


    /*private void updateUI(FirebaseUser user) {
        if(user != null)
        {
            textViewUser.setText(user.getDisplayName());
            if(user.getPhotoUrl() != null)
            {
                String photoUrl = user.getPhotoUrl().toString();
                photoUrl = photoUrl +  "?type=large";
                Picasso.get().load(photoUrl);
            }

        }
        else {
            textViewUser.setText("");

        }


    }*/

    /*@Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(authStateListener != null)
        {
            mFirebaseAuth.removeAuthStateListener(authStateListener);
        }
    }*/

    public void flogin(View view) {
        startActivity(new Intent(getApplicationContext(), Login.class));
    }

    //gmail signin code
    //Creating onStart() method.
    @Override
    public void onStart() {
        super.onStart();

// Checking if the user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d(TAG, "Currently Signed in: " + currentUser.getEmail());
            Toast.makeText(Register.this, "Currently Logged in: " + currentUser.getEmail(), Toast.LENGTH_LONG).show();
        }
    }
    //Calling onActivityResult to use the information about the sign-in user contains in the object.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
// Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Toast.makeText(this, "Google Sign in Succeeded",  Toast.LENGTH_LONG).show();
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
// Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google Sign in Failed " + e,  Toast.LENGTH_LONG).show();
            }
        }
    }
    //Creating helper method FirebaseAuthWithGoogle().
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        //Calling get credential from the GoogleAuthProviderG
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    //Override th onComplete() to see we are successful or not.
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
// Update UI with the sign-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());
                            Toast.makeText(Register.this, "Firebase Authentication Succeeded ",  Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        } else {
// If sign-in fails to display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Register.this, "Firebase Authentication failed:" + task.getException(),  Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signInToGoogle(){
        //Calling Intent and call startActivityForResult() method
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.gsignin) {

            signInToGoogle();

        }


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}





