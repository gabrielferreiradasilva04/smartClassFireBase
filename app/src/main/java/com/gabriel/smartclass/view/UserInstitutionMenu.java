package com.gabriel.smartclass.view;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.gabriel.smartclass.databinding.ActivityUserInstitutionMenuBinding;
import com.gabriel.smartclass.model.Institution;

public class UserInstitutionMenu extends AppCompatActivity {
    private Institution currentInstitution;
    private ActivityUserInstitutionMenuBinding binding;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInstitutionMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        currentInstitution = getIntent().getParcelableExtra("institution");
        Log.d("CurrentInstitution", "onCreate: "+currentInstitution.getName());
        binding.textViewG.setText(currentInstitution.getName());

    }
}