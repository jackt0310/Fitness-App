package com.buffboosterapp.buffbooster;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ProgressPicAdapter extends RecyclerView.Adapter<ProgressPicAdapter.ViewHolder> {
    ArrayList<String> fileNames;
    ArrayList<String> paths;
    ArrayList<LocalDateTime> dates;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    ProgressPicAdapter(Context context, ArrayList<String> fileNames, ArrayList<String> paths, ArrayList<LocalDateTime> dates) {
        this.mInflater = LayoutInflater.from(context);
        this.fileNames = fileNames;
        this.paths = paths;
        this.dates = dates;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.picture_list, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        holder.myTextView.setText(dtf.format(dates.get(position)));
        holder.myImageView.setImageBitmap(loadImageFromStorage(paths.get(position), fileNames.get(position)));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return fileNames.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView myImageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.picDateText);
            myImageView = itemView.findViewById(R.id.picImage);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return fileNames.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }



    /*
    public ProgressPicAdapter() {
        fileNames = new ArrayList<String>();
        paths = new ArrayList<String>();
        dates = new ArrayList<LocalDateTime>();
    }

    @NonNull
    @Override
    public ProgressPicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressPicAdapter.ViewHolder holder, int position) {

    }

    public ProgressPicAdapter(ArrayList<String> fileNames, ArrayList<String> paths, ArrayList<LocalDateTime> dates) {
        this.fileNames = fileNames;
        this.paths = paths;
        this.dates = dates;
    }
*/
    public int getCount() {
        return fileNames.size();
    }
/*
    @Override
    public Object getItem(int position) {
        return fileNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
*/
    void add(String fileName, String path, LocalDateTime date) {
        fileNames.add(fileName);
        paths.add(path);
        dates.add(date);
    }

    /*
    void add(int pos, Workout element) {
        workouts.add(pos, element);
    }

    void replace(int pos, Workout element) {
        workouts.set(pos, element);
    }

    public void save() {
        try
        {
            File file = new File(main.getFilesDir() + "workoutList.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(workouts);
            oos.writeObject(replacePos);
            oos.close();
            fos.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void edit(int position) {
        replacePos = position;
        Intent intent = new Intent(main, EntryForm.class);
        intent.putExtra("editList", (Parcelable) workouts.get(position));
        //workouts.remove(position);
        save();
        main.startActivity(intent);
    }
     */
    private Bitmap loadImageFromStorage(String path, String fileName)
    {

        try {
            File f=new File(path, fileName);
            return BitmapFactory.decodeStream(new FileInputStream(f));
            //ImageView img=(ImageView)findViewById(R.id.imgPicker);
            //img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return null;

    }

    /*
    public View getView(int position, View convertView, ViewGroup parent) {


        ImageView imageView = new ImageView(parent.getContext());

        imageView.setImageBitmap(loadImageFromStorage(paths.get(position), fileNames.get(position)));



        //LinearLayout linLayout = new LinearLayout();
        //linLayout.addView(imageView);

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return (imageView);
    }*/
}