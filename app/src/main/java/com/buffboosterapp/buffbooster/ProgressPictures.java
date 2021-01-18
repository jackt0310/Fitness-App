package com.buffboosterapp.buffbooster;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class ProgressPictures extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private static final String FILE_NAME = "pictures.txt";
    ContentValues values;
    Uri imageUri;

    ArrayList<String> fileNames = new ArrayList<String>();
    ArrayList<String> paths = new ArrayList<String>();
    ArrayList<LocalDateTime> dates = new ArrayList<LocalDateTime>();
    ProgressPicAdapter entryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_pictures);

        Button takePic = (Button) findViewById(R.id.buttonTakePic);

        takePic.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });

        RecyclerView recycler = findViewById(R.id.pictureList);
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        load();
        entryList = new ProgressPicAdapter(this, fileNames, paths, dates);
        recycler.setAdapter(entryList);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage, String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    private Bitmap loadImageFromStorage(String path, String fileName)
    {

        try {
            File f=new File(path, fileName);
            return BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            //img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;

    }

    private void dispatchTakePictureIntent() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        //startActivityForResult(intent, PICTURE_RESULT);

        try {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    public void save() {
        try
        {
            File file = new File(getFilesDir() + FILE_NAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(fileNames);
            oos.writeObject(paths);
            oos.writeObject(dates);
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void load() {
        try {
            FileInputStream fos = new FileInputStream(getFilesDir() + FILE_NAME);
            ObjectInputStream oos = new ObjectInputStream(fos);
            fileNames = (ArrayList<String>) oos.readObject();
            paths = (ArrayList<String>) oos.readObject();
            dates = (ArrayList<LocalDateTime>) oos.readObject();
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case REQUEST_IMAGE_CAPTURE:
                if (requestCode == REQUEST_IMAGE_CAPTURE) {
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);

                            LocalDateTime date = LocalDateTime.now();
                            String fileName = "Image_" + entryList.getCount();
                            String path = saveToInternalStorage(imageBitmap, fileName);

                            entryList.add(fileName, path, date);
                            entryList.notifyDataSetChanged();
                            save();
                            System.out.println(entryList.getCount());
                            /*
                            ImageView imageView = new ImageView(ProgressPictures.this);

                            imageView.setImageBitmap(imageBitmap);



                            LinearLayout linLayout = (LinearLayout) findViewById(R.id.pictureList);
                            linLayout.addView(imageView);

                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                            imageView.setAdjustViewBounds(true);
                            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT));

                             */
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
        }
    }
}
