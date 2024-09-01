package store.technologycenter.android.ui.contact;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import store.technologycenter.android.FilterActivity;
import store.technologycenter.android.R;
import store.technologycenter.android.configurations.TechCenterConfiguration;

import org.jetbrains.annotations.NotNull;

public class ContactFragment extends Fragment {
    Button websiteButton;
    Button phoneButton;
    ImageView facebookImageView;
    ImageView instagramImageView;
    ImageView whatsappImageView;
    ImageView tiktokImageView;
    TextView emailTextView;
    TextView phoneTextView;
    TextView locationTextView;
    Intent intent;
    Intent intentCall;
    Intent intentSendTo;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        setHasOptionsMenu(true);
        websiteButton = view.findViewById(R.id.website_button);
        phoneButton = view.findViewById(R.id.phone_button);
        facebookImageView = view.findViewById(R.id.facebook_image_view);
        instagramImageView = view.findViewById(R.id.instagram_image_view);
        whatsappImageView = view.findViewById(R.id.whatsapp_image_view);
        tiktokImageView = view.findViewById(R.id.tiktok_image_view);
        emailTextView = view.findViewById(R.id.email_text_view);
        phoneTextView = view.findViewById(R.id.phone_text_view);
        locationTextView = view.findViewById(R.id.location_text_view);

        websiteButton.setText(TechCenterConfiguration.WEBSITE);
        phoneButton.setText(TechCenterConfiguration.PHONE);

        emailTextView.setText(TechCenterConfiguration.EMAIL);
        phoneTextView.setText(TechCenterConfiguration.PHONE);
        locationTextView.setText(TechCenterConfiguration.LOCATION);

        intent = new Intent(Intent.ACTION_VIEW);
        websiteButton.setOnClickListener(v->{
            intent.setData(Uri.parse(TechCenterConfiguration.WEBSITE_TO_GO));
            startActivity(intent);
        });

        facebookImageView.setOnClickListener(v->{
            intent.setData(Uri.parse(TechCenterConfiguration.FACEBOOK));
            startActivity(intent);
        });
        instagramImageView.setOnClickListener(v->{
            intent.setData(Uri.parse(TechCenterConfiguration.INSTAGRAM));
            startActivity(intent);
        });
        whatsappImageView.setOnClickListener(v->{
            try{
                intent.setData(Uri.parse(TechCenterConfiguration.WHATSAPP));
                startActivity(intent);
            }catch (ActivityNotFoundException e){
                Toast.makeText(getContext(), getString(R.string.whatsapp_not_installed_error), Toast.LENGTH_SHORT).show();
            }
        });
        tiktokImageView.setOnClickListener(v->{
            intent.setData(Uri.parse(TechCenterConfiguration.TIK_TOK));
            startActivity(intent);
        });

        tiktokImageView.setOnClickListener(v->{
            intent.setData(Uri.parse(TechCenterConfiguration.TIK_TOK));
            startActivity(intent);
        });

        locationTextView.setOnClickListener(v->{
            intent.setData(Uri.parse(TechCenterConfiguration.LOCATION_TO_GO));
            startActivity(intent);
        });

        intentSendTo = new Intent(Intent.ACTION_SENDTO);
        emailTextView.setOnClickListener(v->{
            intentSendTo.setData(Uri.parse("mailto:"+TechCenterConfiguration.EMAIL));
            startActivity(intentSendTo);
        });

        intentCall = new Intent(Intent.ACTION_DIAL);
        phoneButton.setOnClickListener(v->{
            intentCall.setData(Uri.parse(TechCenterConfiguration.PHONE_TO_CALL));
            startActivity(intentCall);
        });

        phoneTextView.setOnClickListener(v->{
            intentCall.setData(Uri.parse(TechCenterConfiguration.PHONE_TO_CALL));
            startActivity(intentCall);
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter_menu_item) {
            Intent intent = new Intent(getContext(), FilterActivity.class);
            filterActivityResultLauncher.launch(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {});

}
