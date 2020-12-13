package org.techtown.starmuri.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import org.techtown.starmuri.R;
import org.techtown.starmuri.Write_ac.WritingActivity;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    String text;
    Button book_title;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //프래그먼트가 죽지 않는 방법은 없다.
        //번들을 계속 주고받고 하는 방법은 없을까?
        //다른 프래그먼트에서 홈 프래그먼트로 넘어 올 때
        //번들이 비어버린다.
        //https://developer.android.com/training/basics/fragments/pass-data-between?hl=ko
        //답이 있을까 ㅜㅜㅜㅜ.. 
        if(bundle!=null) {
            text = bundle.getString("book");
            book_title = root.findViewById(R.id.book_title_home);
            homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    book_title.setText(text);
                }
            });
        }

        else {
            book_title = root.findViewById(R.id.book_title_home);
            homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(@Nullable String s) {
                    book_title.setText(s);
                }
            });
        }

        book_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("클릭");
                Intent intent = new Intent(book_title.getContext(), WritingActivity.class);// Intent 선언
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);   // Intent 시작
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });



        return root;

    }

}




