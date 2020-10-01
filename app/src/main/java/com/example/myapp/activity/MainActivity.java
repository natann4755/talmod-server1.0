package com.example.myapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.example.model.Daf;
import com.example.myapp.R;
import com.example.myapp.dataBase.AppDataBase;
import com.example.myapp.databinding.ActivityMainBinding;
import com.example.myapp.fragment.DeleteStudyFragment;
import com.example.myapp.fragment.ShewStudyRvFragment;
import com.example.myapp.utils.Toast;

import java.util.ArrayList;

import static com.example.myapp.utils.StaticVariables.index1;
import static com.example.myapp.utils.StaticVariables.index2;
import static com.example.myapp.utils.StaticVariables.index3;


public class MainActivity extends AppCompatActivity  {

    public final static String KEY_STUDY_1 = "KEY_STUDY_1";
    public final static String KEY_STUDY_2 = "KEY_STUDY_2";
    public final static String KEY_STUDY_3 = "KEY_STUDY_3";
    private ActivityMainBinding binding;
    private ArrayList<Daf>  myStudy1;
    private ArrayList<Daf>  myStudy2;
    private ArrayList<Daf>  myStudy3;
    private ShewStudyRvFragment mShewStudyRvFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initData();
        openFragment(mShewStudyRvFragment = ShewStudyRvFragment.newInstance(myStudy1, myStudy2 , myStudy3), ShewStudyRvFragment.TAG);
        initViews();
        initListeners();
    }

    private void initData() {
        myStudy1 =  (ArrayList<Daf>) AppDataBase.getInstance(this).daoLearning().getAllLearning(1);
        myStudy2 =  (ArrayList<Daf>) AppDataBase.getInstance(this).daoLearning().getAllLearning(2);
        myStudy3 =  (ArrayList<Daf>) AppDataBase.getInstance(this).daoLearning().getAllLearning(3);
    }

    public void openFragment(Fragment myFragment, String tag) {
        getSupportFragmentManager().beginTransaction().replace(R.id.MA_frameLayout, myFragment)
                .addToBackStack(tag)
                .commit();
    }

    private void initViews() {
        initToolbar();
        initButtonsTypesStudy();
    }


    private void initToolbar() {
        Toolbar toolbar = binding.toolbarMainActivityTB;
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.add_type_of_study_MENU_I:
                if (checkIfCanOpenTypeOfStudy()) {
                    startActivity(new Intent(this, ProfileActivity.class));
                }else {
                    Toast.MyToast(this,getString(R.string.more_than_thre_types_of_study));
                }
                return true;
            case R.id.delete_type_of_study_MENU_I:
                openFragment(DeleteStudyFragment.newInstance(myStudy1, myStudy2 , myStudy3), DeleteStudyFragment.TAG);
                return true;
            case R.id.edit_profile_MENU_I:
                // TODO: edit profile
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initButtonsTypesStudy() {
        if (myStudy1 != null && myStudy1.size()>0){
            binding.typeOfStudy1BU.setText(myStudy1.get(0).getTypeOfStudy());
        }
        if (myStudy2 != null && myStudy2.size()>0){
            binding.typeOfStudy2BU.setVisibility(View.VISIBLE);
            binding.typeOfStudy2BU.setText(myStudy2.get(0).getTypeOfStudy());
        }else {
            binding.typeOfStudy2BU.setVisibility(View.GONE);
        }
        if (myStudy3 != null && myStudy3.size()>0){
            binding.typeOfStudy3BU.setVisibility(View.VISIBLE);
            binding.typeOfStudy3BU.setText(myStudy3.get(0).getTypeOfStudy());
        }else {
            binding.typeOfStudy3BU.setVisibility(View.GONE);
        }
    }

    private void initListeners() {
        binding.typeOfStudy1BU.setOnClickListener(v -> mShewStudyRvFragment.changeLearning(index1));
        binding.typeOfStudy2BU.setOnClickListener(v -> mShewStudyRvFragment.changeLearning(index2));
        binding.typeOfStudy3BU.setOnClickListener(v -> mShewStudyRvFragment.changeLearning(index3));
    }


    private boolean checkIfCanOpenTypeOfStudy() {
        ArrayList <Integer> mLeaning = (ArrayList<Integer>) AppDataBase.getInstance(this).daoLearning().getAllIndexTypeOfLeaning();
        return !mLeaning.contains(index1) || !mLeaning.contains(index2) || !mLeaning.contains(index3);
    }
}