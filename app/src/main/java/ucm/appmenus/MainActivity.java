package ucm.appmenus;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ucm.appmenus.entities.Usuario;
import ucm.appmenus.ficheros.JSONRestaurante;
//import ucm.appmenus.login.LoginActivity;
import ucm.appmenus.recyclers.IReclycerElement;
import ucm.appmenus.recyclers.RecyclerAdapter;
import ucm.appmenus.utils.Localizacion;

/**
 * IMPORTANTE: Esta activity ya loguea al usuario desde SharedPreferences
 * */


public class MainActivity extends AppCompatActivity {

    /*
    //vars para prueba de autenticacion registro
    Button btn_login, btn_registrar;
    EditText et_name, et_email, et_password;

    //variable dodne guardaremos la info de lo que metamos en el input
    private String name = "";
    private String email = "";
    private String password = "";
    private FirebaseAuth firebaseAuth;
    private ProgressDialog ProgressDialog;
    // DatabaseReference mDatabase;
    */

    //esto estaba de antes
    private Usuario usuario;
    private NavController navController;

    /**
     * En principio no hay que hacer nada mas en esta actividad ya que tod0 se hace en los fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Loguea al usaurio para poder usar sus datos luego
        loginUsuario();

        //Importante que esté después del login de usuario o lanzará nullpointer
        setContentView(R.layout.activity_main);

        final BottomNavigationView navView = findViewById(R.id.nav_view);
        //Carga la vista de la barra inferior con las 3 ventanas que contiene
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_inicio, R.id.navigation_filtros, R.id.navigation_perfil)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    /**
     * Cambia entre 2 fragments
     * @param id: fragment al que moverse
     * @param b: datos necesarios para dicho fragment
     */
    public void changeFragment(int id, Bundle b){
        Log.d("BUNDLE", b.toString());
        navController.navigate(id, b);
    }

    public Usuario getUsuario() { return usuario; }

    /**
     * Loguea al usuario
     * Obtiene el email, nombre e imagen de SharedPreferences
     * TODO: Deberia comprobar en la BD que sea correcto(?)
     * */

    //Funcion de login local
    private void loginUsuario(){
        final SharedPreferences sp = this.getSharedPreferences(
                getString(R.string.ucm_appmenus_ficherologin), Context.MODE_PRIVATE);
        String email = sp.getString(getString(R.string.email_usuario), null);
        String nombre = sp.getString(getString(R.string.nombre_usuario), null);
        String imagen = sp.getString(getString(R.string.imagen_usuario), null);
        JSONRestaurante jsonRes = new JSONRestaurante(getApplicationContext(),
                getString(R.string.ucm_appmenus_restaurantesFavoritos),
                getString(R.string.ucm_appmenus_restaurantesFavoritos));
        this.usuario = new Usuario(email, nombre, new Localizacion(this), imagen,
                null, jsonRes.readRestaurantesJSON(), null);

    }

    /**
     * Clase estatica que crea y devuelve un recycler lineal (creada para no repetir codigo)
     * @param elementos lista con los elementos a mostrar en el recycler
     * @param viewHolder clase encargada de soportar los datos del recycler
     * @param idRecycler identificador del recycler en la vista donde se llama a esta funcion
     * @param idLayout identificador del layout donde se llama a esta funcion
     * @param v vista de la clase donde se llama a esta funcion
     * @param orientacion orientacion del layout, vertical u horizontal
     * @param <T> ViewHolder encargado de gestionar los datos proporcionados
     * @param <ELEMENT> tipo de dato proporcionado
     * @return
     */
    public static <T extends RecyclerView.ViewHolder & IReclycerElement<ELEMENT>, ELEMENT> RecyclerView crearRecyclerLineal(
            ArrayList<ELEMENT> elementos, Class<T> viewHolder, @IdRes int idRecycler, @LayoutRes int idLayout, View v,
            @RecyclerView.Orientation int orientacion){
        RecyclerView recyclerView = v.findViewById(idRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                v.getContext(), orientacion, false));

        RecyclerAdapter<T, ELEMENT> adapterRestaurantes =
                new RecyclerAdapter<T, ELEMENT>(elementos, idLayout, viewHolder);
        recyclerView.setAdapter(adapterRestaurantes);
        return recyclerView;
    }

    /**
     * Clase estatica que crea y devuelve un recycler lineal (creada para no repetir codigo)
     * @param elementos lista con los elementos a mostrar en el recycler
     * @param viewHolder clase encargada de soportar los datos del recycler
     * @param idRecycler identificador del recycler en la vista donde se llama a esta funcion
     * @param idLayout identificador del layout donde se llama a esta funcion
     * @param view vista de la clase donde se llama a esta funcion
     * @param nColums numero de columnas que tendrá el layout
     * @param <T> ViewHolder encargado de gestionar los datos proporcionados
     * @param <ELEMENT> tipo de dato proporcionado
     * @return
     */
    public static <T extends RecyclerView.ViewHolder & IReclycerElement<ELEMENT>, ELEMENT> RecyclerView crearRecyclerGrid(
            ArrayList<ELEMENT> elementos, Class<T> viewHolder, @IdRes int idRecycler, @LayoutRes int idLayout, View view,
            int nColums){
        RecyclerView recyclerView = view.findViewById(idRecycler);
        recyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), nColums));

        RecyclerAdapter<T, ELEMENT> adapterRestaurantes =
                new RecyclerAdapter<T, ELEMENT>(elementos, idLayout, viewHolder);
        recyclerView.setAdapter(adapterRestaurantes);
        return recyclerView;
    }

    /*
    //Funcion de login con la base de datos de google
    private void login(String email, String password){
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //en donde dice registro activity tendria que ir el profileActivity
                    Intent intent= new Intent(MainActivity.this,RegistroActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

     */
}