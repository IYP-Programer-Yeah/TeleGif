package ir.telegif.telegif;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.github.amlcurran.showcaseview.ShowcaseView;

import java.util.ArrayList;

public class CategoryMenu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static public Downloader updateStat;

    private Toolbar toolbar;
    public static ShowcaseView SV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CategoryMenu.SV=null;
        System.gc();
        if (updateStat!=null)
            if (updateStat.getStatus()== AsyncTask.Status.PENDING)
                updateStat.execute();

        super.onCreate(savedInstanceState);
        setContentView(SettingsActivity.activity_cat_menu_res[SettingsActivity.lang]);



        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(SettingsActivity.CatMenuTitle[SettingsActivity.lang]);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArrayList<String> CatList[]=new ArrayList[SettingsActivity.langs.length];
        for (int i=0;i<CatList.length;i++)
            CatList[i] = new ArrayList<String>();

        CatList[SettingsActivity.eng].add("Reaction");
        CatList[SettingsActivity.eng].add("Adjectives");
        CatList[SettingsActivity.eng].add("Actions");
        CatList[SettingsActivity.eng].add("Cartoon");
        CatList[SettingsActivity.eng].add("Animals");
        CatList[SettingsActivity.eng].add("Art & Design");
        CatList[SettingsActivity.eng].add("News & Politics");
        CatList[SettingsActivity.eng].add("Sports");
        CatList[SettingsActivity.eng].add("Transportation");
        CatList[SettingsActivity.eng].add("Memes");
        CatList[SettingsActivity.eng].add("Football Team");
        CatList[SettingsActivity.eng].add("Game of Thrones");

        CatList[SettingsActivity.fa].add("واکنش ها");
        CatList[SettingsActivity.fa].add("حالات");
        CatList[SettingsActivity.fa].add("فعالیت ها");
        CatList[SettingsActivity.fa].add("کارتون");
        CatList[SettingsActivity.fa].add("حیوانات");
        CatList[SettingsActivity.fa].add("هنر");
        CatList[SettingsActivity.fa].add("سیاست");
        CatList[SettingsActivity.fa].add("ورزش ها");
        CatList[SettingsActivity.fa].add("خودرو");
        CatList[SettingsActivity.fa].add("میمز");
        CatList[SettingsActivity.fa].add("تیم های فوتبال");
        CatList[SettingsActivity.fa].add("گیم آف ترونز");

        CategoryCustomAdapter AdapterCategory;
        GridView CategoryGrid = (GridView) findViewById(R.id.GVCat);

        final String[] CatNames=new String[CatList[SettingsActivity.lang].size()];
        for (int i=0;i<CatList[SettingsActivity.lang].size();i++)
            CatNames[i]=CatList[SettingsActivity.lang].get(i);

        final int[] resources={R.drawable.tele_gif_reaction,R.drawable.tele_gif_ajectives,R.drawable.tele_gif_actions,R.drawable.tele_gif_cartoon,R.drawable.tele_gif_animals,R.drawable.tele_gif_art,R.drawable.tele_gif_politics,R.drawable.tele_gif_sports,R.drawable.tele_gif_transportations,R.drawable.tele_gif_memes,R.drawable.tele_gif_football,R.drawable.tele_gif_game_of_thrones};

        AdapterCategory = new CategoryCustomAdapter(this, R.layout.category_row, CategoryGrid, CatNames, resources);

        CategoryGrid.setAdapter(AdapterCategory);


        CategoryGrid.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(CategoryMenu.this, SubCategory.class);
                        intent.putExtra("catID", position);
                        intent.putExtra("CatName", CatNames[position]);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tele_gif, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            Intent setting = new Intent(CategoryMenu.this,SettingsActivity.class);
            startActivity(setting);
            finish();
        } else if (id == R.id.nav_about) {
            Intent about = new Intent(CategoryMenu.this,AboutActivity.class);
            startActivity(about);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}