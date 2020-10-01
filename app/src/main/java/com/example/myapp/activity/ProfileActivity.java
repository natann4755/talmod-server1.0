package com.example.myapp.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.model.Daf;
import com.example.model.shas_masechtot.Shas;
import com.example.model.shas_masechtot.Seder;
import com.example.myapp.R;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.databinding.ActivityProfileBinding;
import com.example.myapp.fragment.NumberOfRepetitionsProfileFragment;
import com.example.myapp.fragment.TypeStudyProfileFragment;
import com.example.myapp.utils.ConvertIntToPage;
import com.example.myapp.utils.Toast;
import com.example.myapp.utils.UtilsCalender;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


import static com.example.myapp.utils.StaticVariables.Assets_Name;
import static com.example.myapp.utils.StaticVariables.index1;
import static com.example.myapp.utils.StaticVariables.index2;
import static com.example.myapp.utils.StaticVariables.index3;
import static com.example.myapp.utils.StaticVariables.stringDafHayomi;


public class ProfileActivity extends AppCompatActivity implements TypeStudyProfileFragment.ListenerFragmentTypeStudyProfile, NumberOfRepetitionsProfileFragment.ListenerNumberOfRepetitionsProfile {

    private ActivityProfileBinding binding;
    private ArrayList<Daf> mListLearning = new ArrayList<>();
    private Shas mAllShas;
    private String mStringTypeOfStudy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initListAllShah();
        openFragment(TypeStudyProfileFragment.newInstance((ArrayList<Seder>) mAllShas.getSeder()), TypeStudyProfileFragment.TAG);
    }

    public void initListAllShah() {
        Gson gson = new Gson();
        try {
            String txt;
            txt = convertStreamToString(this.getAssets().open(Assets_Name));
            mAllShas = gson.fromJson(txt, Shas.class);
        } catch (Exception e) {
            Log.e(getString(R.string.conversion_assets_error), e.toString());
        }
    }

    public void openFragment(Fragment myFragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_profile_FL, myFragment)
                .addToBackStack(tag)
                .commit();
    }

    public void createListTypeOfStudy(String typeOfStudy, int pages) {
        if (mStringTypeOfStudy.equals(stringDafHayomi)) {
            createListAllShas();
        } else {
            createListMasechet(typeOfStudy, pages);
        }
    }

    private void createListAllShas() {
        mListLearning.clear();
        Calendar dateDafHayomi = UtilsCalender.findDateOfStartDafHayomiEnglishDate();
        int id = checkHighestId();
        int correctIndexTypeStudy = findCorrectIndexTypeStudy();
        for (int i = 0; i < mAllShas.getSeder().size(); i++) {
            for (int j = 0; j < mAllShas.getSeder().get(i).getMasechtot().size(); j++) {
                for (int k = 2; k < (mAllShas.getSeder().get(i).getMasechtot().get(j).getPages() + 2); k++) {
                    int masechetPage = ConvertIntToPage.fixKinimTamidMidot(k, mAllShas.getSeder().get(i).getMasechtot().get(j).getName());
                    Daf mPage = new Daf(mAllShas.getSeder().get(i).getMasechtot().get(j).getName(), masechetPage, stringDafHayomi, correctIndexTypeStudy, id);
                    mPage.setPageDate(UtilsCalender.dateStringFormat(dateDafHayomi));
                    mListLearning.add(mPage);
                    dateDafHayomi.add(Calendar.DATE, 1);
                    id++;
                }
            }
        }
    }

    private int checkHighestId() {
        int id = AppDataBase.getInstance(this).daoLearning().getHighestId();
        if (id > 0) {
            return id + 1;
        } else {
            return 1;
        }
    }

    private int findCorrectIndexTypeStudy() {
        ArrayList<Integer> mLeaning = (ArrayList<Integer>) AppDataBase.getInstance(this).daoLearning().getAllIndexTypeOfLeaning();
        if (!mLeaning.contains(index1)) {
            return index1;
        } else if (!mLeaning.contains(index2)) {
            return index2;
        } else if (!mLeaning.contains(index3)) {
            return index3;
        }
        return 0;
    }


    private void createListMasechet(String masechetName, int pages) {
        mListLearning.clear();
        int id = checkHighestId();
        int correctIndexTypeStudy = findCorrectIndexTypeStudy();
        for (int i = 2; i < (pages + 2); i++) {
            mListLearning.add(new Daf(masechetName, ConvertIntToPage.fixKinimTamidMidot(i, masechetName), masechetName, correctIndexTypeStudy, id));
            id++;
        }
    }


    private void alertDialogAreYouSure() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    AppDataBase.getInstance(getBaseContext()).daoLearning().insertAllLearning(mListLearning);
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    finish();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        };
        String typeOfStudy = String.format(Locale.getDefault(),"%s %s", getString(R.string.type), mStringTypeOfStudy);
        String numberOfReps = String.format(Locale.getDefault(),"%s %d", getString(R.string.chazara), mListLearning.get(0).getWantChazara());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.you_want_create_daily_study);
        builder.setMessage(typeOfStudy + getString(R.string.break_line) + numberOfReps);
        builder.setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }


    private static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    @Override
    public void updateActivityTypeStudy(String typeOfStudy, int pages) {
        mStringTypeOfStudy = typeOfStudy;
        createListTypeOfStudy(typeOfStudy, pages);
    }

    @Override
    public void typeStudyOk() {
        if (mStringTypeOfStudy == null || mStringTypeOfStudy.isEmpty()) {
            Toast.MyToast(this, getString(R.string.you_must_choose_type_of_study));
        } else {
            openFragment(NumberOfRepetitionsProfileFragment.newInstance(), NumberOfRepetitionsProfileFragment.TAG);
        }
    }

    @Override
    public void numberOfRepOk(int numberOfRep) {
        updateListWithNumberOfRep(numberOfRep);
        alertDialogAreYouSure();
    }

    private void updateListWithNumberOfRep(int numberOfRep) {
        for (Daf daf : mListLearning) {
            daf.setWantChazara(numberOfRep);
        }
    }
}

