package ir.telegif.telegif;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.lang.annotation.Target;

/**
 * Created by livewin8.1.com on 4/24/2016.
 */
public class CategoryCustomAdapter extends ArrayAdapter<String> {

    Context context;
    int resource;
    GridView gridView;
    String[] Names;
    View[] views;
    TextView[] text;
    ImageView[] image;
    int[] resources;


    public CategoryCustomAdapter(Context context,int resource,GridView gridView, String[] Names, int[] resources) {

        super(context, resource, Names);

        this.context = context;
        this.resource = resource;
        this.gridView = gridView;
        this.Names = Names;
        this.resources=resources;

        views=new View[this.Names.length];
        text=new TextView[this.Names.length];
        image=new ImageView[this.Names.length];


        for (int j=0;j<this.Names.length;j++){
            int position=j;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View single_row = inflater.inflate(resource, null, true);
            image[position]=(ImageView) single_row.findViewById(R.id.CatIV);

            image[position].setImageResource(resources[position]);

            text[position] = (TextView) single_row.findViewById(R.id.CatTV);

            text[position].setText(this.Names[position]);

            //text[position].setLayoutParams(new RelativeLayout.LayoutParams(gridView.getWidth()/2,RelativeLayout.LayoutParams.WRAP_CONTENT));

            image[position].setLayoutParams(new RelativeLayout.LayoutParams(gridView.getWidth()/2,gridView.getWidth()/2));
            views[position]=single_row;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        //text[position].setLayoutParams(new RelativeLayout.LayoutParams(gridView.getWidth()/2,RelativeLayout.LayoutParams.WRAP_CONTENT));

        image[position].setLayoutParams(new RelativeLayout.LayoutParams(gridView.getWidth()/2,gridView.getWidth()/2));

        if (TeleGif.catFR&&position==0) {
            ViewTarget t = new ViewTarget(image[position]);

            if (CategoryMenu.SV==null) {

                CategoryMenu.SV = new ShowcaseView.Builder((Activity) context)
                        .setTarget(t)
                        .setContentTitle("گروه بندی")
                        .setContentText("با زدن بر روی هر گروه وارد آن شوید.")
                        .build();

                CategoryMenu.SV.hideButton();
                CategoryMenu.SV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CategoryMenu.SV.hide();
                        CategoryMenu.SV.setClickable(false);
                        CategoryMenu.SV.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(context, SubCategory.class);
                        intent.putExtra("catID", 0);
                        intent.putExtra("CatName", "واکنش ها");
                        context.startActivity(intent);
                        TeleGif.catFR = false;
                    }
                });
            }

        }

        return views[position];
    }

}
