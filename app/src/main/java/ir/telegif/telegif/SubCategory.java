package ir.telegif.telegif;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

public class SubCategory extends AppCompatActivity {

    public static ShowcaseView SV;
    public Context context=this;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SV=null;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final String catName = getIntent().getExtras().getString("CatName");
        toolbar.setTitle(catName);
        setSupportActionBar(toolbar);

        final int catID = getIntent().getExtras().getInt("catID");

        final String[][][] SubCats = {{
                {"Agree", "Amused", "Angry", "Applause", "BFD", "Bored", "Confused", "Crying", "Disappointed", "Disgusted", "Embarrassed", "Excited", "Eye Roll", "Facepalm", "Fist Bump", "FML", "Frown", "Good Job", "Goodbye", "Happy", "High Five", "Laughing", "LOL", "No", "Not Bad", "OK", "Rage", "Sad", "Scared", "Shut Up", "Sleepy", "Smile", "Sorry", "Success", "Thank You", "What", "Yawn", "Yes"},
                {"Blank & White", "Cold", "Funny", "Scary", "Slow Motion", "HD"},
                {"Breaking Up", "Cooking", "Crying", "Eating", "Dreaming", "Fainting", "Falling", "Fighting", "Running", "Singing", "Pout", "Smoking", "Slapping", "Sneezing", "Waiting", "Tossing Drink"},
                {"Minions", "Sponge Bob", "Aladdin", "101 Dalmatians", "Batman", "Garfield", "Hotel Transylvania", "Pink Panther", "Pinocchio", "Beauty And The Beast", "The Lion King", "Wall E", "Toy Story"},
                {"Cat", "Dog", "Goat", "Hedgehog", "Monkey", "Rabbit", "Sloth", "Panda", "Others"},
                {"Black & White", "3D", "Architecture", "Art", "Loop", "Photography", "Timelapse", "Typography"},
                {"Barack Obama", "Hillary Clinton", "Trump"},
                {"Soccer", "Baseball", "Basketball", "Boxing", "Parkour", "Volleyball", "Wrestling", "MMA"},
                {"BMW", "Ferrari", "Audi", "Lamborghini", "Subway"},
                {"Troll", "Surprised Patrick", "Sturgeon-Face", "Come at Me Bro", "Like a Boss", "Fail"},
                {"Barcelona", "Real Madrid", "Arsenal", "MNU", "Chelsea", "Liverpool", "MNC", "Juventus", "Bayern"},
                {"game of Thrones"}},{
                {"موافقم", "شگفت زده", "عصبی", "تایید", "برید کنار شاختون اومد", "کسل", "گیج", "گریه و زاری", "ناامید", "تنفر", "ضایع شدن", "هیجان زده", "آی رول", "فیس پالم", "ایول", " تف به این زندگی", "اخم", "آفرین", "بای بای", "خوشحال", "بزن قدش", "خندیدن", "قهقه زدن", "نه بابا بی خیال", "بد نیست", "اوکی", "خشم", "ناراحت", "ترسیده", "خفه شو", "خوابم میاد", "لبخند", "متاسفم", "موفقیت", "تشکر", "چی؟", "خمیازه", "آره"},
                {"سیاه سفید", "یخ زدم", "خنده دار", "ترسیدن", "حرکت آهسته", "اچ دی"},
                {"قلبم شکست", "آشپزی", "گریه و زرای", "غذا خوردن", "رویا پردازی", "غش کردن", "افتادن", "جنگیدن", "دویدن", "آواز خواندن", "لب غنچه کردن", "سیگار کشیدن", "چک زدن", "عطسه", "منتظر موندن", "آب پاشیدن"},
                {"مینیون ها", "باب اسفنجی", "علاالدین", "101 سگ خال دار", "بتمن", "گارفیلد", "هتل ترنسیلوانیا", "پلنگ صورتی", "پینوکیو", "زشت و زیبا", "شیر شاه", "وال ای", "اسباب بازی ها"},
                {"گربه", "سگ", "بزغاله", "جوجه تیغی", "میمون", "خرگوش", "تنبل", "پاندا", "دیگر"},
                {"سیاه سفید", "سه بعدی", "معماری", "هنر", "بازگشتی", "عکاسی", "گذشت زمان", "تایپو گرافی"},
                {"باراک اوباما", "هیلاری کلینتون", "دونالد ترامپ"},
                {"فوتبال", "بیس بال", "بسکتبال", "بوکس", "پارکور", "والی بال", "کشتی", "ام ام ای"},
                {"بی ام و", "فراری", "آئودی", "لامبورگینی", "مترو"},
                {"ترول", "پاتریک شگفت زده", "بد نیست", "بیا بغلم", "شاخ", "شکست"},
                {"بارسلونا", "رئال مادريد", "آرسنال", "منچستر یونایتد", "چلسی", "لیورپول", "منچستر سیتی", "یوونتوس", "بایرن"},
                {"گیم آف ترونز"}}};



