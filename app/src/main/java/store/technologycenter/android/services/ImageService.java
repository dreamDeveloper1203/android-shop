package store.technologycenter.android.services;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import store.technologycenter.android.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageService {

    public static int DEFAULT_WIDTH = 600;
    public static int DEFAULT_HEIGHT = 600;
    public static int WIDTH_SM = 200;
    public static int HEIGHT_SM = 200;


    public static void setImage(String imageUrl, ImageView imageView) {
        if (imageUrl.isEmpty()) {
            imageView.setBackgroundResource(getDefaultImage());
            return;
        }
        picasso(imageUrl, imageView);
    }

    public static void setImage(String imageUrl, ImageView imageView, int width, int height) {
        if (imageUrl.isEmpty()) {
            imageView.setBackgroundResource(getDefaultImage());
            return;
        }
        try {
            picasso(imageUrl, imageView, width, height);
        } catch (Exception exception) {
            picasso(imageUrl, imageView);
        }
    }

    public static void setImage(String imageUrl, ImageView imageView, int width, int height, Callback callback) {
        if (imageUrl.isEmpty()) {
            imageView.setBackgroundResource(getDefaultImage());
            return;
        }
        try {
            picasso(imageUrl, imageView, width, height, callback);
        } catch (Exception exception) {
            picasso(imageUrl, imageView, callback);
        }
    }


    public static void showDialog(Context context, String image, DisplayMetrics displayMetrics) {
        Dialog dialog = new Dialog(context, R.style.Bottom_Sheet_Dialog_Theme);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_item_image);
        ImageButton btnClose = dialog.findViewById(R.id.btn_close);
        TextView tvLoading = dialog.findViewById(R.id.tv_loading);
        ImageView imageView = dialog.findViewById(R.id.iv_item);
        int width = displayMetrics.widthPixels;
        int height = 0;
        Callback callback = dialogCallback(tvLoading);
        setImage(image, imageView, width, height, callback);
        btnClose.setOnClickListener(arg -> dialog.dismiss());
        dialog.setOnCancelListener(d -> dialog.dismiss());
        dialog.show();
    }

    private static Callback dialogCallback(TextView textView) {
        return new Callback() {
            @Override
            public void onSuccess() {
                textView.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                textView.setVisibility(View.GONE);
            }

        };
    }

    private static void picasso(String imageUrl, ImageView imageView, int width, int height, Callback callback) {
        Picasso.get().load(imageUrl)
                .error(getDefaultImage())
                .resize(width, height)
                .into(imageView, callback);
    }

    private static void picasso(String imageUrl, ImageView imageView, Callback callback) {
        Picasso.get().load(imageUrl)
                .error(getDefaultImage())
                .resize(DEFAULT_WIDTH, DEFAULT_HEIGHT)
                .into(imageView, callback);
    }

    private static void picasso(String imageUrl, ImageView imageView, int width, int height) {
        Picasso.get().load(imageUrl)
                .error(getDefaultImage())
                .resize(width, height)
                .into(imageView);
    }

    private static void picasso(String imageUrl, ImageView imageView) {
        Picasso.get().load(imageUrl)
                .error(getDefaultImage())
                .resize(DEFAULT_WIDTH, DEFAULT_HEIGHT)
                .into(imageView);
    }

    private static int getDefaultImage(){
        return R.drawable.tech_center_logo;
    }
}
