package com.zanetta711;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ViewHolder> {
    private List<Contacto> contactos;
    private ContactosActivity contactosActivity; // Referencia a la actividad

    public ContactosAdapter(List<Contacto> contactos, ContactosActivity contactosActivity) {
        this.contactos = contactos;
        this.contactosActivity = contactosActivity; // Inicializa la referencia
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Contacto contacto = contactos.get(position);
        holder.textViewNombre.setText(contacto.getNombre());
        holder.textViewApellido.setText(contacto.getApellido());
        holder.textViewTelefono.setText(contacto.getTelefono());
        holder.textViewDomicilio.setText(contacto.getDomicilio());
        holder.textViewGenero.setText(contacto.getGenero());

        // Manejar el clic largo para mostrar opciones
        holder.itemView.setOnLongClickListener(v -> {
            contactosActivity.showOptionsDialog(contacto);
            return true; // Indica que el evento ha sido manejado
        });
    }

    @Override
    public int getItemCount() {
        return contactos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewNombre;
        public TextView textViewApellido;
        public TextView textViewTelefono;
        public TextView textViewDomicilio;
        public TextView textViewGenero;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNombre = itemView.findViewById(R.id.textViewNombre);
            textViewApellido = itemView.findViewById(R.id.textViewApellido);
            textViewTelefono = itemView.findViewById(R.id.textViewTelefono);
            textViewDomicilio = itemView.findViewById(R.id.textViewDomicilio);
            textViewGenero = itemView.findViewById(R.id.textViewGenero);
        }
    }
}