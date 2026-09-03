package br.edu.unisenai.rangonaregua;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import br.edu.unisenai.rangonaregua.adapter.LugarAdapter;
import br.edu.unisenai.rangonaregua.data.Catalogo;
import br.edu.unisenai.rangonaregua.model.Lugar;


public class MainActivity extends AppCompatActivity implements LugarAdapter.Acao {

    static List<Lugar> listaLugares = new ArrayList<>();
    private LugarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton btNovo = findViewById(R.id.fabNovo);
        btNovo.setOnClickListener(v -> {
            Intent intent = new Intent(this, NovoLugarActivity.class);
            startActivity(intent);
        });

        // carregar base de dados
        listaLugares = Catalogo.inicial();

        // configurar o RecycleView
        RecyclerView rvLugares = findViewById(R.id.rvLugares);
        rvLugares.setLayoutManager(new LinearLayoutManager(this));

        adapter = new LugarAdapter(listaLugares, this);
        rvLugares.setAdapter(adapter);

        // configurar deslizar
        configDeslizar();
    }

    private void configDeslizar(){
        ItemTouchHelper.SimpleCallback deslizar = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
                // usado para movimentar entre os itens
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicao = viewHolder.getAdapterPosition();
                Lugar item = listaLugares.get(posicao);
                listaLugares.remove(posicao);
                adapter.notifyItemRemoved(posicao);

                // avisar
                Snackbar.make(findViewById(R.id.rvLugares),"Lugar removido", Snackbar.LENGTH_LONG).setAction("Desfazer", v -> {
                    listaLugares.add(item);
                    Catalogo.ordenarPorVotos(listaLugares);
                    adapter.notifyDataSetChanged();
                }).show();
            }
        };
        new ItemTouchHelper(deslizar).attachToRecyclerView(findViewById(R.id.rvLugares));
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void votar(Lugar lugar) {
        lugar.setVotos(lugar.getVotos()+1);
        Catalogo.ordenarPorVotos(listaLugares);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void detalhar(Lugar lugar) {
        Intent rota = new Intent(this, DetalheActivity.class);
        rota.putExtra("obj", lugar);
        startActivity(rota);
    }
}
