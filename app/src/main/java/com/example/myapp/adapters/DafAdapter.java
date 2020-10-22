package com.example.myapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.model.Daf;
import com.example.model.enums.FILTERS;
import com.example.myapp.R;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.utils.ConvertIntToPage;
import com.example.myapp.utils.UtilsCalender;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import static com.example.myapp.utils.StaticVariables.index1;
import static com.example.myapp.utils.StaticVariables.index2;
import static com.example.myapp.utils.StaticVariables.index3;
import static com.example.myapp.utils.StaticVariables.stringDafHayomi;

public class DafAdapter extends RecyclerView.Adapter<DafAdapter.ViewHolder> {

    private Activity activity;
    private RecyclerView.LayoutManager mLayoutManager;
    private listenerOneDafAdapter mListener;
    private ArrayList<Daf> mMaster1;
    private ArrayList<Daf> myMaster2;
    private ArrayList<Daf> mMaster3;
    private ArrayList<Daf> myListALLDaf = new ArrayList<>();
    private ArrayList<Daf> myListDafToDisplay = new ArrayList<>();
    private ArrayList<Daf> myListFilterDaf = new ArrayList<>();
    private int currentTabString;



    public DafAdapter(Activity activity, RecyclerView.LayoutManager layoutManager, ArrayList<Daf> mMaster1 , ArrayList<Daf> mMaster2, ArrayList<Daf> mMaster3, listenerOneDafAdapter mListener) {
        this.activity = activity;
        this.mLayoutManager = layoutManager;
        this.mMaster1 = mMaster1;
        this.myMaster2 = mMaster2;
        this.mMaster3 = mMaster3;
        this.mListener = mListener;
        initLearning(mMaster1);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_daf, parent, false);
        return new DafAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setHolder(myListDafToDisplay.get(position));
    }

    @Override
    public int getItemCount() {
        return myListDafToDisplay.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void initLearning(ArrayList<Daf> mMaster1) {
        this.myListALLDaf.addAll(mMaster1);
        this.myListDafToDisplay.addAll(myListALLDaf);
        scrollRecyclerView(myListALLDaf);
        setSummaryLearning(R.string.all,myListALLDaf);
    }

    private void scrollRecyclerView(ArrayList<Daf> myListALLDaf) {
        if (myListALLDaf.size() > 2000) {
            int todayDaf = findTodayDafHayomi(myListALLDaf);
            if (todayDaf != -1) {
                mLayoutManager.scrollToPosition(todayDaf);
            }
        }else if (myListALLDaf.size()>0 && myListALLDaf.size()<300){
            int correctDaf =  findLastPageLearned(myListALLDaf);
            if (correctDaf != -1) {
                mLayoutManager.scrollToPosition(correctDaf);
            }
        }
    }

    private int findLastPageLearned(ArrayList<Daf> myListALLDaf) {
        int lastLearned =-1;
        for (int i = myListALLDaf.size()-1; i >= 0 ; i--) {
            if (myListALLDaf.get(i).isLearning()) {
                lastLearned = i;
                break;
            }
        }
        return lastLearned;
    }

    private int findTodayDafHayomi(ArrayList<Daf> myListALLDaf) {
        int todayDafInList = -1;
        String today = UtilsCalender.dateStringFormat(Calendar.getInstance());
        for (int i = 0; i < myListALLDaf.size(); i++) {
            if (myListALLDaf.get(i).getPageDate().equals(today)) {
                todayDafInList = i;
                break;
            }
        }
        return todayDafInList;
    }

    private void setSummaryLearning(int tabString, ArrayList<Daf> mList) {
        currentTabString = tabString;
        switch(tabString) {
            case R.string.all:
            case R.string.learned:
                mListener.initSummaryLearning(getNameLearning(mList),
                        String.format(Locale.getDefault(),"%s %d %s", activity.getString(R.string.you_learned), calculateHowMuchLearned(mList), activity.getString(R.string.pages)),
                        String.format(Locale.getDefault(),"%s %d", activity.getString(R.string.from), mList.size()));
                break;
            case R.string.skipped:
                mListener.initSummaryLearning(getNameLearning(mList),
                        String.format(Locale.getDefault(),"%d %s", + calculateHowMuchSkipped(mList), activity.getString(R.string.pages_to_complete)),
                        "");
                break;
        }
    }

    private int calculateHowMuchLearned(ArrayList<Daf> mList) {
        int learned = 0;
        for (Daf daf: mList) {
            if (daf.isLearning()){
                learned++;
            }
        }
        return learned;
    }

    private int calculateHowMuchSkipped(ArrayList<Daf> mList) {
        int learned = mList.size();
        for (Daf daf: mList) {
            if (daf.isLearning()){
                learned--;
            }
        }
        return learned;
    }


    private String getNameLearning(ArrayList<Daf> mList) {
        if (mList.size() > 0) {
            return myListALLDaf.get(0).getTypeOfStudy().equals(stringDafHayomi) ? myListALLDaf.get(0).getTypeOfStudy() : String.format(Locale.getDefault(),"%s %s", activity.getString(R.string.masechet), myListALLDaf.get(0).getTypeOfStudy());
        }
        return "";
    }

    public void filter(FILTERS filter){
        myListFilterDaf.clear();

        if (filter ==FILTERS.allLearning){
            changeDisplayList(myListALLDaf);
            visibleRVMasechtot(myListALLDaf.size());
            scrollRecyclerView(myListALLDaf);
            setSummaryLearning(R.string.all,myListALLDaf);
        }

        if (filter == FILTERS.learned){
            for (int i = 0; i <myListALLDaf.size() ; i++) {
                if (myListALLDaf.get(i).isLearning()) {
                    myListFilterDaf.add(myListALLDaf.get(i));
                }
            }
            Collections.reverse(myListFilterDaf);
            changeDisplayList(myListFilterDaf);
            setSummaryLearning(R.string.learned,myListALLDaf);
        }

        if (filter == FILTERS.skipped){
            if(myListALLDaf.size()<2000){
                int myLastPageHasBeenLearned = -1;
               myLastPageHasBeenLearned = findLastPageLearned(myListALLDaf);
                if (myLastPageHasBeenLearned != -1) {
                    for (int i = 0; i <= myLastPageHasBeenLearned; i++) {
                        if (!myListALLDaf.get(i).isLearning()) {
                            myListFilterDaf.add(myListALLDaf.get(i));
                        }
                    }
                }
            }
            if(myListALLDaf.size()>2000){
                int todayDaf = -1;
                todayDaf = findTodayDafHayomi(myListALLDaf);
                if (todayDaf != -1) {
                    for (int i = 0; i <= todayDaf-1; i++) {
                        if (!myListALLDaf.get(i).isLearning()) {
                            myListFilterDaf.add(myListALLDaf.get(i));
                        }
                    }
                }
            }
            Collections.reverse(myListFilterDaf);
            changeDisplayList(myListFilterDaf);
            setSummaryLearning(R.string.skipped,myListFilterDaf);
        }
    }

    private void visibleRVMasechtot(int size) {
        RecyclerView masechtotRecyclerView = activity.findViewById(R.id.show_study_RV_masechtot);
        if (size>2000){
            masechtotRecyclerView.setVisibility(View.VISIBLE);
        }else {
            masechtotRecyclerView.setVisibility(View.GONE);
        }
    }

    public void changeTypeStudy(int typeStudy){
        myListALLDaf.clear();
        switch(typeStudy) {
            case index1:
                myListALLDaf.addAll(mMaster1);
                break;
            case index2:
                myListALLDaf.addAll(myMaster2);
                break;
            case index3:
                myListALLDaf.addAll(mMaster3);
                break;
        }
        changeDisplayList(myListALLDaf);
        setSummaryLearning(R.string.all,myListALLDaf);
    }

    public void filterOneMasechet(String nameMasechet){
        myListFilterDaf.clear();
        for (int i = 0; i <myListALLDaf.size() ; i++) {
            if (myListALLDaf.get(i).getMasechet().equals(nameMasechet)) {
                myListFilterDaf.add(myListALLDaf.get(i));
            }
        }
        changeDisplayList(myListFilterDaf);
    }

    private void changeDisplayList(ArrayList<Daf> mNewList) {
        myListDafToDisplay.clear();
        myListDafToDisplay.addAll(mNewList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
       private CheckBox ifLearn;
       private TextView masechet;
       private TextView numDaf;
       private TextView date;
       private LinearLayout linearLayoutChazara;
       private CheckBox chazara1;
       private CheckBox chazara2;
       private CheckBox chazara3;
       private Daf mDaf;


        ViewHolder(View itemView) {
            super(itemView);
            ifLearn = itemView.findViewById(R.id.one_daf_checkbox);
            masechet = itemView.findViewById(R.id.one_daf_Tv_masechet);
            numDaf = itemView.findViewById(R.id.one_daf_Tv_numPage);
            date = itemView.findViewById((R.id.one_daf_Tv_date));
            linearLayoutChazara = itemView.findViewById((R.id.one_daf_chazara_LL));
            chazara1 = itemView.findViewById((R.id.chazara_1_CB));
            chazara2 = itemView.findViewById((R.id.chazara_2_CB));
            chazara3 = itemView.findViewById((R.id.chazara_3_CB));

            ifLearn.setOnClickListener(v -> {
                inCancelingStudyCancelChazara();
                AppDataBase.getInstance(activity).daoLearning().updateIsLearning(ifLearn.isChecked(),mDaf.getIndexTypeOfStudy() ,mDaf.getMasechet(),mDaf.getPageNumber());
                updateListIfLearning(ifLearn.isChecked());
                setSummaryLearning(currentTabString,myListDafToDisplay);
            });

            chazara1.setOnClickListener(v -> chazaraClickListener(chazara1, 1));
            chazara2.setOnClickListener(v -> chazaraClickListener(chazara2, 2));
            chazara3.setOnClickListener(v -> chazaraClickListener(chazara3, 3));
        }

        private void chazaraClickListener(CheckBox chazara, int indexChazara) {
            if (chazara.isChecked()) {
                updateChazara(indexChazara);
            } else {
                updateChazara(indexChazara-1);
            }
        }

        private void inCancelingStudyCancelChazara() {
            if (!ifLearn.isChecked()){
                if (chazara1.isChecked()){
                    chazara1.performClick();
                }
            }
        }

        private void updateListIfLearning(boolean checked) {
            for (int i = 0; i <myListALLDaf.size() ; i++) {
                if (myListALLDaf.get(i).getMasechet().equals(mDaf.getMasechet()) && myListALLDaf.get(i).getPageNumber() == mDaf.getPageNumber()){
                    myListALLDaf.get(i).setLearning(checked);
                    return;
                }
            }
        }


        private void updateChazara(int chazara) {
            updateChazaraListAndDB(chazara);
            switch(chazara) {
                case 0:
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 1:
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    whenHaveChazaraHaveLearn();
                    break;
                case 2:
                    chazara1.setChecked(true);
                    chazara3.setChecked(false);
                    whenHaveChazaraHaveLearn();
                    break;
                case 3:
                    chazara1.setChecked(true);
                    chazara2.setChecked(true);
                    whenHaveChazaraHaveLearn();
                    break;
            }
        }

        private void whenHaveChazaraHaveLearn() {
            if (!ifLearn.isChecked()){
                ifLearn.performClick();
            }
        }

        private void updateChazaraListAndDB(int chazara) {
            AppDataBase.getInstance(activity).daoLearning().updateNumOfChazara(chazara, mDaf.getIndexTypeOfStudy(),mDaf.getMasechet(), mDaf.getPageNumber());
            updateListInChazara(chazara);
        }

        private void updateListInChazara(int chazara) {
            for (int i = 0; i <myListALLDaf.size() ; i++) {
                if (myListALLDaf.get(i).getMasechet().equals(mDaf.getMasechet()) && myListALLDaf.get(i).getPageNumber() == mDaf.getPageNumber()){
                    myListALLDaf.get(i).setChazara(chazara);
                    return;
                }
            }
        }

        public void setHolder(Daf mDaf) {
            this.mDaf = mDaf;
            initWantChazara(mDaf.getWantChazara());
            ifLearn.setChecked(mDaf.isLearning());
            masechet.setText(mDaf.getMasechet());
            numDaf.setText(ConvertIntToPage.intToPage(mDaf.getPageNumber()));
            initChazara(mDaf.getChazara());
            if (mDaf.getPageDate() != null && !mDaf.getPageDate().isEmpty()) {
                date.setText(UtilsCalender.convertDateToHebrewDate(mDaf.getPageDate()));
            }else {
                date.setText("");
            }
        }


        public void initWantChazara(int numOfChazara) {
            switch(numOfChazara) {
                case 0:
                 linearLayoutChazara.setVisibility(View.GONE);
                    break;
                case 1:
                    linearLayoutChazara.setVisibility(View.VISIBLE);
                    chazara1.setVisibility(View.VISIBLE);
                    chazara2.setVisibility(View.GONE);
                    chazara3.setVisibility(View.GONE);
                    break;
                case 2:
                    linearLayoutChazara.setVisibility(View.VISIBLE);
                    chazara1.setVisibility(View.VISIBLE);
                    chazara2.setVisibility(View.VISIBLE);
                    chazara3.setVisibility(View.GONE);
                    break;
                case 3:
                    linearLayoutChazara.setVisibility(View.VISIBLE);
                    chazara1.setVisibility(View.VISIBLE);
                    chazara2.setVisibility(View.VISIBLE);
                    chazara3.setVisibility(View.VISIBLE);
                    break;
            }
        }

        private void initChazara(int chazara) {
            switch(chazara) {
                case 0:
                    chazara1.setChecked(false);
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 1:
                    chazara1.setChecked(true);
                    chazara2.setChecked(false);
                    chazara3.setChecked(false);
                    break;
                case 2:
                    chazara1.setChecked(true);
                    chazara2.setChecked(true);
                    chazara3.setChecked(false);
                    break;
                case 3:
                    chazara1.setChecked(true);
                    chazara2.setChecked(true);
                    chazara3.setChecked(true);
                    break;
            }
        }
    }

    public interface listenerOneDafAdapter {
        void initSummaryLearning(String nameLearning , String pageLearned  , String totalLearning);
    }
}

