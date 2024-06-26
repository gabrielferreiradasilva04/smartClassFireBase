package com.gabriel.smartclass.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private boolean showOnlyCourseView;
    private boolean enableCourseAccess;
    private DefaultClickListener<Course> clickListenerRemove;
    private DefaultClickListener<Course> clickListenerEdit;
    private DefaultClickListener<Course> clickListenerSubjects;
    private DefaultClickListener<Course> clickListenerAccess;

    public boolean isShowOnlyCourseView() {
        return showOnlyCourseView;
    }

    public boolean isEnableCourseAccess() {
        return enableCourseAccess;
    }

    public void setEnableCourseAccess(boolean enableCourseAccess) {
        this.enableCourseAccess = enableCourseAccess;
    }

    public DefaultClickListener<Course> getClickListenerAccess() {
        return clickListenerAccess;
    }

    public void setClickListenerAccess(DefaultClickListener<Course> clickListenerAccess) {
        this.clickListenerAccess = clickListenerAccess;
    }

    public CourseRCAdapter(){
        this.mutableliveDataCourse = new MutableLiveData<>(new ArrayList<>());
        this.tIds = new HashSet<>();
    }

    public void setShowOnlyCourseView(boolean showOnlyCourseView) {
        this.showOnlyCourseView = showOnlyCourseView;
    }

    public boolean isShowOnlyMenuOptions() {
        return showOnlyCourseView;
    }

    public void setShowOnlyMenuOptions(boolean showOnlyMenuOptions) {
        this.showOnlyCourseView = showOnlyMenuOptions;
    }

    public DefaultClickListener<Course> getClickListenerEdit() {
        return clickListenerEdit;
    }

    public void setClickListenerEdit(DefaultClickListener<Course> clickListenerEdit) {
        this.clickListenerEdit = clickListenerEdit;
    }

    public DefaultClickListener<Course> getClickListenerSubjects() {
        return clickListenerSubjects;
    }

    public void setClickListenerSubjects(DefaultClickListener<Course> clickListenerSubjects) {
        this.clickListenerSubjects = clickListenerSubjects;
    }

    public MutableLiveData<List<Course>> getMutableliveDataCourse() {
        return mutableliveDataCourse;
    }

    public HashSet<String> gettIds() {
        return tIds;
    }

    public DefaultClickListener<Course> getClickListenerRemove() {
        return clickListenerRemove;
    }

    public void setClickListenerRemove(DefaultClickListener<Course> clickListenerRemove) {
        this.clickListenerRemove = clickListenerRemove;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_courseslayout, parent, false);
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
            Button buttonSubjects = holder.itemView.findViewById(R.id.course_recycler_view_buttonsubjects);
            TextView textDescription = holder.itemView.findViewById(R.id.course_recyclerview_title);
            textDescription.setText(description);
            if(showOnlyCourseView){
                buttonRemoveItem.setVisibility(View.GONE);
                buttonEditItem.setVisibility(View.GONE);
                buttonSubjects.setVisibility(View.GONE);
            }
            if(!showOnlyCourseView){
                if(clickListenerRemove != null && clickListenerEdit != null && clickListenerSubjects != null){
                    buttonRemoveItem.setOnClickListener(view -> clickListenerRemove.onClick(mutableliveDataCourse.getValue().get(position)));
                    buttonEditItem.setOnClickListener(view -> clickListenerEdit.onClick(mutableliveDataCourse.getValue().get(position)));
                    buttonSubjects.setOnClickListener(view -> clickListenerSubjects.onClick(mutableliveDataCourse.getValue().get(position)));
                }
            }
            if(enableCourseAccess){
                if(clickListenerAccess != null){
                    holder.itemView.setOnClickListener(view -> clickListenerAccess.onClick(mutableliveDataCourse.getValue().get(position)));
                }
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
