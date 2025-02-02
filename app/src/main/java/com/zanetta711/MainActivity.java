package com.zanetta711;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText usuarioEditText;
    private EditText contraseniaEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        // Verificar si el usuario ya está autenticado
        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, ContactosActivity.class);
            startActivity(intent);
            finish(); // Cierra MainActivity
            return; // Salir del método
        }

        usuarioEditText = findViewById(R.id.usuario);
        contraseniaEditText = findViewById(R.id.contrasenia);
        Button iniciarSesionButton = findViewById(R.id.button);

        iniciarSesionButton.setOnClickListener(v -> {
            String usuario = usuarioEditText.getText().toString().toLowerCase();
            String contrasenia = contraseniaEditText.getText().toString();

            if (usuario.equals("usuario_tudai") && contrasenia.equals("1234")) {
                // Inicio de sesión exitoso
                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                // Guardar estado de autenticación en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();

                Intent intent = new Intent(MainActivity.this, ContactosActivity.class);
                startActivity(intent);
                finish(); // Cierra MainActivity
            } else {
                // Mensaje de error
                Toast.makeText(MainActivity.this, "Nombre de usuario o contraseña incorrectos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}