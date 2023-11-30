package com.daominh.quickmem.adapter.viewpager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import com.daominh.quickmem.R;
import com.daominh.quickmem.databinding.ItemOnboardingBinding;
import org.jetbrains.annotations.NotNull;

public class OnboardingAdapter extends PagerAdapter {

    private final Context context;

    public OnboardingAdapter(Context context) {
        this.context = context;
    }

    private final int[] onBoardingTitles = {
            R.string.onboarding_title_1,
            R.string.onboarding_title_2,
            R.string.onboarding_title_3,
            R.string.onboarding_title_4
    };

    private final int[] onBoardingImages = {
            R.drawable.onboarding_1,
            R.drawable.ic_start_svg,
            R.drawable.onboarding_3,
            R.drawable.onboarding_2
    };

    @Override
    public int getCount() {
        return onBoardingTitles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        final ItemOnboardingBinding binding = ItemOnboardingBinding.inflate(
                LayoutInflater.from(context), container, false
        );

        binding.onboardingTitleTv.setText(onBoardingTitles[position]);
        binding.onboardingIv.setImageResource(onBoardingImages[position]);

        container.addView(binding.getRoot());

        return binding.getRoot();
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((View) object);
    }
}
