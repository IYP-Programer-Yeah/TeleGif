package ir.telegif.telegif;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Created by livewin8.1.com on 4/24/2016.
 */
public class SubCategoryCustomAdapter extends ArrayAdapter<String> {

    private Context context;
    private int resource;
    private ListView listView;
    private int[] imageID;
    private String[] imageName;
    private View[] views;


    public SubCategoryCustomAdapter(final Context context,final int resource,final ListView listView,final int[] imageID,final  String[] imageName) {
        super(context,resource,imageName);

        this.context = context;
        this.resource = resource;
        this.listView = listView;
        this.imageID = imageID;
        this.imageName = imageName;
        views=new View[imageName.length];
        for (int i=0;i<imageName.length;i++){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View single_row = inflater.inflate(resource, null,
                    true);

            TextView textView = (TextView) single_row.findViewById(R.id.SubCatTV);

            textView.setText(imageName[i]);

            ImageView imageView = (ImageView) single_row.findViewById(R.id.SubCatIV);
            imageView.setImageResource(imageID[i]);
            views[i] = single_row;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (TeleGif.subCatFR&&SubCategory.SV==null) {
            ViewTarget t = new ViewTarget(views[position].findViewById(R.id.SubCatIV));

            SubCategory.SV = new ShowcaseView.Builder((Activity)context)
                    .setTarget(t)
                    .setContentTitle("زیر گروه ها")
                    .setContentText("با زدن بر روی هر زیر گروه وارد آن شوید.")
                    .build();

            SubCategory.SV.hideButton();
            SubCategory.SV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SubCategory.SV.hide();
                    SubCategory.SV.setClickable(false);
                    SubCategory.SV.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(context, GifMenu.class);
                    intent.putExtra("SubCatID", 1);
                    intent.putExtra("CatID", 1);
                    intent.putExtra("SubCatName", "موافقم");
                    context.startActivity(intent);
                    TeleGif.subCatFR=false;
                }
            });
        }
        return views[position];
    }
}
