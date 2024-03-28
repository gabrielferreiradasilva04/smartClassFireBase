package com.gabriel.smartclass.viewModels;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.gabriel.smartclass.adapter.InstitutionsAdapter;
import com.gabriel.smartclass.model.Institution;
import com.gabriel.smartclass.observer.EmptyRecyclerViewObserver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.gabriel.smartclass.dao.InstitutionDAO;
import java.util.ArrayList;
import java.util.List;

public class InstitutionsSearchViewModel extends ViewModel {

    private InstitutionsAdapter adapter; /*adapter responsável por realizar o controle dos dados no recyclerView*/
    private List<Institution> institutions; /*Lista de instituições que vão compor os itens dentro do adapter*/
    private InstitutionDAO dao; /*dao para a pesquisa no banco*/
    private Context context; /*contexto da aplicação */
    private EmptyRecyclerViewObserver emptyRecyclerViewObserver; /*observador para mostrar a tela de recycler vazia*/

    /*getters and setters*/
    public InstitutionsAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(InstitutionsAdapter adapter) {
        this.adapter = adapter;
    }

    public InstitutionsSearchViewModel(Context context, EmptyRecyclerViewObserver emptyRecyclerViewObserver){
        this.context = context;
        this.dao = new InstitutionDAO();
        this.institutions = new ArrayList<>();
        this.emptyRecyclerViewObserver = emptyRecyclerViewObserver;

    }
    /*constructors*/

    /*método de pesquisa onde se recebe um recyclerview para carregar os dados obtidos através da busca no banco de dados na coleção de instituições, juntamente com um evento de click
     * para capturar a instituição selecionada e mandar ela para a proxima dela onde vai ser preenchido um formulario para enviar uma solicitação de vínculo*/
    public void search(RecyclerView recyclerView, String institutionName, InstitutionsAdapter.ItemClickListener itemClickListener){
        adapter = new InstitutionsAdapter(institutions,itemClickListener);
        adapter.registerAdapterDataObserver(emptyRecyclerViewObserver);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        dao.getInstitutionByName(institutionName, new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    institutions = new ArrayList<>();
                    for (DocumentSnapshot document: task.getResult()){
                        Institution institution = document.toObject(Institution.class);
                        institution.setId(document.getId());
                        adapter.addItem(institution);
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    adapter.getInstitutionsMutableLiveData().getValue().clear();
                    adapter.notifyDataSetChanged();
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {}
        });


    }


}
