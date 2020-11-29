package com.example.mynotify;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Student extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    SearchView mySearchView;
    ArrayAdapter<String> adapter;
    private RecyclerView mItemlist;
    private DatabaseReference mRef;
    private View mView;
    private LinearLayoutManager mManager;
    private FirebaseDatabase mDatabase;
    private FirebaseRecyclerAdapter <Item, ItemViewHolder> mItemlistAdapter;
    String userId;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        mItemlist=(RecyclerView)findViewById(R.id.itemlist);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        mItemlist.setLayoutManager(new LinearLayoutManager(this));
        mItemlist.setHasFixedSize(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRef = FirebaseDatabase.getInstance().getReference().child(userId).child("Student").child("item");
        Query query = mRef.orderByKey();
        mRef.keepSynced(true);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,databaseList()  );

        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(query, Item.class).build();
        mItemlistAdapter = new FirebaseRecyclerAdapter<Item, ItemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemViewHolder holder, int position, @NonNull Item model) {
                holder.setItemname(model.getName());
                holder.setItemnumber(model.getQuantity());
                holder.setItemdesc(model.getDescription());
                holder.setItemimage(model.getImage());

            }

            @NonNull
            @Override
            public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.item_row, parent, false);

                return new ItemViewHolder(view);
            }
        };

        mItemlist.setAdapter(mItemlistAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();

        mItemlistAdapter.startListening();


    }

    @Override
    protected void onStop() {
        super.onStop();

        mItemlistAdapter.stopListening();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setItemname(String name){

            TextView item_name = (TextView) mView.findViewById(R.id.item_name);
            item_name.setText(name);

        }
        public void setItemnumber(String number){

            TextView item_number = (TextView) mView.findViewById(R.id.item_number);
            item_number.setText(number);

        }
        public void setItemdesc(String desc){

            TextView item_desc = (TextView) mView.findViewById(R.id.item_description);
            item_desc.setText(desc);

        }
        public void setItemimage(String image){

            ImageView item_image = (ImageView) mView.findViewById(R.id.item_image);
            Picasso.get().load(image).into(item_image);

        }
    }
    public void ClickAddevent(View view){
        MainActivity.redirectActivity(this, Addingevents.class);
    }

    public void AddItems(View view){
        startActivity(new Intent(Student.this,Addingitems.class));

    }

    public void ClickMenu(View view){
        //Open drawer
        MainActivity.openDrawer(drawerLayout);
    }

    public void ClickLogo(View view){
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
    public void ClickTrigger(View view){
        MainActivity.redirectActivity(this,Trigger.class);
    }

    public void ClickHome(View view){
        //Redirect activity to home
        MainActivity.redirectActivity(this,MainActivity.class);
    }

    public void ClickProfile(View view){
        //redirect to Profile activity
        MainActivity.redirectActivity(this,Profile.class);
    }
    public void ClickStudent(View view){
        //recreate
        recreate();
    }
    public void ClickHousehold(View view){
        //redirect to household activity
        MainActivity.redirectActivity(this,Household.class);
    }
    public void ClickOffice(View view){
        //redirect to office activity
        MainActivity.redirectActivity(this,Office.class);
    }
    public void ClickShare(View view){
        MainActivity.redirectActivity(this,Share.class);
    }

    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
        Toast.makeText(Student.this, "You are logged out", Toast.LENGTH_SHORT).show();
    }
    public void ClickRateus(View view){
        //redirect to rate us activity
        MainActivity.redirectActivity(this,Rateus.class);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Close drawer
        MainActivity.closeDrawer(drawerLayout);
    }
}