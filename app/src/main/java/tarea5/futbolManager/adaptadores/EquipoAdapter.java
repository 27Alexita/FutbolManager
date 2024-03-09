package tarea5.futbolManager.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import tarea5.futbolManager.R;
import tarea5.futbolManager.modelos.Jugador;

public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder> {

    private List<Jugador> jugadores;
    private Context context;
    private OnContactClickListener listener;

    public EquipoAdapter(List<Jugador> jugadores, Context context) {
        this.jugadores = jugadores;
        this.context = context;
    }

    @NonNull
    @Override
    public EquipoAdapter.EquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_equipo, parent, false);
        return new EquipoViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EquipoAdapter.EquipoViewHolder holder, int position) {
        Jugador jugador = jugadores.get(position);
        holder.textViewNombre.setText(jugador.getNombre());
        holder.textViewPosicion.setText(jugador.getPosicion());
        holder.imageViewJugador.setImageResource(jugador.getImageId());

        // Configurar la imagen de la estrella según si el jugador está seleccionado o no
        if (jugador.isConvocado()) {
            holder.imageViewConvocado.setImageResource(R.drawable.icono_convocado);
        } else {
            holder.imageViewConvocado.setImageResource(R.drawable.icono_no_convocado);
        }

        // Establecer el listener para el clic en la imagen de la estrella
        holder.imageViewConvocado.setOnClickListener(view -> {
            // Cambiar el estado de selección del jugador
            boolean nuevoEstado = !jugador.isConvocado();
            jugador.setConvocado(nuevoEstado);

            // Cambiar el recurso de la imagen de la estrella según el nuevo estado
            holder.imageViewConvocado.setImageResource(nuevoEstado ? R.drawable.icono_convocado : R.drawable.icono_no_convocado);

            // Notificar al adaptador que los datos han cambiado
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return jugadores.size();
    }

    public void setOnContactClickListener(OnContactClickListener listener){
        this.listener = listener;
    }

    public interface OnContactClickListener{
        void onContactClick(int position);
    }

    public static class EquipoViewHolder extends RecyclerView.ViewHolder{

        public TextView textViewNombre, textViewPosicion;
        public ImageView imageViewJugador, imageViewConvocado;
        public CardView cardViewEquipo;

        public EquipoViewHolder(@NonNull View itemView, final OnContactClickListener listener) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPosicion = itemView.findViewById(R.id.textViewPosicion);
            imageViewJugador = itemView.findViewById(R.id.imageViewJugador);
            imageViewConvocado = itemView.findViewById(R.id.imageViewConvocado);
            cardViewEquipo = itemView.findViewById(R.id.cardViewEquipo);

            imageViewConvocado.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onContactClick(position);
                }
            });
        }
    }
}
