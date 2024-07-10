package com.gabriel.smartclass.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gabriel.smartclass.adapter.RankAdapter;
import com.gabriel.smartclass.dao.CourseDAO;
import com.gabriel.smartclass.model.Classroom;
import com.gabriel.smartclass.model.Course;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.model.InstitutionUser;
import com.gabriel.smartclass.model.RankPosition;
import com.gabriel.smartclass.model.Student;
import com.gabriel.smartclass.model.StudentGrade;

import java.util.ArrayList;
import java.util.List;

public class RankTeacherViewModel extends ViewModel {
    private Classroom classroom;
    private Course course;
    private InstitutionUser institutionUser;
    private Institution institution;
    private final RankAdapter rankAdapter = new RankAdapter();
    private MutableLiveData<List<RankPosition>> positions = new MutableLiveData<>(new ArrayList<>());

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public InstitutionUser getInstitutionUser() {
        return institutionUser;
    }

    public void setInstitutionUser(InstitutionUser institutionUser) {
        this.institutionUser = institutionUser;
    }

    public Institution getInstitution() {
        return institution;
    }

    public void setInstitution(Institution institution) {
        this.institution = institution;
    }

    public RankAdapter getRankAdapter() {
        return rankAdapter;
    }

    public MutableLiveData<List<RankPosition>> getPositions() {
        return positions;
    }

    public void setPositions(MutableLiveData<List<RankPosition>> positions) {
        this.positions = positions;
    }

    public void buildRank() {
        MutableLiveData<Double> overallGrade = new MutableLiveData<>((double) 0);
        new CourseDAO().getStudentsAsStudents(this.institution.getId(), this.course.getId(), task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                List<Student> list = task.getResult();
                for (Student student : list) {
                    overallGrade.setValue((double) 0);
                    if (student.getStudentGrades() != null) {
                        List<StudentGrade> gradesList = student.getStudentGrades();
                        for (StudentGrade grade : gradesList) {
                            if(grade.getClassroom().getId().equals(this.classroom.getId())){
                                double overGrade;
                                overGrade = overallGrade.getValue();
                                overGrade = overGrade + grade.getFinalGrade();
                                overallGrade.setValue(overGrade);
                            }
                        }
                        positions.getValue().add(new RankPosition(student.getDescription(), overallGrade.getValue()));
                    }
                }
                positions.setValue(RankPosition.make_ranking(positions.getValue()));
                rankAdapter.getList().setValue(positions.getValue());
                rankAdapter.notifyDataSetChanged();
            }
        });
    }

}
