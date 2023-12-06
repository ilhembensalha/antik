package com.example.antik;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import com.squareup.picasso.Picasso;
import java.util.List;

public class autoscrollpageradapter extends PagerAdapter {

    private Context context;
    private List<Annonce.Annoncee> annonces;

    public autoscrollpageradapter(Context context, List<Annonce.Annoncee> annonces) {
        this.context = context;
        this.annonces = annonces;
    }

    @Override
    public int getCount() {
        return annonces.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(context);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.auto_scroll_item, container, false);

        ImageView imageView = layout.findViewById(R.id.autoScrollImageView);
        TextView titleTextView = layout.findViewById(R.id.autoScrollTitleTextView);
        TextView priceTextView = layout.findViewById(R.id.autoScrollPriceTextView);

        Annonce.Annoncee annoncee = annonces.get(position);

        Picasso.get().load(annoncee.getImage().getUrl()).into(imageView);
        titleTextView.setText(annoncee.getTitre());
        priceTextView.setText("" + annoncee.getPrix() + " DT");

        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
