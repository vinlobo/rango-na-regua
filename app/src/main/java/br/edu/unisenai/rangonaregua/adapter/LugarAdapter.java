package br.edu.unisenai.rangonaregua.adapter;

import static android.view.View.inflate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.edu.unisenai.rangonaregua.R;
import br.edu.unisenai.rangonaregua.model.Lugar;

public class LugarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // interface para tratar os cliques
    public interface Acao {
        void votar(Lugar lugar);
        void detalhar(Lugar lugar);
    }

    private Acao acao;
    // --fim

    private List<Lugar> lugares;

    private static final int CARD_LIDER = 0;
    private static final int CARD_NORMAL = 1;

    public LugarAdapter(List<Lugar> listalugares, Acao evento){
        this.lugares = listalugares;
        this.acao = evento;
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0?CARD_LIDER:CARD_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CARD_LIDER){
            View tela = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lider, parent, false);
            return new ViewHolderlider(tela);
        } else {
            View tela = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_lugar, parent, false);
            return new ViewHolder(tela);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Lugar item = lugares.get(position);

        if (holder instanceof ViewHolderlider) {
            ((ViewHolderlider)holder).txtNomeLider.setText(item.getNome());
            ((ViewHolderlider)holder).txtCategoriaLider.setText("R$ "+item.getPrecoMedio());
            ((ViewHolderlider)holder).txtVotosLider.setText(item.getVotos()+" votos");
            ((ViewHolderlider)holder).btnVotarLider.setOnClickListener(v -> {
                acao.votar(item);
            });

            ((ViewHolderlider)holder).itemView.setOnClickListener(v -> {
                acao.detalhar(item);
            });
        } else {
            ((ViewHolder)holder).txtPosicao.setText(String.valueOf(position+1));
            ((ViewHolder)holder).txtNome.setText(item.getNome());
            ((ViewHolder)holder).txtCategoria.setText("R$ "+item.getPrecoMedio());
            ((ViewHolder)holder).txtVotos.setText(item.getVotos()+" votos");
            ((ViewHolder)holder).btnVotar.setOnClickListener(v -> {
                acao.votar(item);
            });

            ((ViewHolder)holder).itemView.setOnClickListener(v -> {
                acao.detalhar(item);
            });
        }

    }

    @Override
    public int getItemCount() {
        return lugares.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPosicao, txtNome, txtCategoria, txtPreco, txtVotos;
        Button btnVotar;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            txtPosicao = itemView.findViewById(R.id.txtPosicao);
            txtNome = itemView.findViewById(R.id.txtNome);
            txtCategoria = itemView.findViewById(R.id.txtCategoria);
            txtPreco = itemView.findViewById(R.id.txtPreco);
            txtVotos = itemView.findViewById(R.id.txtVotos);
            btnVotar = itemView.findViewById(R.id.btnVotar);
        }
    }

    public class ViewHolderlider extends RecyclerView.ViewHolder {
        TextView txtNomeLider, txtCategoriaLider, txtPrecoLider, txtVotosLider;
        Button btnVotarLider;

        public ViewHolderlider(@NonNull View itemView){
            super(itemView);
            txtNomeLider = itemView.findViewById(R.id.txtNomeLider);
            txtCategoriaLider = itemView.findViewById(R.id.txtCategoriaLider);
            txtPrecoLider = itemView.findViewById(R.id.txtPrecoLider);
            txtVotosLider = itemView.findViewById(R.id.txtVotosLider);
            btnVotarLider = itemView.findViewById(R.id.btnVotarLider);
        }
    }
}
