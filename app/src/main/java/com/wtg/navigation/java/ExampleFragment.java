package com.wtg.navigation.java;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.brent.navigation.Navigation;
import com.wtg.navigation.R;

import java.util.Random;

public class ExampleFragment extends Fragment {

    @NonNull
    static ExampleFragment newInstance() {
        return newInstance(R.color.random_background_0 + new Random().nextInt(8));
    }

    @NonNull
    static ExampleFragment newInstance(@ColorRes int color) {
        ExampleFragment fragment = new ExampleFragment();

        Bundle arguments = new Bundle();
        arguments.putInt("color", color);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_example, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            view.setBackgroundResource(arguments.getInt("color", R.color.random_background_0));
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int backStackCount = getFragmentManager() == null ?
                0 : getFragmentManager().getBackStackEntryCount();

        TextView exampleDepth = view.findViewById(R.id.exampleDepth);
        exampleDepth.setText(getString(R.string.backstack_count, backStackCount));

        view.findViewById(R.id.examplePresent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowNextFragment();
            }
        });

        view.findViewById(R.id.examplePresentUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRaiseNextFragment();
            }
        });

        view.findViewById(R.id.examplePresentClearStack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowNextFragmentAndClear();
            }
        });

        view.findViewById(R.id.exampleSlide).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSlideNextFragment();
            }
        });

        view.findViewById(R.id.exampleSlideClearStack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSlideNextFragmentAndClear();
            }
        });


        View popButton = view.findViewById(R.id.examplePop);
        if (backStackCount == 0) {
            popButton.setVisibility(View.GONE);
        } else {
            popButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popBackStack();
                }
            });
        }
    }

    private void disableButtons() {
        View view = getView();
        if(view != null) {
            view.findViewById(R.id.examplePresent).setEnabled(false);
            view.findViewById(R.id.examplePresentUp).setEnabled(false);
            view.findViewById(R.id.examplePresentClearStack).setEnabled(false);
            view.findViewById(R.id.examplePop).setEnabled(false);
            view.findViewById(R.id.exampleSlide).setEnabled(false);
            view.findViewById(R.id.exampleSlideClearStack).setEnabled(false);
        }
    }

    private void onShowNextFragment() {
        disableButtons();
        Navigation.pushFragment(this, newInstance());
    }

    private void onShowNextFragmentAndClear() {
        disableButtons();
        Navigation.clearBackStack(this);
        Navigation.pushFragment(this, newInstance());
    }

    private void onRaiseNextFragment() {
        disableButtons();
        Navigation.pushFragment(this, newInstance(), R.anim.fragment_up, R.anim.fragment_down);
    }

    private void onSlideNextFragment() {
        disableButtons();
        Navigation.slideFragment(this, newInstance());
    }

    private void onSlideNextFragmentAndClear() {
        disableButtons();
        Navigation.clearBackStack(this);
        Navigation.slideFragment(this, newInstance());
    }

    private void popBackStack() {
        if(getActivity() != null) {
            getActivity().onBackPressed();
        }
    }
}
