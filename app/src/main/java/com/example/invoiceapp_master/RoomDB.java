package com.example.appkhushveehoreca;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for handling Firestore queries.
 * Keeps UI (adapters) separate from data retrieval.
 */
public class RoomDB {

    private static final String TAG = "DBRepository";

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public DBRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Load categories from Firestore
     */
    public void loadCategories(RecyclerView recyclerView, Context context) {
        firestore.collection("CATEGORIES")
                .orderBy("index")
                .get()
                .addOnCompleteListener(task -> handleCategoryResult(task, recyclerView, context));
    }

    private void handleCategoryResult(Task<QuerySnapshot> task,
                                      RecyclerView recyclerView,
                                      Context context) {
        if (task.isSuccessful() && task.getResult() != null) {
            List<CategoryModel> categories = new ArrayList<>();
            for (QueryDocumentSnapshot doc : task.getResult()) {
                String icon = doc.getString("icon");
                String name = doc.getString("categoryName");

                if (icon != null && name != null) {
                    categories.add(new CategoryModel(icon, name));
                }
            }

            CategoryAdapter adapter = new CategoryAdapter(categories);
            recyclerView.setAdapter(adapter);

        } else {
            handleError(task.getException(), context);
        }
    }

    /**
     * Load data for a specific fragment/category
     */
    public void loadFragmentData(RecyclerView recyclerView,
                                 Context context,
                                 int index,
                                 String categoryName) {
        firestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS")
                .orderBy("index")
                .get()
                .addOnCompleteListener(task -> handleFragmentResult(task, recyclerView, context, index));
    }

    private void handleFragmentResult(Task<QuerySnapshot> task,
                                      RecyclerView recyclerView,
                                      Context context,
                                      int index) {
        if (task.isSuccessful() && task.getResult() != null) {
            List<HomePageModel> homePageModels = new ArrayList<>();

            for (QueryDocumentSnapshot doc : task.getResult()) {
                Long viewType = doc.getLong("view_type");
                if (viewType == null) continue;

                switch (viewType.intValue()) {
                    case 0: // Slider banners
                        homePageModels.add(new HomePageModel(
                                0,
                                extractSliderModels(doc)
                        ));
                        break;

                    case 1: // Strip ad
                        homePageModels.add(new HomePageModel(
                                1,
                                safeGet(doc, "strip_ad_banner"),
                                safeGet(doc, "background")
                        ));
                        break;

                    case 2: // Horizontal products
                        homePageModels.add(new HomePageModel(
                                2,
                                safeGet(doc, "layout_title"),
                                safeGet(doc, "layout_background"),
                                extractHorizontalProducts(doc),
                                extractWishlistProducts(doc)
                        ));
                        break;

                    case 3: // Grid products
                        homePageModels.add(new HomePageModel(
                                3,
                                safeGet(doc, "layout_title"),
                                safeGet(doc, "layout_background"),
                                extractHorizontalProducts(doc)
                        ));
                        break;
                }
            }

            HomePageAdapter adapter = new HomePageAdapter(homePageModels);
            recyclerView.setAdapter(adapter);
            HomeFragment.swipeRefreshLayout.setRefreshing(false);

        } else {
            handleError(task.getException(), context);
        }
    }

    // ----------- Helpers ----------- //

    private List<SliderModel> extractSliderModels(DocumentSnapshot doc) {
        List<SliderModel> sliders = new ArrayList<>();
        Long count = doc.getLong("no_of_banners");
        if (count == null) return sliders;

        for (int i = 1; i <= count; i++) {
            String banner = safeGet(doc, "banner_" + i);
            String bg = safeGet(doc, "banner_" + i + "_background");
            if (banner != null && bg != null) {
                sliders.add(new SliderModel(banner, bg));
            }
        }
        return sliders;
    }

    private List<HorizontalProductScrollModel> extractHorizontalProducts(DocumentSnapshot doc) {
        List<HorizontalProductScrollModel> products = new ArrayList<>();
        Long count = doc.getLong("no_of_products");
        if (count == null) return products;

        for (int i = 1; i <= count; i++) {
            products.add(new HorizontalProductScrollModel(
                    safeGet(doc, "product_ID_" + i),
                    safeGet(doc, "product_image_" + i),
                    safeGet(doc, "product_title_" + i),
                    safeGet(doc, "product_subtitle_" + i),
                    safeGet(doc, "product_subtitle2_" + i),
                    safeGet(doc, "product_price_" + i),
                    safeGet(doc, "cutted_price_" + i)
            ));
        }
        return products;
    }

    private List<WishlistModel> extractWishlistProducts(DocumentSnapshot doc) {
        List<WishlistModel> wishlist = new ArrayList<>();
        Long count = doc.getLong("no_of_products");
        if (count == null) return wishlist;

        for (int i = 1; i <= count; i++) {
            wishlist.add(new WishlistModel(
                    doc.getId(),
                    safeGet(doc, "product_image_" + i),
                    safeGet(doc, "product_title_" + i),
                    safeGet(doc, "product_subtitle_" + i),
                    safeGet(doc, "product_subtitle2_" + i),
                    safeGet(doc, "product_price_" + i),
                    safeGet(doc, "cutted_price_" + i)
            ));
        }
        return wishlist;
    }

    private String safeGet(DocumentSnapshot doc, String key) {
        return doc.contains(key) ? doc.getString(key) : null;
    }

    private void handleError(Exception e, Context context) {
        if (e != null) {
            Log.e(TAG, "Firestore Error: ", e);
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
