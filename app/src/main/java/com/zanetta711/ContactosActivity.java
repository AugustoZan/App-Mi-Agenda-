package com.zanetta711;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.app.AlertDialog;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactosActivity extends AppCompatActivity {

    private List<Contacto> contactos;
    private ContactosAdapter adapter;
    private LinearLayout emptyListLayout;
    private ActivityResultLauncher<Intent> agregarContactoLauncher;
    private DataBase databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        // Inicializar la base de datos
        databaseHelper = new DataBase(this);

        // Inicializar la lista de contactos
        contactos = databaseHelper.obtenerTodosLosContactos();

        // Inicializar el RecyclerView
        RecyclerView recyclerView = findViewById(R.id.lista_contactos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Crear el adaptador y pasar la referencia de la actividad
        adapter = new ContactosAdapter(contactos, this);
        recyclerView.setAdapter(adapter);

        emptyListLayout = findViewById(R.id.emptyListLayout);
        updateEmptyListVisibility();

        setupSearchView();

        agregarContactoLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            String nombre = data.getStringExtra("nombre");
                            String apellido = data.getStringExtra("apellido");
                            String telefono = data.getStringExtra("telefono");
                            String domicilio = data.getStringExtra("domicilio");
                            String genero = data.getStringExtra("genero");

                            // Crear el nuevo contacto y agregarlo a la base de datos
                            Contacto nuevoContacto = new Contacto(nombre, apellido, telefono, domicilio, genero);
                            databaseHelper.agregarContacto(nuevoContacto);
                            contactos.add(nuevoContacto);
                            adapter.notifyItemInserted(contactos.size() - 1);
                            updateEmptyListVisibility();
                        }
                    }
                });

        // Configurar el botón de cerrar sesión
        ImageButton cerrarSesionButton = findViewById(R.id.cerrar_sesion);
        cerrarSesionButton.setOnClickListener(v -> cerrarSesion());
    }
    public void showOptionsDialog(Contacto contacto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Configuración del contacto")
                .setItems(new CharSequence[]{"Modificar", "Eliminar"}, (dialog, which) -> {
                    if (which == 0) {
                        modificarContacto(contacto);
                    } else if (which == 1) {
                        eliminarContacto(contacto.getTelefono());
                    }
                })
                .show();
    }
    public void modificarContacto(Contacto contacto) {
        Intent intent = new Intent(this, AgregarContactoActivity.class);
        intent.putExtra("contacto", contacto); // Envía el contacto actual para editar
        agregarContactoLauncher.launch(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Contacto contactoModificado = (Contacto) data.getSerializableExtra("contactoModificado");
            if (contactoModificado != null) {
                // Encuentra el índice del contacto que se está modificando
                int index = -1;
                for (int i = 0; i < contactos.size(); i++) {
                    if (contactos.get(i).getTelefono().equals(contactoModificado.getTelefono())) {
                        index = i;
                        break;
                    }
                }
                // Si se encontró el contacto, actualízalo
                if (index != -1) {
                    contactos.set(index, contactoModificado); // Actualiza el contacto en la lista
                    databaseHelper.actualizarContacto(contactoModificado, contactoModificado.getTelefono());
                    adapter.notifyItemChanged(index); // Notifica al adaptador que el elemento ha cambiado
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void eliminarContacto(String telefono) {
        databaseHelper.eliminarContacto(telefono);
        // Actualizar la lista de contactos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            contactos.removeIf(contacto -> contacto.getTelefono().equals(telefono)); // Eliminar el contacto de la lista
        }
        adapter.notifyDataSetChanged(); // Notificar al adaptador
        updateEmptyListVisibility();
    }
    private void updateEmptyListVisibility() {
        emptyListLayout.setVisibility(contactos.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void cerrarSesion() {
        // Cambiar el estado de autenticación en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", false);
        editor.apply();

        // Regresar a MainActivity
        Intent intent = new Intent(ContactosActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Cierra ContactosActivity
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.buscar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContactList(newText);
                return false;
            }
        });
    }
    //Función para buscar contactos
    private void filterContactList(String query) {
        List<Contacto> filteredContactos = new ArrayList<>();
        for (Contacto contacto : contactos) {
            if (contacto.getNombre().toLowerCase().contains(query.toLowerCase()) ||
                    contacto.getApellido().toLowerCase().contains(query.toLowerCase()) ||
                    contacto.getTelefono().toLowerCase().contains(query.toLowerCase())) {
                filteredContactos.add(contacto);
            }
        }
        adapter.setContactos(filteredContactos);
        emptyListLayout.setVisibility(filteredContactos.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void agregarContacto(View view) {
        Intent intent = new Intent(this, AgregarContactoActivity.class);
        agregarContactoLauncher.launch(intent);
    }


}