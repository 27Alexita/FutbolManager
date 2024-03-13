package tarea5.futbolManager.modelos;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Añade una decoración a un RecyclerView para que los elementos se desvanezcan a medida que se desplazan
 * hacia arriba o hacia abajo.
 */
public class FadeInItemDecorator extends RecyclerView.ItemDecoration {

    /**
     * Dibuja sobre los elementos del RecyclerView para que se desvanezcan a medida que se desplazan
     * hacia arriba o hacia abajo.
     * @param c El Canvas sobre el que se dibuja.
     * @param parent El RecyclerView al que se le está añadiendo la decoración.
     * @param state El estado actual del RecyclerView.
     */
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            float factor = getFadeFactor(child, parent);
            child.setAlpha(factor);
        }
    }

    /**
     * Calcula el factor de desvanecimiento para un elemento del RecyclerView.
     * @param child El elemento del RecyclerView.
     * @param parent El RecyclerView al que pertenece el elemento.
     * @return El factor de desvanecimiento para el elemento.
     */
    private float getFadeFactor(View child, RecyclerView parent) {
        float center = (parent.getHeight() - child.getHeight()) / 2f;
        float distance = Math.abs(center - (child.getTop() + child.getHeight() / 2f));
        // Ajusta el cálculo aquí para cambiar la opacidad inicial y cómo cambia con la distancia
        float factor = 1f - Math.min(1f, distance / center);
        // Para hacer el elemento central un poco más opaco y aumentar su opacidad hacia el centro,
        // se puede invertir el factor o ajustarlo según tu preferencia.
        // Por ejemplo, para hacerlos más transparentes cuando están lejos del centro y más opacos cerca del centro:
        return 0.55f + 0.55f * factor; // Ajusta esto según la necesidad de opacidad inicial y final.
    }
}