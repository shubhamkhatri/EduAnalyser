package com.shubham.eduanalyser;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.shubham.eduanalyser.model.word;

import java.util.ArrayList;

public class DocumentAdaptor extends ArrayAdapter<word> {

    int mcolorId;

    public DocumentAdaptor(Activity context, ArrayList<word> words, int colorId) {
        super(context, 0, words);
        mcolorId = colorId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        word currentWord = getItem(position);


        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name_text_view);
        nameTextView.setText(currentWord.getNameTranslation());

        TextView categryTextView = (TextView) listItemView.findViewById(R.id.categry_text_view);
        categryTextView.setText(currentWord.getCategryTranslation());


        return listItemView;

    }


}
