package com.startup.threecat.sherlock.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.startup.threecat.sherlock.R;
import com.startup.threecat.sherlock.databasehelper.DatabaseUtil;
import com.startup.threecat.sherlock.dialog.ChooseImageDialog;
import com.startup.threecat.sherlock.model.ConstantData;
import com.startup.threecat.sherlock.model.Movement;
import com.startup.threecat.sherlock.model.Person;
import com.startup.threecat.sherlock.util.LoadImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class AddPersonActivity extends AppCompatActivity {

    private ImageView imgAddPerson;
    private EditText edtName;
    private EditText edtAge;
    private EditText edtHeight;
    private EditText edtHairColor;
    private EditText edtAddress;
    private EditText edtAddComment;

    private RadioButton rbtMale;
    private RadioButton rbtFemale;
    private Button btnSave;
    private Button btnReturn;

    private String mCurrentPhotoPath = null;
    private int typeImage = 0;

    public static final int PICK_IMAGE_FROM_GALLERY = 1;
    public static final int TAKE_PHOTO = 2;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 3;
    public static final int TAKE_PHOTO_PERMISSIONS = 4;;
    private boolean isAdd = false;
    private ArrayList<Person> listPersonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        listPersonAdd = new ArrayList<>();
        initView();
    }

    public void initView() {
        imgAddPerson = (ImageView)findViewById(R.id.imgAddPerson);
        edtName = (EditText)findViewById(R.id.edtInputName);
        edtAge = (EditText)findViewById(R.id.edtInputAge);
        edtHeight = (EditText)findViewById(R.id.edtInputHeight);
        edtHairColor = (EditText)findViewById(R.id.edtInputHairColor);
        edtAddress = (EditText)findViewById(R.id.edtAddress);
        edtAddComment = (EditText)findViewById(R.id.edtAdditionalComment);

        rbtMale = (RadioButton)findViewById(R.id.rbtMale);
        rbtFemale = (RadioButton)findViewById(R.id.rbtFemale);

        btnReturn = (Button)findViewById(R.id.btnReturn);
        btnSave = (Button)findViewById(R.id.btnSave);

        imgAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAddImagePerson();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSaveInfo();
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleReturn();
                AddPersonActivity.this.finish();
            }
        });

    }

    private void handleAddImagePerson() {

        ChooseImageDialog dialog = new ChooseImageDialog();
        dialog.setListener(new ChooseImageDialog.ChooseImageDialogListener() {
            @Override
            public void onDialogNegativeClick(android.support.v4.app.DialogFragment dialog) {
                dialog.dismiss();
            }

            @Override
            public void onDialogItemClick(android.support.v4.app.DialogFragment dialog, int position) {
                if(position == ChooseImageDialog.PICK_FROM_GALLERY) {
                    pickImageFromGallery();
                }else {
                    takePhoto();
                }
            }
        });
        dialog.show(getSupportFragmentManager(), "ChooseImage");
    }

    /**
     * pick image from gallery and save image into file
     * check permission on runtime
     */
    private void pickImageFromGallery() {

        if (Build.VERSION.SDK_INT >= 23){
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(AddPersonActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(AddPersonActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(AddPersonActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }else{
                ActivityCompat.requestPermissions(AddPersonActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            }
        }else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_GALLERY);
        }
    }

    /**
     * take Photo and save image into file
     */
    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Can't save image", Toast.LENGTH_LONG).show();
            }

            if (photoFile != null) {
                Uri phoURI = FileProvider.getUriForFile(
                        this,
                        "com.startup.threecat.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, phoURI);
                startActivityForResult(takePictureIntent,TAKE_PHOTO);
            }
        }else {
            Toast.makeText(this,
                    "Your device can't start camera, please check camera app",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_FROM_GALLERY
                && resultCode == RESULT_OK
                && data != null) {

            Uri selectedImage = data.getData();
            String [] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            LoadImage.loadImagePerson(this, imgAddPerson, picturePath);
            mCurrentPhotoPath = picturePath;
            typeImage = PICK_IMAGE_FROM_GALLERY;
        }else if(requestCode == TAKE_PHOTO
                && resultCode == RESULT_OK) {

            LoadImage.loadImagePerson(this, imgAddPerson, mCurrentPhotoPath);
            typeImage = TAKE_PHOTO;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_GALLERY);
                } else {
                    Toast.makeText(this, "Can't not pick image from gallery", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case TAKE_PHOTO_PERMISSIONS : {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LoadImage.loadImagePerson(this, imgAddPerson, mCurrentPhotoPath);
                } else {
                    Toast.makeText(this, "Can't not pick image from gallery", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    private File createImageFile() throws IOException {

        // create an image file name
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath =  image.getAbsolutePath();
        return image;
    }

    private void saveImagePickFromGallery(String pathFromFile) {

        try{
            String desFileName = "JPEG_" + System.currentTimeMillis() +".jpg";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File desFile = new File(storageDir, desFileName);
            File fromFile = new File(pathFromFile);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(fromFile));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(desFile));
            mCurrentPhotoPath = desFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSaveInfo() {

        String name = edtName.getText().toString().trim();
        String sAge = edtAge.getText().toString().trim();
        String sHeight = edtHeight.getText().toString().trim();
        String hairColor = edtHairColor.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String comment = edtAddComment.getText().toString().trim();
        int gender = Person.MALE;
        String sGender = "Male";
        int age;
        float height;
        if(rbtFemale.isChecked()) {
            gender = Person.FEMALE;
            sGender = "Female";
        }
        if(checkEmpty(name, sAge, sHeight)) {

            age = Integer.parseInt(sAge);
            height = Float.parseFloat(sHeight);

            String id = System.currentTimeMillis() + "";
            ArrayList<Movement> movements = new ArrayList<>();
            Person person = new Person(id, name, age, height,
                    gender, hairColor, address, comment, movements);
            showConfirm(person, sGender);
        }

    }

    public boolean checkEmpty(String name, String sAge, String sHeight) {

        boolean check = true;
        ArrayList<EditText> editTexts = new ArrayList<>();
        String error = "Required Value !";
        if(name.equals("")) {
            edtName.setError(error);
            editTexts.add(edtName);
            check = false;
        }
        if(sAge.equals("")) {
            edtAge.setError(error);
            editTexts.add(edtAge);
            check = false;

        }
        if(sHeight.equals("")) {
            edtHeight.setError(error);
            editTexts.add(edtHeight);
            check = false;
        }

        if(editTexts.size() != 0) {
            editTexts.get(0).requestFocus();
        }
        return check;
    }

    public void showConfirm(final Person person, String sGender) {
        String infoComfirm = "Person name : " + person.getName() + "\n" +
                "Person Age : " + person.getAge() + "\n" +
                "Person Height : " + person.getHeight() + "(cm)\n" +
                "Person Gender : " + sGender ;

        AlertDialog.Builder  builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Person Information");

        builder.setMessage(infoComfirm);
        builder.setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveInfo(person);
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    public void saveInfo(final Person person) {

        final ProgressDialog progressDialog = new ProgressDialog(AddPersonActivity.this);
        progressDialog.setMessage("Saving...");
        progressDialog.setIndeterminate(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setProgress(0);
        progressDialog.show();
        isAdd = false;
        new AsyncTask<Void, Integer, Void>() {

            @Override
            protected Void doInBackground(Void... params) {

                if(typeImage == PICK_IMAGE_FROM_GALLERY) {
                    saveImagePickFromGallery(mCurrentPhotoPath);
                }
                publishProgress(40);
                person.setUrlImage(mCurrentPhotoPath);
                isAdd = DatabaseUtil.addPerson(AddPersonActivity.this, person);
                publishProgress(80);
                if(isAdd) {
                    listPersonAdd.add(person);
                }
                publishProgress(90);
                SystemClock.sleep(200);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progressDialog.cancel();
                if(isAdd) {
                    Toast.makeText(AddPersonActivity.this, "Successful", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(AddPersonActivity.this, "Can't save info", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                progressDialog.setProgress(values[0]);
            }
        }.execute();

    }

    public void handleReturn() {
        Intent intent = AddPersonActivity.this.getIntent();
        Bundle data = new Bundle();
        data.putSerializable(ConstantData.RESULT_ADD_PERSON, listPersonAdd);
        intent.putExtra(ConstantData.PACKAGE, data);
        AddPersonActivity.this.setResult(ConstantData.RESULT_CODE_ADD_PERSON, intent);
    }

    @Override
    public void onBackPressed() {
        handleReturn();
        super.onBackPressed();
    }
}
