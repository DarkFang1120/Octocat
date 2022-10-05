package com.example.appkhushveehoreca;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DBQueries {

    public static FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public static FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

//    public static List<HomePageModel> homePageModelList = new ArrayList<>();

    ///// Categories List //////

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadCategoriesNames = new ArrayList<>();

    public static List<String> cartList = new ArrayList<>();
    public static List<CartItemModel> cartItemModelList = new ArrayList<>();


    /// List inside list parent child logic

    public static void loadCategories(final RecyclerView categoriesRecyclerView, final Context context) {

        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {
                                categoryModelList.add(new CategoryModel(Objects.requireNonNull(documentSnapshot.get("icon")).toString(), Objects.requireNonNull(documentSnapshot.get("categoryName")).toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList);
                            categoriesRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            String error = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //// if error then remove requireNoNull ////

    public static void loadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName) {

        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName.toUpperCase())
                .collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult())) {

                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banners");
                                    for (long x = 1; x < no_of_banners + 1; x++) {
                                        sliderModelList.add(new SliderModel(Objects.requireNonNull(documentSnapshot.get("banner_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("banner_" + x + "_background")).toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));
                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(1, Objects.requireNonNull(documentSnapshot.get("strip_ad_banner")).toString(),
                                            Objects.requireNonNull(documentSnapshot.get("background")).toString()));

                                } else if ((long) documentSnapshot.get("view_type") == 2) {

                                    List<WishlistModel> viewAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(Objects.requireNonNull(documentSnapshot.get("product_ID_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_image_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_title_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_subtitle_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_subtitle2_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_price_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("cutted_price_" + x)).toString()
                                        ));


                                        viewAllProductList.add(new WishlistModel(documentSnapshot.getId(), Objects.requireNonNull(documentSnapshot.get("product_image_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_title_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_subtitle_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_subtitle2_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_price_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("cutted_price_" + x)).toString()
                                        ));
                                    }
                                    lists.get(index).add(new HomePageModel(2, Objects.requireNonNull(documentSnapshot.get("layout_title")).toString(), Objects.requireNonNull(documentSnapshot.get("layout_background")).toString(), horizontalProductScrollModelList, viewAllProductList));

                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> gridLayoutModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        gridLayoutModelList.add(new HorizontalProductScrollModel(Objects.requireNonNull(documentSnapshot.get("product_ID_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_image_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_title_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_subtitle_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_subtitle2_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("product_price_" + x)).toString()
                                                , Objects.requireNonNull(documentSnapshot.get("cutted_price_" + x)).toString()
                                        ));
                                    }
                                    lists.get(index).add(new HomePageModel(3, Objects.requireNonNull(documentSnapshot.get("layout_title")).toString(), Objects.requireNonNull(documentSnapshot.get("layout_background")).toString(), gridLayoutModelList));
                                }
                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);
                            homePageAdapter.notifyDataSetChanged();
                        } else {
                            String error = Objects.requireNonNull(task.getException()).getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}