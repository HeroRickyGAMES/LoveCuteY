package com.herorickystudios.lovecutey;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

//Programado por HeroRickyGames

public class arrayAdapter extends ArrayAdapter<cards>{

    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> itens){
        super(context, resourceId, itens);
    }
    public View getView(int position, View convertView, ViewGroup parent){

        cards card_item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView idade = (TextView) convertView.findViewById(R.id.idade);
        TextView cidade = (TextView) convertView.findViewById(R.id.cidade);
        TextView bio = (TextView) convertView.findViewById(R.id.bioText);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);


        name.setText(card_item.getName());
        idade.setText(card_item.getIdade());
        cidade.setText(card_item.getLocal());
        bio.setText(card_item.getBio());
        Glide.with(getContext()).load(card_item.getProfileImageURI()).into(image);

        return convertView;

    }
}
