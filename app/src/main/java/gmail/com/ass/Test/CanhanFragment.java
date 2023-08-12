package gmail.com.ass.Test;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import gmail.com.ass.Dangnhap;
import gmail.com.ass.DoiMK;
import gmail.com.ass.R;

public class CanhanFragment extends Fragment {
    private static final int REQUEST_IMAGE_PICK = 100;
    private ImageView imageView, dangxuat, test;
    CircleImageView imageView1;
    private Button button;
    String test1;
    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view1 = inflater.inflate(R.layout.fragment_canhan, container, false);
        imageView1 = view1.findViewById(R.id.imgAnh);
        button = view1.findViewById(R.id.btnthemanh);
        imageView = view1.findViewById(R.id.imageView7);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DoiMK.class);
                startActivity(intent);
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        dangxuat = view1.findViewById(R.id.imageView6);
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Dangnhap.class);
                startActivity(intent);
            }
        });

        displayRoundedImage();

        return view1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImageUri);
                Bitmap roundedBitmap = getRoundedBitmap(bitmap);
                imageView1.setImageBitmap(roundedBitmap);
                saveRoundedImageToPrefs(roundedBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int radius = Math.min(width, height) / 2;

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(width / 2f, height / 2f, radius, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }


    // Lưu ảnh dưới dạng byte array
    // Lưu đường dẫn tới ảnh tròn vào SharedPreferences
    private void saveRoundedImageToPrefs(Bitmap bitmap) {
        File appDir = requireContext().getFilesDir();
        String imageName = "profile_image_rounded.jpg";
        File imageFile = new File(appDir, imageName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            Bitmap roundedBitmap = getRoundedBitmap(bitmap);
            roundedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("roundedImagePath", imageFile.getAbsolutePath());
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hiển thị ảnh tròn từ SharedPreferences
    private void displayRoundedImage() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String roundedImagePath = sharedPreferences.getString("roundedImagePath", "");

        if (!roundedImagePath.isEmpty()) {
            Bitmap roundedBitmap = BitmapFactory.decodeFile(roundedImagePath);
            if (roundedBitmap != null) {
                imageView1.setImageBitmap(roundedBitmap);
            }
        }

    }


}
