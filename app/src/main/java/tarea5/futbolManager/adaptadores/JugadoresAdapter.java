package tarea5.futbolManager.adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import tarea5.futbolManager.R;
import tarea5.futbolManager.modelos.Jugador;

/**
 * Adaptador para el RecyclerView de jugadores en el historial de partidos
 */
public class JugadoresAdapter extends RecyclerView.Adapter<JugadoresAdapter.JugadorViewHolder> {

    // Declaración de variables
    private List<Jugador> listaJugadores; // Lista de jugadores a mostrar

    // Constructor
    public JugadoresAdapter(List<Jugador> listaJugadores) {
        this.listaJugadores = listaJugadores;
    }

    /**
     * Crea un nuevo ViewHolder para el RecyclerView
     * @param parent ViewGroup padre
     * @param viewType Tipo de vista
     * @return Nuevo JugadorViewHolder
     */
    @NonNull
    @Override
    public JugadorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del cardview
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_historico, parent, false);
        // Retornar un nuevo JugadorViewHolder
        return new JugadorViewHolder(view);
    }

    /**
     * Vincula los datos de un jugador con un ViewHolder
     * @param holder ViewHolder
     * @param position Posición del jugador en la lista
     */
    @Override
    public void onBindViewHolder(@NonNull JugadorViewHolder holder, int position) {
        Jugador jugador = listaJugadores.get(position);
        holder.textViewNombre.setText(jugador.getNombre());
        holder.textViewPosicion.setText(jugador.getPosicion());
        loadImage(holder, jugador.getImageUrl()); // Cargar la imagen del jugador desde Firebase
    }

    /**
     * Carga la imagen del jugador desde Firebase
     * @param holder ViewHolder
     * @param imagePath Ruta de la imagen
     */
    private void loadImage(@NonNull JugadorViewHolder holder, String imagePath) {
        // Si la ruta de la imagen no está vacía, cargar la imagen desde Firebase
        if (imagePath != null && !imagePath.isEmpty()) {
            // Obtener la referencia de la imagen
            StorageReference imageRef = FirebaseStorage.getInstance().getReference().child(imagePath);
            // Obtener la URL de la imagen
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Cargar la imagen en el ImageView
                Glide.with(holder.itemView.getContext())
                        .load(uri) // Cargar la imagen desde la URL
                        .into(holder.imageViewJugador); // Cargar la imagen en el ImageView
            }).addOnFailureListener(exception -> {
                // Si ocurre un error al cargar la imagen, cargar una imagen por defecto
                holder.imageViewJugador.setImageResource(R.drawable.ic_launcher_background);
            });
        } else {
            // Si la ruta de la imagen está vacía, cargar una imagen por defecto
            holder.imageViewJugador.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    /**
     * Retorna la cantidad de elementos en la lista
     * @return Cantidad de elementos
     */
    @Override
    public int getItemCount() {
        return listaJugadores.size(); // Retorna la cantidad de jugadores en la lista
    }

    /**
     * Actualiza la lista de jugadores
     * @param jugadores Nueva lista de jugadores
     */
    public void setJugadores(List<Jugador> jugadores) {
        this.listaJugadores = jugadores; // Actualiza la lista de jugadores
        notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
    }

    /**
     * Clase externa ViewHolder para los jugadores
     */
    public static class JugadorViewHolder extends RecyclerView.ViewHolder {

        // Declaración de variables
        ImageView imageViewJugador; // ImageView para la imagen del jugador
        TextView textViewNombre, textViewPosicion; // TextViews para el nombre y la posición del jugador

        /**
         * Constructor
         * @param itemView Vista del ViewHolder
         */
        public JugadorViewHolder(View itemView) {
            super(itemView);
            imageViewJugador = itemView.findViewById(R.id.imageViewJugador);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPosicion = itemView.findViewById(R.id.textViewPosicion);
        }
    }
}
