package org.techtown.starmuri.ui.option;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.techtown.starmuri.Loading_ac.LoadingActivity;
import org.techtown.starmuri.MainActivity;
import org.techtown.starmuri.R;

public class OptionFragment extends Fragment {

    private OptionViewModel optionViewModel;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        optionViewModel =
                ViewModelProviders.of(this).get(OptionViewModel.class);
        View root = inflater.inflate(R.layout.fragment_option, container, false);
        Button log_out = root.findViewById(R.id.sign_out_button);
        //final TextView textView = root.findViewById(R.id.text_option);
        optionViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("로그 아웃 클릭");
                //user.delete();
                firebaseAuth.signOut();
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.server_client_id))
                        .requestEmail()
                        .build();
                mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
                mGoogleSignInClient.revokeAccess();
                mGoogleSignInClient.signOut();
                getActivity().finish();
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firebaseAuth = FirebaseAuth.getInstance();
        if (mAuthListener != null) {
            firebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

}

