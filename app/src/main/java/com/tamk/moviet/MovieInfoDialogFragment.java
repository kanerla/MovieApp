package com.tamk.moviet;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

public class MovieInfoDialogFragment extends DialogFragment {

    private TextView title;
    private TextView original_title;
    private TextView release;
    private TextView length;
    private TextView genres;
    private TextView synopsis;
    private TextView link;
    private Button watchList;
    private Button seenButton;
    private ImageView photo;
    private Event thisEvent;
    private MovieViewModel movieViewModel;

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
        release = view.findViewById(R.id.release);
        length = view.findViewById(R.id.length);
        genres = view.findViewById(R.id.genres);
        synopsis = view.findViewById(R.id.synopsis);
        synopsis.setMovementMethod(new ScrollingMovementMethod());
        link = view.findViewById(R.id.link);

        movieViewModel = ViewModelProviders.of(requireActivity()).get(MovieViewModel.class);

        watchList = view.findViewById(R.id.watchButton);
        seenButton = view.findViewById(R.id.seenButton);

        Bundle mArgs = getArguments();
        thisEvent = mArgs.getParcelable("movie");

        //if (thisEvent.getPhoto() != null)
        Picasso.with(getContext()).load(thisEvent.getPhoto()).fit().error(R.drawable.placeholder).into(photo);
        title.setText(thisEvent.getTitle());
        synopsis.setText(thisEvent.getSummary());
        original_title.setText(thisEvent.getOriginalTitle());

        String releaseTime = thisEvent.getRelease();
        String[] times = releaseTime.split("T", 2);
        String releaseDate = times[0];
        String[] dates = releaseDate.split("-", 5);

        release.setText("Release Date: " + dates[2] + "/" + dates[1] + "/" + dates[0]);

        length.setText("Length: " + thisEvent.getLength());
        genres.setText(thisEvent.getGenres());
        link.setClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());
        String linkText = "<a href='" + thisEvent.getLink() + "'> See showtimes and buy tickets </a>";
        link.setText(HtmlCompat.fromHtml(linkText, HtmlCompat.FROM_HTML_MODE_LEGACY));

        watchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Watch", "button was clicked");
                movieViewModel.insert(thisEvent);
            }
        });

        seenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Seen", "button was clicked");
                movieViewModel.insert(thisEvent);
                thisEvent.setSeen(true);
            }
        });
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
