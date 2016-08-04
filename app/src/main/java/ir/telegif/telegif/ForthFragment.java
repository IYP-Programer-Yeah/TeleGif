package ir.telegif.telegif;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ForthFragment extends Fragment {
    private int page;
    public static View view=null;

    public ForthFragment() {
    }

    // newInstance constructor for creating fragment with arguments
    public static ForthFragment newInstance(int page) {
        ForthFragment fragmentForth = new ForthFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        fragmentForth.setArguments(args);
        return fragmentForth;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view==null) {
            view = inflater.inflate(R.layout.fragment_forth, container, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.ForthImg);

            DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();

            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;

            int IMGWidthArr[] = {1080, 1920};
            int IMGHeightArr[] = {1920, 1080};

            int Img;

            if (width > height) {
                Img = 1;
                imageView.setImageResource(R.drawable.viewpager4h);
            } else {
                Img = 0;
                imageView.setImageResource(R.drawable.viewpager4);
            }

            int IMGWidth = IMGWidthArr[Img], IMGHeight = IMGHeightArr[Img];
            if (((float) height / (float) IMGHeight) > ((float) width / (float) IMGWidth)) {
                imageView.setLayoutParams(new FrameLayout.LayoutParams((int) ((float) IMGWidth * (float) height / ((float) IMGHeight)),
                        height)
                );
                imageView.setPadding((width - (int) ((float) IMGWidth * (float) height / ((float) IMGHeight))), 0, 0, 0);
            } else {
                imageView.setLayoutParams(new FrameLayout.LayoutParams(width,
                        (int) ((float) IMGHeight * (float) width / ((float) IMGWidth)))
                );
                imageView.setPadding(0, (height - (int) ((float) IMGHeight * (float) width / ((float) IMGWidth))), 0, 0);
            }
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.context, CategoryMenu.class);
                    startActivity(intent);
                    ((Activity) MainActivity.context).finish();
                }
            });
        }
        return view;
    }
}