        final int[][] EmojiIDs = {
                {R.drawable.agree, R.drawable.amused, R.drawable.angry, R.drawable.applause, R.drawable.bfd, R.drawable.bored, R.drawable.confused, R.drawable.crying, R.drawable.disappointed, R.drawable.disgusted, R.drawable.embarrassed, R.drawable.excited, R.drawable.eyeroll, R.drawable.facepalm, R.drawable.fistbump, R.drawable.fml, R.drawable.frown, R.drawable.goodjob, R.drawable.godbye, R.drawable.happy, R.drawable.highfive, R.drawable.laughing, R.drawable.lol, R.drawable.no, R.drawable.notbad, R.drawable.ok, R.drawable.rage, R.drawable.sad, R.drawable.scared, R.drawable.shutup, R.drawable.sleepy, R.drawable.smile, R.drawable.sorry, R.drawable.success, R.drawable.thankyou, R.drawable.what, R.drawable.yawn, R.drawable.yes},
                {R.drawable.blackandwhite, R.drawable.cold, R.drawable.funny, R.drawable.scary, R.drawable.slowmotion, R.drawable.hd},
                {R.drawable.breakingup, R.drawable.cooking, R.drawable.crying, R.drawable.eating, R.drawable.dreaming, R.drawable.faint, R.drawable.falling, R.drawable.fighting, R.drawable.running, R.drawable.singing, R.drawable.pout, R.drawable.smoking, R.drawable.slapping, R.drawable.sneezing, R.drawable.waiting, R.drawable.tossingdrink},
                {R.drawable.minions, R.drawable.spongebob, R.drawable.aladdin, R.drawable._101dalmatians, R.drawable.batman, R.drawable.garfield, R.drawable.hoteltransylvania, R.drawable.pinkpanther, R.drawable.pinocchio, R.drawable.beautyandthebeast, R.drawable.thelionking, R.drawable.walle, R.drawable.toystory},
                {R.drawable.cat, R.drawable.dog, R.drawable.goat, R.drawable.hedgehog, R.drawable.monkey, R.drawable.rabbit, R.drawable.sloth, R.drawable.panda, R.drawable.others},
                {R.drawable.blackandwhite, R.drawable._3d, R.drawable.architecture, R.drawable.art, R.drawable.loop, R.drawable.photography, R.drawable.timelapse, R.drawable.typography},
                {R.drawable.obama, R.drawable.clinton, R.drawable.trump},
                {R.drawable.soccer, R.drawable.baseball, R.drawable.basketball, R.drawable.boxing, R.drawable.parkour, R.drawable.volleyball, R.drawable.wrestling, R.drawable.mma},
                {R.drawable.bmw, R.drawable.ferrari, R.drawable.audi, R.drawable.lamborghini, R.drawable.subway},
                {R.drawable.troll, R.drawable.surprisedpatrick, R.drawable.sturgeonface, R.drawable.comeatmebro, R.drawable.likeaboss, R.drawable.fail},
                {R.drawable.barcelona, R.drawable.realmadrid, R.drawable.tele_gif_football, R.drawable.mnu, R.drawable.chelsea, R.drawable.liverpool, R.drawable.mnc, R.drawable.juventus, R.drawable.bayern},
                {R.drawable.gameofthrones}};
        listView = (ListView) findViewById(R.id.LV1);
        SubCategoryCustomAdapter arrayAdapter = new SubCategoryCustomAdapter(this, R.layout.sub_category_row, listView, EmojiIDs[catID], SubCats[SettingsActivity.lang][catID]);
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubCategory.this, GifMenu.class);
                intent.putExtra("SubCatID", position + 1);
                intent.putExtra("CatID", catID + 1);
                intent.putExtra("SubCatName", SubCats[SettingsActivity.lang][catID][position]);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        context=null;
    }
}
