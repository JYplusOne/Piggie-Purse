package com.example.piggiepurse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class ShopAssistantFragment extends Fragment {
    public static final String TAG = "ShopAssistantFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shop_assistant_fragment, container, false);
        ImageButton personalCareImageButton = view.findViewById(R.id.personalcareicon);
        ImageButton clothApparelImageButton = view.findViewById(R.id.clothingapparelicon);
        ImageButton foodGrocerylImageButton = view.findViewById(R.id.foodgroceryicon);
        ImageButton FootWearImageButton = view.findViewById(R.id.footwearicon);

        personalCareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityPersonalCare.class);
                startActivity(intent);
            }
        });

        clothApparelImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityClothAparel.class);
                startActivity(intent);
            }
        });

        foodGrocerylImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityFoodGrocery.class);
                startActivity(intent);
            }
        });

        FootWearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityFootWear.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
