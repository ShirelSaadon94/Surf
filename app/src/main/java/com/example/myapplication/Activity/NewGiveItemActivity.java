package com.example.myapplication.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.PhotoModeDialog;
import com.example.myapplication.Interface.PhotoModeListener;
import com.example.myapplication.Objects.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class NewGiveItemActivity extends AppCompatActivity implements PhotoModeListener {
    private static final String TAG = "pttt";
    private ShapeableImageView itemPhoto;
    private TextInputLayout itemName;
    private EditText itemPrice;
    private TextInputLayout itemDescription;
    private Spinner condition;
    private Spinner category;
    private MaterialButton submitBtn;
    private Bitmap userCustomImage;
    private static final String NEW_GIVE_ITEM = "111";
    public static final String CURRENT_USER = "currentUser";
    private static final int CAMERA_PERMISSION_SETTINGS_REQUSETCODE = 123;
    private static final int STORAGE_PERMISSION_SETTINGS_REQUSETCODE = 133;
    private static final int CAMERA_PICTURE_REQUEST = 124;
    private static final int STORAGE_PICTURE_REQUEST = 125;
    private static final int NEW_GIVE_ITEM_RESULT_CODE = 1011;

    private static final String ITEM_COUNT = "itemCount";


    private int itemCount = 0;
    private ArrayList<String> categoriesUS;
    private ArrayList<String> conditionsUS;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_give_item);
        initViews();
        setViewListeners();
    }

    private void initViews() {
        Log.d(TAG, "initViews: Creating views");
        itemPhoto = findViewById(R.id.editItem_IMG_itemPhoto);
        itemPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPhotoDialog();
            }
        });
        itemName = findViewById(R.id.editItem_EDT_itemName);
        itemDescription = findViewById(R.id.editItem_EDT_itemDescription);
        itemPrice = findViewById(R.id.editItem_EDT_itemPrice);
        condition = findViewById(R.id.editItem_LST_conditionSpinner);
        category = findViewById(R.id.editItem_LST_categorySpinner);
        submitBtn = findViewById(R.id.editItem_BTN_submitBtn);
        initCategorySpinner();
        initConditionSpinner();

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkForValidInput();
                saveNewReview();
            }
        });
    }

    /**
     * A method to set view listeners in case the user to enable the submit button after bad info
     */
    private void setViewListeners() {
        Log.d(TAG, "setViewListeners: ");

        condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                submitBtn.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                submitBtn.setEnabled(true);
            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                submitBtn.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                submitBtn.setEnabled(true);
            }
        });

        itemName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                itemName.setErrorEnabled(false); // disable error
                submitBtn.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        itemDescription.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                itemDescription.setErrorEnabled(false); // disable error
                submitBtn.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        itemPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                submitBtn.setEnabled(true);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initConditionSpinner() {
        Log.d(TAG, "initConditionSpinner: initing condition spinner");
        ArrayList<String> conditions = new ArrayList<>();
        conditions.add("select_condition");
        conditions.add("new_item");
        conditions.add("used_item");


        conditionsUS = new ArrayList<>();
        conditionsUS.add("Select condition");
        conditionsUS.add("New");
        conditionsUS.add("Used");
        conditionsUS.add("opened but not used");

        //create an ArrayAdapter from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, conditions);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        condition.setAdapter(dataAdapter);
        //attach the listener to the spinner
        condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: Selected: " + conditions.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    /**
     * A method to open photo choices dialog
     */
    private void openPhotoDialog() {
        Log.d(TAG, "openPhotoDialog: ");
        //TODO: fix quality
        PhotoModeDialog photoModeDialog = new PhotoModeDialog(this);
        photoModeDialog.show();
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        photoModeDialog.getWindow().setLayout(width, RelativeLayout.LayoutParams.WRAP_CONTENT);
        photoModeDialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        photoModeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        photoModeDialog.getWindow().setDimAmount(0.9f);
    }


    /**
     * A method to initialize the item category spinner
     */
    private void initCategorySpinner() {
        Log.d(TAG, "initCategotySpinner: Initing category spinner");
        ArrayList<String> categories = new ArrayList<>();
        categories.add("select categories");
        categories.add("Surfing suit");
        categories.add("Surfboards");
        categories.add("Other");


        categoriesUS = new ArrayList<>();
        categoriesUS.add("Select categories");
        categoriesUS.add("Surfing suit");
        categoriesUS.add("Surfboards");
        categoriesUS.add("Other");


        //create an ArrayAdapter from the String Array
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categories);
        //set the view for the Drop down list
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter to the spinner
        category.setAdapter(dataAdapter);
        //attach the listener to the spinner
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemSelected: Selected: " + categories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void saveNewReview() {
        Log.d(TAG, "saveNewReview: ");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Products");
        DatabaseReference newReviewRef = myRef.push();
        Product product = new Product(itemName.getEditText().getText().toString(), categoriesUS.get((int) category.getSelectedItemId()).toString()
                , conditionsUS.get((int) condition.getSelectedItemId()).toString(), itemPrice.getText().toString(), itemDescription.getEditText().getText().toString()
                );
        newReviewRef.setValue(product);
        saveToInternalStorage(userCustomImage,product);
        Log.d(TAG, "saveNewReview: " + product.toString());
        Toast.makeText(this,"Thanks!",Toast.LENGTH_LONG).show();
        finish();

    }

    @Override
    public void photoMode(Boolean fromCamera) {

        if (fromCamera) {
            Log.d(TAG, "photoMode: Taking picture from camera");
            if (ContextCompat.checkSelfPermission(NewGiveItemActivity.this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onClick: User already given permission, moving straight to camera");
                openCamera();
            } else {
                checkingForCameraPermissions();
            }
        } else {
            Log.d(TAG, "photoMode: Fetching picture from storage");
            if (ContextCompat.checkSelfPermission(NewGiveItemActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onClick: User already given permission, moving straight to storage");
                openStorage();
            } else {
                checkingForStoragePermissions();
            }
        }
    }

    private void checkingForCameraPermissions() {


        Log.d(TAG, "checkingForCameraPermissions: checking for users permissions");

        Dexter.withContext(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.d(TAG, "onPermissionGranted: User given permission");
                        openCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Log.d(TAG, "onPermissionDenied: User denied permission permanently!");
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewGiveItemActivity.this);
                            builder.setTitle("permission_denied")
                                    .setMessage("permission_denied_explication_camera")
                                    .setNegativeButton(getString(R.string.cancel), null)
                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Log.d(TAG, "onClick: Opening settings activity");
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                            startActivityForResult(intent, CAMERA_PERMISSION_SETTINGS_REQUSETCODE);
                                        }
                                    }).show();
                        } else {
                            Log.d(TAG, "onPermissionDenied: User denied permission!");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: Im im activity result");
        switch (requestCode) {
            case CAMERA_PERMISSION_SETTINGS_REQUSETCODE:
                Log.d(TAG, "onActivityResult: I came from app settings: camera");
                break;
            case STORAGE_PERMISSION_SETTINGS_REQUSETCODE:
                Log.d(TAG, "onActivityResult: I came from app settings: storage");
                break;
            case CAMERA_PICTURE_REQUEST:
                Log.d(TAG, "onActivityResult: I came from camera");
                if (data != null) {
                    Log.d(TAG, "onActivityResult: " + data);
                    userCustomImage = (Bitmap) data.getExtras().get("data");
                    Log.d(TAG, "onActivityResult: " + userCustomImage);
                    itemPhoto.setStrokeWidth(30);
                    itemPhoto.setImageBitmap(userCustomImage);
                    submitBtn.setEnabled(true);
                }
                break;
            case STORAGE_PICTURE_REQUEST:
                Log.d(TAG, "onActivityResult: I came from storage");
                if (data != null) {
                    Uri selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        userCustomImage = bitmap;
                        itemPhoto.setStrokeWidth(30);
                        itemPhoto.setImageBitmap(bitmap);
                        submitBtn.setEnabled(true);
                    } catch (IOException e) {
                        Log.i("TAG", "Some exception " + e);
                    }
                }
                break;
        }
    }

    private void openCamera() {
        Log.d(TAG, "openCamera: opening camera");
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_PICTURE_REQUEST);

    }

    private void saveToInternalStorage(Bitmap bitmapImage,Product product) {
        Log.d(TAG, "saveToInternalStorage: ");
        final String itemID = product.getKey();
        Log.d(TAG, "saveToInternalStorage: "+itemID);
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // [START upload_create_reference]
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to "mountains.jpg"
        StorageReference mountainsRef = storageRef.child(itemID+".jpg");

        // Create a reference to 'images/mountains.jpg'
        StorageReference mountainImagesRef = storageRef.child("images/"+itemID+".jpg");
        Log.d(TAG, "saveToInternalStorage: 333");

        // While the file names are the same, the references point to different files
        mountainsRef.getName().equals(mountainImagesRef.getName());    // true
        mountainsRef.getPath().equals(mountainImagesRef.getPath());    // false
        Log.d(TAG, "saveToInternalStorage: ");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userCustomImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "onFailure: Upload failed: " + exception.getMessage());
                Toast.makeText(NewGiveItemActivity.this, getString(R.string.check_internet_connection), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "onSuccess: Image upload successful!");

            }
        });
    }


    private void checkingForStoragePermissions() {
        Log.d(TAG, "checkingForStoragePermissions: checking for users permissions");

        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        Log.d(TAG, "onPermissionGranted: User given permission");
                        openStorage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            Log.d(TAG, "onPermissionDenied: User denied permission permanently!");
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewGiveItemActivity.this);
                            builder.setTitle(getString(R.string.permission_denied))
                                    .setMessage(getString(R.string.permission_denied_explication_storage))
                                    .setNegativeButton(getString(R.string.cancel), null)
                                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Log.d(TAG, "onClick: Opening settings activity");
                                            Intent intent = new Intent();
                                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                                            startActivityForResult(intent, STORAGE_PERMISSION_SETTINGS_REQUSETCODE);
                                        }
                                    }).show();
                        } else {
                            Log.d(TAG, "onPermissionDenied: User denied permission!");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openStorage() {
        Log.d(TAG, "openStorage: opening storage");
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, STORAGE_PICTURE_REQUEST);
    }
    }


