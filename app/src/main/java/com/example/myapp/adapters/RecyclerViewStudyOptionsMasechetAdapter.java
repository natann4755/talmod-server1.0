package com.example.myapp.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.model.shas_masechtot.Masechet;
import com.example.myapp.R;

import java.util.List;

public class RecyclerViewStudyOptionsMasechetAdapter extends RecyclerView.Adapter<RecyclerViewStudyOptionsMasechetAdapter.Holder> {

    private List <Masechet> mMasechetItems;
    private ListenerCreateTypeOfStudy mListener;


    public RecyclerViewStudyOptionsMasechetAdapter(List<Masechet> mSederItems,  ListenerCreateTypeOfStudy listenercreateTypeOfStudy) {
        this.mMasechetItems = mSederItems;
        this.mListener = listenercreateTypeOfStudy;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_masechet_in_profile,parent,false);
        return new RecyclerViewStudyOptionsMasechetAdapter.Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.setHolder(mMasechetItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mMasechetItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView study;
        private Masechet masechet;

        public Holder(@NonNull View itemView) {
            super(itemView);
            study = itemView.findViewById(R.id.rv_Horizontal_text_book_TV);

            itemView.setOnClickListener(v -> mListener.createListTypeOfStudy(masechet.getName(), masechet.getPages()));
        }

        public void setHolder (Masechet masechet){
            this.masechet = masechet;
            study.setText(masechet.getName());
        }
    }

    public interface ListenerCreateTypeOfStudy {
        void createListTypeOfStudy(String stringTypeOfStudy, int pageMasechet);
    }
}