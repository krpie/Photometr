package com.example.artem.photometr;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by artem on 16.08.2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final ArrayList<String> items;
    private final int mResource;
    private final WeakReference<MainActivity> mActivity;

    public CustomArrayAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull ArrayList<String> objects, WeakReference<MainActivity> mActivity) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
        this.mActivity = mActivity;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(final int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView measuringInfo = view.findViewById(R.id.date_textView);
        Button button = view.findViewById(R.id.button_close_graph);

        measuringInfo.setText(items.get(position));
        if(!items.get(position).equals("Пусто")){
            button.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onClick(View view) {
                    if(mActivity.get().rzlts.get(position).isSaved)
                        mActivity.get().closeItem(position);
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity.get());
                        builder.setTitle("\""+new DecimalFormat("0000").format(mActivity.get().rzlts.get(position).number)+" - "+new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(mActivity.get().rzlts.get(position).date))+"\". Файл не сохранен. Сохранить?")
                                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mActivity.get().createNewFile(3,position);
                                        mActivity.get().closeItem(position);
                                    }
                                })
                                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        mActivity.get().closeItem(position);
                                    }
                                })
                                .setCancelable(false);
                    }
                }
            });
            measuringInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mActivity.get().lastSelectedItemPos==position){
                        mActivity.get().tabWatchFragment.spinner.performClick();
                    }else {
                        mActivity.get().selectValues(position);
                    }

                }
            });
        } else button.setVisibility(View.INVISIBLE);


        return view;
    }
}
