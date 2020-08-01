package com.icdominguez.minitwitter.ui.tweets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.icdominguez.minitwitter.R;
import com.icdominguez.minitwitter.common.Constants;
import com.icdominguez.minitwitter.common.SharedPreferencesManager;
import com.icdominguez.minitwitter.data.TweetViewModel;

public class NewTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageView ivClose, ivUserPhoto;
    Button btnTwittear;
    EditText etTweet;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_new_tweet, container, false);

        ivClose = view.findViewById(R.id.imageViewClose);
        ivUserPhoto = view.findViewById(R.id.imageViewUserPhoto);
        btnTwittear = view.findViewById(R.id.buttonTweet);
        etTweet = view.findViewById(R.id.editTextTweet);

        // Events

        ivClose.setOnClickListener(this);
        btnTwittear.setOnClickListener(this);

        String photoUrl = SharedPreferencesManager.getSomeStringValue(Constants.PREF_PHOTOURL);

        if(!photoUrl.isEmpty()){
            Glide.with(getActivity())
                    .load(Constants.API_MINITWITTER_FILES_URL + photoUrl)
                    .into(ivUserPhoto);
        } else {
            ivUserPhoto.setBackground(getResources().getDrawable(R.drawable.ic_baseline_account_circle_24));
        }

        return view;

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String message = etTweet.getText().toString();

        if(id == R.id.buttonTweet) {
            if(message.isEmpty()) {
                Toast.makeText(getActivity(),"Debe escribir un texto en el mensaje", Toast.LENGTH_SHORT).show();
            } else {
                TweetViewModel tweetViewModel = ViewModelProviders.of(getActivity()).get(TweetViewModel.class);
                tweetViewModel.insertTweet(message);
                getDialog().dismiss();
            }
        } else if(id == R.id.imageViewClose) {
            if(message != null){
                ShowDialogConfirm();
            } else {
                getDialog().dismiss();
            }

        }
    }

    private void ShowDialogConfirm() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("¿Desea realmente cancelar el tweet?").setTitle("Cancelar Tweet");
        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                getDialog().dismiss();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}