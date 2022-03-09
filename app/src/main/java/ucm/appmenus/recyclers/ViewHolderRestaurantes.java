package ucm.appmenus.recyclers;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;

import ucm.appmenus.R;
import ucm.appmenus.entities.Restaurante;

import android.graphics.Bitmap;

import androidx.recyclerview.widget.GridLayoutManager;

import ucm.appmenus.ui.filtros.FiltrosFragment;
import ucm.appmenus.utils.Pair;

public class ViewHolderRestaurantes extends RecyclerView.ViewHolder implements IReclycerElement<Restaurante>{

    private final View view;
    private Restaurante datos;

    private final TextView nombre;
    private final TextView url;
    private final ToggleButton favorito;
    private final RatingBar valoracion;
    private final TextView direccion;
    private final ImageView imagenPrinc;
    private final RecyclerView filtrosRecycler;

    public ViewHolderRestaurantes(@NonNull View view) {
        super(view);
        this.view = view;
        nombre = view.findViewById(R.id.textRestarurantNameRecycler);
        url = view.findViewById(R.id.textRestaurantURL);
        favorito = view.findViewById(R.id.toggleButtonFavRestaurantRecycler);
        valoracion = view.findViewById(R.id.ratingRestaurantRecycler);
        direccion = view.findViewById(R.id.textDireccionRestaurante);
        imagenPrinc = view.findViewById(R.id.imageRestaurantRecycler);
        filtrosRecycler = view.findViewById(R.id.filtrosRestauranteRecycler);

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Aqui se añadiria a los favoritos del usuario
            }
        });

    }

    @Override
    public void setDatos(final Restaurante restaurante) {
        nombre.setText(restaurante.getNombre());
        url.setText(restaurante.getStringURL());
        favorito.setChecked(false);
        valoracion.setRating((float )restaurante.getValoracion());
        valoracion.setClickable(false);
        direccion.setText(restaurante.getDireccion());
        //TODO: Hacer algo con la imagen
        //imagenPrincDir.setImageBitmap(BitmapFactory.decodeFile(restaurante.getimagenPrincDir()));
        final Observer<ArrayList<Bitmap>> observerImagen = new Observer<ArrayList<Bitmap>>() {
            @Override
            public void onChanged(ArrayList<Bitmap> img) {
                imagenPrinc.setImageBitmap(img.get(0));
            }
        };
        restaurante.getliveDataImagen().observe((LifecycleOwner) view.getContext(), observerImagen);

        //Actualiza el recycler cuando se reciben los datos
        final Observer<HashSet<String>> observerFiltros = new Observer<HashSet<String>>() {
            @Override
            public void onChanged(HashSet<String> filtros) {
                //Recycler filtros
                filtrosRecycler.setLayoutManager(new GridLayoutManager(view.getContext(), 3));
                RecyclerAdapter<ViewHolderFiltros, Pair<String, Boolean>> adapterFiltros = new RecyclerAdapter<>(
                        FiltrosFragment.transform(filtros, false),
                        R.layout.recycler_filtros, ViewHolderFiltros.class);
                filtrosRecycler.setAdapter(adapterFiltros);
            }
        };
        restaurante.getLivedataFiltros().observe((LifecycleOwner) view.getContext(), observerFiltros);
    }

    @Override
    public Restaurante getDatos() { return datos; }

    /*
    public static class RecyclerTest extends AbstractViewHolder<Restaurante>{

        private View view;

        private final TextView nombre;
        private final TextView url;
        private final ToggleButton favorito;
        private final RatingBar valoracion;
        private final ImageView imagenPrincDir;
        private final RecyclerView filtrosRecycler;

        public RecyclerTest(@NonNull View view) {
            super(view);
            this.view = view;
            nombre = view.findViewById(R.id.textRestarurantNameRecycler);
            url = view.findViewById(R.id.textRestaurantURL);
            favorito = view.findViewById(R.id.toggleButtonFavRestaurantRecycler);
            valoracion = view.findViewById(R.id.ratingRestaurantRecycler);
            imagenPrincDir = view.findViewById(R.id.imageRestaurantRecycler);
            filtrosRecycler = view.findViewById(R.id.filtrosRestauranteRecycler);

            favorito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Aqui se añadiria a los favoritos del usuario
                }
            });
        }

        @Override
        public void setDatos(Restaurante restaurante) {
            nombre.setText(restaurante.getNombre());
            url.setText(restaurante.getStringURL());
            favorito.setChecked(false);
            valoracion.setRating((float )restaurante.getValoracion());
            valoracion.setClickable(false);
            //TODO: Hacer algo con la imagen
            //imagenPrincDir.setImageBitmap(BitmapFactory.decodeFile(restaurante.getimagenPrincDir()));
            //Recycler filtros
            filtrosRecycler.setLayoutManager(new LinearLayoutManager(
                    view.getContext(), LinearLayoutManager.HORIZONTAL, false));
            //Create adapter
            FiltrosRecyclerAdapter adapterFiltros = new FiltrosRecyclerAdapter(
                    restaurante.getFiltros().toArray(new String[0]), true);
            //Set the adapter
            filtrosRecycler.setAdapter(adapterFiltros);
        }
    }

     */
}