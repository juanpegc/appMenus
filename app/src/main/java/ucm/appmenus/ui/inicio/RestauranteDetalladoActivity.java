package ucm.appmenus.ui.inicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import ucm.appmenus.MainActivity;
import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.recyclers.ViewHolderFiltros;
import ucm.appmenus.recyclers.ViewHolderImagenes;
import ucm.appmenus.recyclers.ViewHolderRestaurantes;
import ucm.appmenus.ui.filtros.FiltrosFragment;
import ucm.appmenus.utils.Pair;

/**
 * Clase utilizada para mostar los detalles de un restaurante, por ejemplo con mas imagenes, filtros o reseñas
 */
public class RestauranteDetalladoActivity extends AppCompatActivity {

    private Restaurante restaurante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurante_detallado);

        restaurante = getIntent().getParcelableExtra("restaurante");

        TextView nombre = findViewById(R.id.nombreRestaurante);
        RatingBar valoracion = findViewById(R.id.valoracionRestaurante);
        TextView url = findViewById(R.id.webRestaurante);
        TextView direccion = findViewById(R.id.direccionRestaurante);
        TextView telefono = findViewById(R.id.telefonoRestaurante);
        TextView horario = findViewById(R.id.horarioRestaurante);
        Button botonResenia = findViewById(R.id.botonAniadirResenia);

        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        //imagen.setImageBitmap(restaurante.getListaImagenes().get(0));
        valoracion.setRating((float )restaurante.getValoracion());
        valoracion.setClickable(false);
        direccion.setText(restaurante.getDireccion().getValue());
        telefono.setText(restaurante.getTelefono() + "");
        horario.setText(restaurante.getHorarios());

        //Actaualiza las imagenes y filtros de los restaurantes para tener mas detalles.
        restaurante.updateImagenes();
        restaurante.updateFiltros();

        View v = getWindow().getDecorView().getRootView();
        final Observer<HashSet<String>> observerFiltros = new Observer<HashSet<String>>() {
            @Override
            public void onChanged(HashSet<String> filtros) {
                //Recycler filtros
                RecyclerAdapter.crearRecyclerGrid(FiltrosFragment.transform(filtros, false), ViewHolderFiltros.class,
                        R.id.filtrosRestauranteRecycler, R.layout.recycler_filtros, v, 3);
            }
        };
        restaurante.getLivedataFiltros().observe(this, observerFiltros);

        final Observer<ArrayList<Bitmap>> observerImagenes = new Observer<ArrayList<Bitmap>>() {
            @Override
            public void onChanged(ArrayList<Bitmap> img) {
                //Recycler imagenes
                RecyclerAdapter.crearRecyclerLineal(img, ViewHolderImagenes.class, R.id.recyclerImagenesRestaurante,
                        R.layout.recycler_imagenes, v, LinearLayoutManager.HORIZONTAL);
            }
        };
        restaurante.getliveDataImagen().observe(this, observerImagenes);

        //Boton que abre la activiry para crear una reseña de un restaurante
        botonResenia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReseniaActivity.class);
                startActivity(intent);
            }
        });
    }
}