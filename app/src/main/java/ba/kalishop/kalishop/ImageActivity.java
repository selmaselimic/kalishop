package ba.kalishop.kalishop;

import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ba.kalishop.kalishop.web.ApiInterface;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class ImageActivity extends AppCompatActivity {

    private ImageView ivFile;
    private RelativeLayout rlFile;
    private ImageView ivClose;

    private TextView tvFollowupQuestion;
    private EditText etFollowupAnswer;

    private RelativeLayout rlUploadImage;
    private TextView tvFilename;
    private ImageView ivAddFile, ivRemoveFile;


    public File file;
    private Intent data;
    int REQUEST_CAMERA = 0, SELECT_PHOTO = 1, SELECT_FILE = 2;
    int imageWidth = 400, imageHeight = 400;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);



        // File uploads (Images, Videos, etc..)
        rlFile = (RelativeLayout) findViewById(R.id.rlFile);
        ivFile = (ImageView) findViewById(R.id.ivFile);

        rlUploadImage = (RelativeLayout) findViewById(R.id.rlUploadImage);
        rlUploadImage.setVisibility(View.VISIBLE);
        tvFilename = (TextView) findViewById(R.id.tvUploadFile);
        ivClose = (ImageView) findViewById(R.id.ivClose);

        rlFile.setVisibility(View.GONE);
        rlUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              selectImage();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivFile.setImageDrawable(null);
                file = null;
                rlFile.setVisibility(View.GONE);
                rlUploadImage.setVisibility(View.VISIBLE);
            }
        });
    }


    private void selectImage() {

        // TODO: TESTING
        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        //intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Filter to show only images, using the image MIME data type.
        // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
        // To search for all documents available via installed storage providers,
        // it would be "*/*".
        //intent.setType("image/*");
        //startActivityForResult(intent, READ_REQUEST_CODE);
        // TODO: TESTING

        // TODO: REMOVE "false" to make code work
        if (this.file == null) {
            final CharSequence[] items = {"Take photo", "Choose photo", "Choose file",
                    "Cancel" };
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setTitle("Add photo");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (item == 0) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        //Toast.makeText(getApplicationContext(), R.string._cannot_take_photos_yet, Toast.LENGTH_LONG).show();

                        startActivityForResult(intent, REQUEST_CAMERA);
                    } else if (item == 1) {
                        //userChoosenTask="Choose from Library";
                        Intent intent = new Intent();
                        //intent.setType("*/*");
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        //ovaj dio meni treba,testirat se mora
                        //Toast.makeText(getApplicationContext(), R.string._cannot_upload_photos_yet, Toast.LENGTH_LONG).show();
                        startActivityForResult(Intent.createChooser(intent, "Select Photo"), SELECT_PHOTO); // COMMENT THIS BACK IN !!!!
                    } else if (item == 2) {
                        //userChoosenTask="Choose from Library";
                        Intent intent = new Intent();
                        intent.setType("*/*");
                        //intent.setType("image/*");

                        //Toast.makeText(getApplicationContext(), R.string._cannot_upload_files_yet, Toast.LENGTH_LONG).show();
                        intent.setAction(Intent.ACTION_GET_CONTENT);//
                        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                    } else {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        } else {
            this.file = null;
            tvFilename.setText("Upload file");
            this.ivAddFile.setVisibility(View.VISIBLE);
            this.ivRemoveFile.setVisibility(View.GONE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == SELECT_FILE || requestCode == SELECT_PHOTO) {
                File file = FileManager.getFileByUri(this, data.getData());
                //if (file.exists()) {
                this.file = file;
                this.data = data;
                //ovdje bih trebala pozvat api
                //RequestBody mFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                Uri imageUri=data.getData();//dodala poslje treba testirat
                String image=getRealPathFromUri(imageUri);//dodala treba testirat

                //RequestBody mFile = RequestBody.create(MediaType.parse("image/*"), file);
               // MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
                //RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

                RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
                MultipartBody.Part body=MultipartBody.Part.createFormData("file",file.getName(),requestBody);
                loadFileOrImagePreview();


                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://192.168.2.102:62225")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                ApiInterface uploadImage = retrofit.create(ApiInterface.class);
                Call<UploadObject> fileUpload = uploadImage.uploadFile(body);
                fileUpload.enqueue(new Callback<UploadObject>() {
                    @Override
                    public void onResponse(Call<UploadObject> call, Response<UploadObject> response) {
                        Toast.makeText(ImageActivity.this, "Response " + response.raw().message(), Toast.LENGTH_LONG).show();
                        Toast.makeText(ImageActivity.this, "Success " + response.body().getSuccess(), Toast.LENGTH_LONG).show();

                    }
                    @Override
                    public void onFailure(Call<UploadObject> call, Throwable t) {
                        Toast.makeText(ImageActivity.this, "Error" + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
                //ovdje zavrsava sto sam kopirala




                /*} else {
                    Toast.makeText(this, getString(R.string.fileupload_could_not_find_file), Toast.LENGTH_LONG).show();
                }*/
            }
            else if(requestCode == REQUEST_CAMERA){
                File file = FileManager.getFileByUri(this, data.getData());
                Bitmap bitmap1=(Bitmap)data.getExtras().get("data");
                cropImage(bitmap1);
                scaleImage(bitmap1,imageWidth,imageHeight);
                ivFile.setImageBitmap(bitmap1);
                displayImage(bitmap1);
                this.file= bitmapToFile(bitmap1,"CameraPhoto.jpg");





            }


        }
    }

    private String getRealPathFromUri(Uri uri){

        String []projection={MediaStore.Images.Media.DATA};
        CursorLoader loader=new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor=loader.loadInBackground();
        int column_idx=cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result=cursor.getString(column_idx);
        cursor.close();
        return result;

    }

    private void loadFileOrImagePreview() {
        if (file != null) {
            String imageRegex = "(.*/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP)$";
            if (file.getName().matches(imageRegex)) {
                Log.d("onActivityResult", "previewImage: " + file.getAbsolutePath());
                Log.d("onActivityResult", "previewImage: " + data.getType());
                previewImage(file, data);
            } else {
                Log.d("onActivityResult", "displayFile");
                displayFile(file);


            }
        }
    }

    private void previewImage(File file, Intent data) {
        Log.d("onActivityResult", "previewImage: ");
        Bitmap bGallery = onSelectFromGalleryResult(data);
        Bitmap cropped = cropImage(bGallery);
        Bitmap scaled = scaleImage(cropped, imageWidth, imageHeight);
        Bitmap bOrientation = fixImageOrientation(file.getPath(), scaled);
        displayImage(bOrientation);
        this.file = bitmapToFile(bOrientation, file.getPath());

    }

    public Bitmap cropImage(Bitmap bitmap) {
        if (bitmap.getWidth() >= bitmap.getHeight()){
            return Bitmap.createBitmap(bitmap, bitmap.getWidth()/2 - bitmap.getHeight()/2, 0, bitmap.getHeight(), bitmap.getHeight());
        }else{
            return Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2 - bitmap.getWidth()/2, bitmap.getWidth(), bitmap.getWidth());
        }
    }

    public void displayImage(final Bitmap bitmap) {
        rlFile.setVisibility(View.VISIBLE);
        rlUploadImage.setVisibility(View.GONE);
        ivFile.setImageBitmap(bitmap);
        ivFile.invalidate();
    }

    public void displayFile(final File file) {
        this.file = file;
        rlFile.setVisibility(View.GONE);
        ivAddFile.setVisibility(View.GONE);
        ivRemoveFile.setVisibility(View.VISIBLE);
        if (file == null) {
            tvFilename.setText("File is null!!");
        } else {
            tvFilename.setText(file.getPath());
        }
    }

    private Bitmap onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            /*
            try {
                //bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
            bm = decodeSampledBitmapFromResource(data.getData(), imageWidth, imageHeight); //decodeSampledBitmapFromResource(data.getData())
        }

        return bm;
    }

    public Bitmap decodeSampledBitmapFromResource(Uri uri, int reqWidth, int reqHeight) {
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inDither = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
        return null;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap scaleImage(Bitmap bitmap, int width, int height) {
        return bitmap;//Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    public Bitmap fixImageOrientation(String filePath, Bitmap bitmap) {
        try {
            ExifInterface ei = new ExifInterface(filePath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

            switch(orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    Log.d("ROTATE", "90");
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    Log.d("ROTATE", "180");
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    Log.d("ROTATE", "270");
                    bitmap = rotateImage(bitmap, 270);
                    break;
                // etc.
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return bitmap;
    }

    public File bitmapToFile(Bitmap bitmap, String filePath) {
        // Upload image
        try {
            Log.d("saveImage", "try 1");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();

            String fileName = filePath;
            if (fileName.contains("/")) {
                fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            }



            FileOutputStream fos = openFileOutput(fileName/*"test_image_name.png"*/, Context.MODE_WORLD_WRITEABLE);
            fos.write(bArr);
            fos.flush();
            fos.close();
            Log.d("FILE_UPLOOOAD", "getFilesDir().getAbsolutePath(): " + getFilesDir().getAbsolutePath());
            Log.d("FILE_UPLOOOAD", "getFilesDir().getName(): " + getFilesDir().getName());
            Log.d("FILE_UPLOOOAD", "getFilesDir().getPath(): " + getFilesDir().getPath());
            File mFile = new File(getFilesDir().getAbsolutePath(), fileName/*"test_image_name.png"*/);
            Log.d("saveImage", "try 2");
            return mFile;



        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }







}
