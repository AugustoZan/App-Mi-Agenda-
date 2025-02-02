package com.zanetta711;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AgregarContactoActivity extends AppCompatActivity {
    private EditText nombreEditText;
    private EditText apellidoEditText;
    private EditText telefonoEditText;
    private RadioGroup generoRadioGroup;
    private EditText domicilioEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        // Inicializar los componentes de la interfaz
        ImageButton flechaVolver = findViewById(R.id.flecha_volver);
        flechaVolver.setOnClickListener(v -> mostrarDialogoDeConfirmacion());

        nombreEditText = findViewById(R.id.nombre_edit_text);
        apellidoEditText = findViewById(R.id.apellido_edit_text);
        telefonoEditText = findViewById(R.id.telefono_edit_text);
        generoRadioGroup = findViewById(R.id.genero_radio_group);
        domicilioEditText = findViewById(R.id.domicilio_edit_text);

        // Obtener el TextView donde se mostrará el título
        TextView tituloTextView = findViewById(R.id.titulo_text_view); // Asegúrate de que el ID coincida con el de tu layout

        // Obtener el contacto si existe
        Contacto contacto = (Contacto) getIntent().getSerializableExtra("contacto");
        if (contacto != null) {
            // Si el contacto existe, estamos en modo de modificación
            tituloTextView.setText(R.string.titulo_modificar);

            // Llenar los campos con los datos del contacto
            nombreEditText.setText(contacto.getNombre());
            apellidoEditText.setText(contacto.getApellido());
            telefonoEditText.setText(contacto.getTelefono());
            domicilioEditText.setText(contacto.getDomicilio());

            // Establecer el género seleccionado
            if (contacto.getGenero().equals("Masculino")) {
                generoRadioGroup.check(R.id.genero_masculino);
            } else {
                generoRadioGroup.check(R.id.genero_femenino);
            }
        } else {
            // Si no hay contacto, estamos en modo de agregar
            tituloTextView.setText(R.string.titulo_agregar);
        }

        // Obtener el botón de la palomita
        ImageButton botonCheck = findViewById(R.id.boton_check);
        botonCheck.setOnClickListener(v -> {
            // Recibe los valores de los inputs del layout de agregar contactos
            String nombre = nombreEditText.getText().toString();
            String apellido = apellidoEditText.getText().toString();
            String telefono = telefonoEditText.getText().toString();
            String domicilio = domicilioEditText.getText().toString();
            int generoId = generoRadioGroup.getCheckedRadioButtonId();
            String genero = "";
            if (generoId != -1) {
                RadioButton radioButton = findViewById(generoId);
                genero = radioButton.getText().toString();
            }

            // Verifica que los campos nombre y género estén completos. También verifica que el número de teléfono tenga 10 dígitos.
            if (nombre.isEmpty() || telefono.length() != 10 || genero.isEmpty()) {
                // Soltará mensajes pidiendo al usuario ingresar los valores correspondientes, según el caso.
                if (nombre.isEmpty() || genero.isEmpty()) {
                    Toast.makeText(AgregarContactoActivity.this, "Por favor, complete al menos nombre, número de tel. y género", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AgregarContactoActivity.this, "El número de teléfono debe tener 10 dígitos", Toast.LENGTH_SHORT).show();
                }
            } else {
                // El contacto se guarda exitosamente sí las anteriores condiciones no se cumplen
                Contacto contactoModificado = new Contacto(nombre, apellido, telefono, domicilio, genero);
                Intent intent = new Intent();
                intent.putExtra("nombre", nombre);
                intent.putExtra("apellido", apellido);
                intent.putExtra("telefono", telefono);
                intent.putExtra("domicilio", domicilio);
                intent.putExtra("genero", genero);
                intent.putExtra("contactoModificado", contactoModificado);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    //En casos que el usuario quiera regresar, se le pregunta sí quiere descartar lo hecho o bien, seguir editando.
    private void mostrarDialogoDeConfirmacion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AgregarContactoActivity.this);
        builder.setMessage("No se guardaron tus cambios");
        builder.setPositiveButton("Seguir editando", (dialog, which) -> {
            // No hacer nada, solo cerrar el diálogo
            dialog.dismiss();
        });
        builder.setNegativeButton("Descartar", (dialog, which) -> {
            // Descartar los cambios y regresar a la pantalla anterior
            finish();
        });
        builder.show();
    }
/*
Lógica de agregar/modificar contactos:
La plantilla de agregar/modificar es la misma (solo cambia el nombre). La única cosa que permite a la aplicación saber cuando se agrega y cuando se modifica es a través del número de teléfono.
El número de teléfono funciona como la ID del contacto. Sí yo modifico el contacto y modifico el número de teléfono, la app interpreta que estoy creando un nuevo usuario.
Como así también, sí creo un nuevo contacto y pongo un número de teléfono con el que coincide de un número de teléfono de un contacto ya crado, la app interpreta que estoy modificando un contacto.
 */

}