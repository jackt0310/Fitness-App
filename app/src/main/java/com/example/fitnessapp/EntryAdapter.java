package com.example.fitnessapp;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

public class EntryAdapter extends BaseAdapter implements ListAdapter {
    ArrayList<Entry> entry;

    public EntryAdapter() {
        entry = new ArrayList<Entry>();
    }

    public EntryAdapter(ArrayList<Entry> entry) {
        this.entry = entry;
    }

    public ArrayList<Entry> getList() {
        return entry;
    }

    @Override
    public int getCount() {
        return entry.size();
    }

    @Override
    public Object getItem(int position) {
        return entry.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    void add(Entry element) {
        entry.add(0, element);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = new LinearLayout(parent.getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        TextView dateText = new TextView(parent.getContext());
        dateText.setLayoutParams(lparams);
        dateText.setTextSize(28);
        SpannableString content = new SpannableString(entry.get(position).date);
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        dateText.setText(content);

        layout.addView(dateText);

        TextView exerciseText = new TextView(parent.getContext());
        exerciseText.setLayoutParams(lparams);
        exerciseText.setText(entry.get(position).exercise + " (" + entry.get(position).type + ")");
        exerciseText.setTextSize(24);
        layout.addView(exerciseText);

        TextView detailsText = new TextView(parent.getContext());
        detailsText.setLayoutParams(lparams);
        detailsText.setText(entry.get(position).sets + "x" + entry.get(position).reps + " @ " + entry.get(position).weight + "lb");
        detailsText.setTextSize(24);
        layout.addView(detailsText);

        CardView cardView = new CardView(parent.getContext());
        cardView.addView(layout);
        /*
        LayoutInflater inflater = getLayoutInflater();
        View row;
        row = inflater.inflate(R.layout.custom, parent, false);
        TextView title, detail;
        ImageView i1;
        title = (TextView) row.findViewById(R.id.title);
        detail = (TextView) row.findViewById(R.id.detail);
        i1=(ImageView)row.findViewById(R.id.img);
        title.setText(Title[position]);
        detail.setText(Detail[position]);
        i1.setImageResource(imge[position]);
        */
        return (cardView);
    }
}