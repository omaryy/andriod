package Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.PagerAdapter;

import com.example.image.MainActivity;
import com.example.image.R;
import com.example.image.WelcomeActivity;

import java.util.prefs.BackingStoreException;

public class Slideviewpageradapter extends PagerAdapter {
    Context context;

    public Slideviewpageradapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
View view=layoutInflater.inflate(R.layout.slidescreen,container,false);
container.addView(view);
ImageView logo=view.findViewById(R.id.logo);
        ImageView one=view.findViewById(R.id.one);
        ImageView two=view.findViewById(R.id.two);

        ImageView next=view.findViewById(R.id.next);

        ImageView before=view.findViewById(R.id.before);
        Button start=view.findViewById(R.id.Start);
        TextView slidescreentextview=view.findViewById(R.id.slidescreentextview);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(context,MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(intent);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelcomeActivity.viewPager.setCurrentItem(position+1);

            }
        });
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WelcomeActivity.viewPager.setCurrentItem(position-1);


            }
        });
        switch (position)
        {
            case  0:
               logo.setImageResource(R.drawable.undraw_moving_twwf);
                one.setImageResource(R.drawable.selected);
                two.setImageResource(R.drawable.unselected);

                before.setVisibility(view.GONE);
                next.setVisibility(view.VISIBLE);
                start.setVisibility(view.GONE);
                break;
            case 1:
                logo.setImageResource(R.drawable.undraw_takeout_boxes_ap54);
                one.setImageResource(R.drawable.unselected);
                two.setImageResource(R.drawable.selected);
               slidescreentextview.setText("Once you Found it contact with the publisher");
                before.setVisibility(view.VISIBLE);
next.setVisibility(view.GONE);
break;
        }


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
