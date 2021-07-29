package com.example.safeout.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.safeout.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

public class MarkerManagerRenderer extends DefaultClusterRenderer<MyMarkers> {

    private final IconGenerator iconGenerator;
    private final ImageView imageView;
    private final int markerWidth;
    private final int markerHeight;
    private Context context;

    public MarkerManagerRenderer(Context context, GoogleMap map, ClusterManager<MyMarkers> clusterManager) {

        super(context, map, clusterManager);
        this.context = context;

        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView =  new ImageView(context.getApplicationContext());
        markerWidth = (int) context.getResources().getDimension(R.dimen.custom_marker_image);
        markerHeight = (int) context.getResources().getDimension(R.dimen.custom_marker_image);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(markerWidth, markerHeight));
        int padding = (int) context.getResources().getDimension(R.dimen.custom_marker_padding);
        imageView.setPadding(padding, padding, padding, padding);
        iconGenerator.setContentView(imageView);
    }

    @Override
    protected void onBeforeClusterItemRendered(MyMarkers item, MarkerOptions markerOptions) {
        Glide.with(context).load(item.getUserPicture().getUrl()).into(imageView);
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.position(item.getPosition()).title(item.getTitle()).snippet("📞 " + item.getSnippet()).icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster<MyMarkers> cluster) {
        // Avoid clustering
        return false;
    }
}
