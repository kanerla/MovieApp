package com.example.xmlparser;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;

public class MovieInfoDialogFragment extends DialogFragment {

    TextView title;
    TextView original_title;
    TextView length;
    TextView genres;
    TextView synopsis;
    TextView link;
    ImageView photo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.movieinfo_fragment, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        photo = view.findViewById(R.id.picture);
        title = view.findViewById(R.id.title);
        original_title = view.findViewById(R.id.original_title);
        length = view.findViewById(R.id.length);
        genres = view.findViewById(R.id.genres);
        synopsis = view.findViewById(R.id.synopsis);
        synopsis.setMovementMethod(new ScrollingMovementMethod());
        link = view.findViewById(R.id.link);

        Bundle mArgs = getArguments();

        if (mArgs.getString("photo") != null) {
            Picasso.with(getContext()).load(mArgs.getString("photo")).fit().error(R.drawable.placeholder).into(photo);
        }

        title.setText(mArgs.getString("title"));
        synopsis.setText(mArgs.getString("synopsis"));
        original_title.setText(mArgs.getString("original"));
        length.setText("Length: " + mArgs.getString("length"));
        genres.setText(mArgs.getString("genres"));
        link.setClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='" + mArgs.getString("link") + "'> See showtimes and buy tickets </a>";
        link.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }
}
