package com.technosaab.newdavai.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.technosaab.newdavai.Models.AdsImgModel;
import com.technosaab.newdavai.R;
import com.technosaab.newdavai.Helper.CustomVolleyRequest;


import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<AdsImgModel> sliderImg;
    private ImageLoader imageLoader;


    public ViewPagerAdapter(List sliderImg, Context context) {
        this.sliderImg = sliderImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.swipe_fragment, null);

        AdsImgModel utils = sliderImg.get(position);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(utils.getImgPath(), ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(context, "You Clicked Slide " + position, Toast.LENGTH_SHORT).show();
//                if(position == 0){
//                    Toast.makeText(context, "Slide 1 Clicked", Toast.LENGTH_SHORT).show();
//                } else if(position == 1){
//                    Toast.makeText(context, "Slide 2 Clicked", Toast.LENGTH_SHORT).show();
//                } else if (position == 2){
//                    Toast.makeText(context, "Slide 3 Clicked", Toast.LENGTH_SHORT).show();
//                }else if (position == 3){
//                    Toast.makeText(context, "Slide 4 Clicked", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);

    }
}
