package tarea5.futbolManager.adaptadores;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class FadeInItemDecorator extends RecyclerView.ItemDecoration {

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

    private float getFadeFactor(View child, RecyclerView parent) {
        float center = (parent.getHeight() - child.getHeight()) / 2f;
        float distance = Math.abs(center - (child.getTop() + child.getHeight() / 2f));
        // Ajusta el cálculo aquí para cambiar la opacidad inicial y cómo cambia con la distancia
        float factor = 1f - Math.min(1f, distance / center);
        // Para hacer el elemento central un poco más opaco y aumentar su opacidad hacia el centro,
        // puedes invertir el factor o ajustarlo según tu preferencia.
        // Por ejemplo, para hacerlos más transparentes cuando están lejos del centro y más opacos cerca del centro:
        return 0.6f + 0.6f * factor; // Ajusta esto según la necesidad de opacidad inicial y final.
    }
}