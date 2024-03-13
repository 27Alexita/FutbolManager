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

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import tarea5.futbolManager.R;
import tarea5.futbolManager.modelos.Jugador;

/**
 * Adaptador para el RecyclerView que muestra la lista de jugadores en el equipo
 */
public class EquipoAdapter extends RecyclerView.Adapter<EquipoAdapter.EquipoViewHolder> {

    // Declaración de variables
    private List<Jugador> jugadores; // Lista de jugadores
    private Context context; // Contexto de la aplicación
    private OnContactClickListener listener; // Listener para el clic en el ícono de la estrella

    // Constructor
    public EquipoAdapter(List<Jugador> jugadores, Context context) {
        this.jugadores = jugadores;
        this.context = context;
    }

    /**
     * Método para crear una nueva instancia de EquipoViewHolder
     * @param parent ViewGroup
     * @param viewType int
     * @return EquipoViewHolder
     */
    @NonNull
    @Override
    public EquipoAdapter.EquipoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del CardView
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_equipo, parent, false);
        // Crear y retornar una nueva instancia de EquipoViewHolder
        return new EquipoViewHolder(view, listener);
    }

    /**
     * Método para enlazar los datos de un jugador con las vistas del ViewHolder
     * @param holder EquipoViewHolder
     * @param position int
     */
    @Override
    public void onBindViewHolder(@NonNull EquipoAdapter.EquipoViewHolder holder, int position) {
        // Obtener el jugador en la posición dada
        Jugador jugador = jugadores.get(position);
        // Configurar las vistas del ViewHolder con los datos del jugador
        setupPlayerInfo(holder, jugador);
        // Cargar la imagen del jugador
        setupPlayerImage(holder, jugador);
        // Configurar el clic en el ícono de la estrella
        setupClickListener(holder, position);
    }

    /**
     * Método para configurar las vistas del ViewHolder con los datos del jugador
     * @param holder EquipoViewHolder
     * @param jugador Jugador
     */
    private void setupPlayerInfo(@NonNull EquipoAdapter.EquipoViewHolder holder, Jugador jugador) {
        holder.textViewNombre.setText(jugador.getNombre());
        holder.textViewPosicion.setText(jugador.getPosicion());
        holder.imageViewConvocado.setImageResource(jugador.isConvocado() ? R.drawable.icono_convocado : R.drawable.icono_no_convocado);
    }

    /**
     * Método para cargar la imagen del jugador. La imagen se cargará desde la base de datos de Firebase
     * @param holder EquipoViewHolder
     * @param jugador Jugador
     */
    private void setupPlayerImage(@NonNull EquipoAdapter.EquipoViewHolder holder, Jugador jugador) {
        // Obtener la referencia de almacenamiento de Firebase para la imagen del jugador
        String imageRef = jugador.getImageUrl();
        // Cargar la imagen del jugador desde Firebase
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(imageRef);

        // Cargar la imagen del jugador en el ImageView
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            // Cargar la imagen del jugador en el ImageView usando Glide
            Glide.with(context)
                    .load(uri)// Cargar la imagen desde la URI obtenida
                    .into(holder.imageViewJugador); // Cargar la imagen en el ImageView
        }).addOnFailureListener(e -> { // Manejar el error
            // Colocar una imagen predeterminada
            holder.imageViewJugador.setImageResource(R.drawable.ic_launcher_background);
        });
    }

    /**
     * Método para configurar el clic en el ícono de la estrella
     * @param holder EquipoViewHolder
     * @param position int
     */
    private void setupClickListener(@NonNull EquipoAdapter.EquipoViewHolder holder, int position) {
        // Configurar el clic en el ícono de la estrella
        holder.imageViewConvocado.setOnClickListener(view -> {
            // Notificar al listener que se ha hecho clic en el ícono de la estrella
            if (listener != null) {
                listener.onContactClick(position);
            }
            // Animar el ícono de la estrella
            animateStar(holder.imageViewConvocado);
        });
    }

    /**
     * Método para animar el ícono de la estrella
     * @param imageView ImageView
     */
    private void animateStar(ImageView imageView) {
        // Animar el ícono de la estrella
        imageView.animate()
                .rotationBy(360f) // Rotar el ícono 360 grados
                .setDuration(500) // Duración de la animación
                .start(); // Iniciar la animación
    }

    /**
     * Método para actualizar la lista de jugadores en el adaptador
     * @param nuevosJugadores List<Jugador>
     */
    public void setEquipos(List<Jugador> nuevosJugadores) {
        this.jugadores = nuevosJugadores; // Actualizar la lista de jugadores
        notifyDataSetChanged(); // Notificar al adaptador para refrescar la lista
    }

    /**
     * Método para obtener la cantidad de elementos en la lista de jugadores
     * @return int
     */
    @Override
    public int getItemCount() {
        return jugadores.size();
    }

    /**
     * Método para establecer el listener para el clic en el ícono de la estrella
     * @param listener OnContactClickListener
     */
    public void setOnContactClickListener(OnContactClickListener listener){
        this.listener = listener;
    }

    /**
     * Interfaz para el clic en el ícono de la estrella
     */
    public interface OnContactClickListener{
        void onContactClick(int position);
    }

    /**
     * Clase interna para el ViewHolder del adaptador
     */
    public static class EquipoViewHolder extends RecyclerView.ViewHolder{

        // Declaración de variables
        public TextView textViewNombre, textViewPosicion; // Vistas para el nombre y la posición del jugador
        public ImageView imageViewJugador, imageViewConvocado; // Vistas para la imagen del jugador y el ícono de la estrella
        public CardView cardViewEquipo; // Vista para el CardView del jugador

        /**
         * Constructor
         * @param itemView View
         * @param listener OnContactClickListener
         */
        public EquipoViewHolder(@NonNull View itemView, final OnContactClickListener listener) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewPosicion = itemView.findViewById(R.id.textViewPosicion);
            imageViewJugador = itemView.findViewById(R.id.imageViewJugador);
            imageViewConvocado = itemView.findViewById(R.id.imageViewConvocado);
            cardViewEquipo = itemView.findViewById(R.id.cardViewEquipo);
        }
    }
}
