package com.at.newsapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.at.newsapp.Activity.DetailsActivity;
import com.at.newsapp.Activity.MainActivity;
import com.at.newsapp.Models.Articles;
import com.at.newsapp.Models.TotalResponse;
import com.at.newsapp.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;


public class ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ARTICLE_ITEM=0;
    private static final int AD_ITEM=1;
    private int lastPosition=-1;
    private final List<Object> mRecyclerViewItems;
    public ArticlesAdapter(List<Object> mRecyclerViewItems) {
        this.mRecyclerViewItems = mRecyclerViewItems;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case AD_ITEM:
                View unifiedNativeLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.gnt_small_template_view,
                        viewGroup, false);
                return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
            case ARTICLE_ITEM:
                // Fall through.
            default:
                View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.list_item, viewGroup, false);
                return new ArticleItemViewHolder(menuItemLayoutView);
        }
    }
    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = mRecyclerViewItems.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return AD_ITEM;
        }
        return ARTICLE_ITEM;
    }
    public class ArticleItemViewHolder extends RecyclerView.ViewHolder {
        private TextView listTitle, listAuthor, listSource, listDescription, listContent, listPublicationDate;
        private ImageView listPhoto;

        ArticleItemViewHolder(View view) {
            super(view);
            listTitle = itemView.findViewById(R.id.article_title);
            listAuthor = itemView.findViewById(R.id.article_author);
            listPublicationDate = itemView.findViewById(R.id.article_date);
            listSource = itemView.findViewById(R.id.article_source);
            listDescription = itemView.findViewById(R.id.article_description);
            listContent = itemView.findViewById(R.id.article_content);
            listPublicationDate = itemView.findViewById(R.id.article_date);
            listPhoto = itemView.findViewById(R.id.article_photo);
        }
    }

    public class UnifiedNativeAdViewHolder extends RecyclerView.ViewHolder {
        private UnifiedNativeAdView adView;
        UnifiedNativeAdViewHolder(View view) {
            super(view);
            adView = (UnifiedNativeAdView) view.findViewById(R.id.ad_view);
            // The MediaView will display a video asset if one is present in the ad, and the
            // first image asset otherwise.
            adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
            // Register the view used for each individual asset.
            adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
            adView.setBodyView(adView.findViewById(R.id.ad_body));
            adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
            adView.setIconView(adView.findViewById(R.id.ad_icon));
            adView.setPriceView(adView.findViewById(R.id.ad_price));
            adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
            adView.setStoreView(adView.findViewById(R.id.ad_store));
            adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        }
        public UnifiedNativeAdView getAdView() {
            return adView;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case AD_ITEM:
                Log.d("ADP", "123");
                UnifiedNativeAd nativeAd = (UnifiedNativeAd) mRecyclerViewItems.get(position);
                populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
                break;
            case ARTICLE_ITEM:
                // fall through
            default:
                ArticleItemViewHolder menuItemHolder = (ArticleItemViewHolder) holder;
                Articles articles = (Articles) mRecyclerViewItems.get(position);

                // Get the menu item image resource ID.
                String title = articles.getTitle();
                String author = articles.getAuthor();
                String source = articles.getSource().getName();
                String description = articles.getDescription();
                String content = articles.getContent();
                String publicationDate = articles.getPublishedAt();
                String urlToImage = articles.getUrlToImage();
                String url = articles.getUrl();
                String publicationTime = publicationDate.substring(11,publicationDate.length()-1);
                publicationDate = publicationDate.substring(0,10);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
                try{
                    Date newDate = format.parse(publicationDate);
                    format = new SimpleDateFormat("dd MMMM yyyy");
                    String date = format.format(newDate);
                    publicationDate = publicationTime+ ", " + date;
                }catch (Exception ex){

                }
                menuItemHolder.listTitle.setText(title);
                menuItemHolder.listAuthor.setText(author);
                menuItemHolder.listPublicationDate.setText(publicationDate);
                menuItemHolder.listSource.setText(source);
                menuItemHolder.listDescription.setText(description);
                menuItemHolder.listContent.setText(content);
                if(menuItemHolder.listContent.getText().equals("")){
                    menuItemHolder.listContent.setVisibility(View.GONE);
                }
                menuItemHolder.listPublicationDate.setText(publicationDate);
                menuItemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(menuItemHolder.itemView.getContext(), DetailsActivity.class);
                        intent.putExtra("url", url);
                        menuItemHolder.itemView.getContext().startActivity(intent);
                    }
                });
                try {
                    Glide.with(menuItemHolder.itemView.getContext())
                            .load(urlToImage)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    menuItemHolder.itemView.findViewById(R.id.article_pb).setVisibility(View.GONE);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    menuItemHolder.itemView.findViewById(R.id.article_pb).setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(menuItemHolder.listPhoto);
                } catch (Exception e) {

                }
        }
        if(lastPosition < position){
            lastPosition = position;
        }
    }
    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView listTitle, listAuthor, listSource, listDescription, listContent, listPublicationDate;
        private ImageView listPhoto;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listTitle = itemView.findViewById(R.id.article_title);
            listAuthor = itemView.findViewById(R.id.article_author);
            listPublicationDate = itemView.findViewById(R.id.article_date);
            listSource = itemView.findViewById(R.id.article_source);
            listDescription = itemView.findViewById(R.id.article_description);
            listContent = itemView.findViewById(R.id.article_content);
            listPublicationDate = itemView.findViewById(R.id.article_date);
            listPhoto = itemView.findViewById(R.id.article_photo);
        }

        private void setArticles(final String title, final String author, final String source, final String description, final String content, final String publicationDate, final String url, final int position) {
            listTitle.setText(title);
            listAuthor.setText(author);
            listPublicationDate.setText(publicationDate);
            listSource.setText(source);
            listDescription.setText(description);
            listContent.setText(content);
            if(listContent.getText().equals("")){
                listContent.setVisibility(View.GONE);
            }
            listPublicationDate.setText(publicationDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), DetailsActivity.class);
                    intent.putExtra("url", url);
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        private void setListPhoto(String iconPath) {

        }
    }
    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
}