package com.gabriel.smartclass.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.gabriel.smartclass.R;
import com.gabriel.smartclass.adapter.ViewHolders.DefaultViewHolder;
import com.gabriel.smartclass.adapter.interfaces.DefaultClickListener;
import com.gabriel.smartclass.model.Course;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CourseRCAdapter extends RecyclerView.Adapter {
    private final MutableLiveData<List<Course>> mutableliveDataCourse;
    private final HashSet<String> tIds;
    private DefaultClickListener<Course> clickListener;

    public CourseRCAdapter(){
        this.mutableliveDataCourse = new MutableLiveData<>(new ArrayList<>());
        this.tIds = new HashSet<>();
    }

    public MutableLiveData<List<Course>> getMutableliveDataCourse() {
        return mutableliveDataCourse;
    }

    public HashSet<String> gettIds() {
        return tIds;
    }

    public DefaultClickListener<Course> getClickListener() {
        return clickListener;
    }

    public void setClickListener(DefaultClickListener<Course> clickListener) {
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_recyclerview_layout, parent, false);
        return new DefaultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        try{
            holder.itemView.setAlpha(0f);
            holder.itemView.animate().alpha(1f).setDuration(300).start();
            String description = mutableliveDataCourse.getValue().get(position).getName();
            ImageButton buttonRemoveItem = holder.itemView.findViewById(R.id.course_recycler_view_buttonremove);
            ImageButton buttonEditItem = holder.itemView.findViewById(R.id.course_recycler_view_buttonedit);
            TextView textDescription = holder.itemView.findViewById(R.id.course_recyclerview_title);
            textDescription.setText(description);
            if(clickListener != null){
                buttonRemoveItem.setOnClickListener(view ->{
                    clickListener.onClick(mutableliveDataCourse.getValue().get(position));
                });
                buttonEditItem.setOnClickListener(view ->{
                    clickListener.onClick(mutableliveDataCourse.getValue().get(position));
                });
            }
        }catch (NullPointerException e){
            Log.d("ADAPTER ERROR", "onBindViewHolder: valor nulo encontrado no adapter");
        }
    }

    @Override
    public int getItemCount() {
        if(mutableliveDataCourse.getValue() != null){
            return mutableliveDataCourse.getValue().size();
        }
        return 0;
    }
    public void addItem(@NotNull Course Course){
        if(!tIds.contains(Course.getId())){
            tIds.add(Course.getId());
            mutableliveDataCourse.getValue().add(Course);
        }
    }
    public void removeItem(@NonNull Course Course){
        if(mutableliveDataCourse.getValue().contains(Course)){
            int removedPosition = mutableliveDataCourse.getValue().indexOf(Course);
            mutableliveDataCourse.getValue().remove(Course);
            notifyItemRemoved(removedPosition);
            this.notifyItemRangeChanged(removedPosition, this.getItemCount());
        }

    }
    
}
