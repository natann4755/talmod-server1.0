package com.example.myapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.model.shas_masechtot.Seder;
import com.example.myapp.adapters.RecyclerViewStudyOptionsMasechetAdapter;
import com.example.myapp.adapters.RecyclerViewStudyOptionsSederAdapter;
import com.example.myapp.databinding.FragmentTypeStudyProfileBinding;

import java.util.ArrayList;

import static com.example.myapp.utils.StaticVariables.stringDafHayomi;

public class TypeStudyProfileFragment extends Fragment implements RecyclerViewStudyOptionsMasechetAdapter.ListenerCreateTypeOfStudy {
    private FragmentTypeStudyProfileBinding binding;
    public static final String TAG = TypeStudyProfileFragment.class.getSimpleName();
    private ListenerFragmentTypeStudyProfile mListener;
    private RecyclerViewStudyOptionsSederAdapter mRecyclerViewOptionsSederAdapter;
    public static String KEY_ALL_SHAS = "KEY_ALL_SHAS";
    private ArrayList<Seder> mSixSdarim;
    private boolean mOptionTalmudBavlyOpen = false;


    public static TypeStudyProfileFragment newInstance(ArrayList<Seder> mSixSdarim) {
        TypeStudyProfileFragment fragment = new TypeStudyProfileFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_ALL_SHAS, mSixSdarim);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSixSdarim = getArguments().getParcelableArrayList(KEY_ALL_SHAS);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTypeStudyProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initListener();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ListenerFragmentTypeStudyProfile) {
            mListener = (ListenerFragmentTypeStudyProfile) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void initListener() {
        binding.studyOptionDafHayomi.setOnClickListener(v -> mListener.updateActivityTypeStudy(stringDafHayomi, 2711));

        binding.studyOptionTalmudBavly.setOnClickListener(v -> {
            if (!mOptionTalmudBavlyOpen) {
                mOptionTalmudBavlyOpen = true;
                initRecyclerView();
            } else {
                mOptionTalmudBavlyOpen = false;
                binding.myRecyclerViewOptionsSederRV.setVisibility(View.GONE);
                mRecyclerViewOptionsSederAdapter.closeOthersSdarim();
            }
        });
        binding.typeStudyOkBU.setOnClickListener(v -> mListener.typeStudyOk());
    }

    private void initRecyclerView() {
        binding.myRecyclerViewOptionsSederRV.setVisibility(View.VISIBLE);
        binding.myRecyclerViewOptionsSederRV.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerViewOptionsSederAdapter = new RecyclerViewStudyOptionsSederAdapter(getContext(), mSixSdarim, this);
        binding.myRecyclerViewOptionsSederRV.setAdapter(mRecyclerViewOptionsSederAdapter);
    }

    @Override
    public void createListTypeOfStudy(String stringTypeOfStudy, int pages) {
        mListener.updateActivityTypeStudy(stringTypeOfStudy, pages);
    }

    public interface ListenerFragmentTypeStudyProfile {
        void updateActivityTypeStudy(String TypeOfStudy, int Pages);

        void typeStudyOk();
    }
}