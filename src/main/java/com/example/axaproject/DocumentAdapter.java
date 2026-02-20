package com.example.axaproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder> {

    private final List<DocumentItem> documents;

    public DocumentAdapter(List<DocumentItem> documents) {
        this.documents = documents;
    }

    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_document, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        DocumentItem document = documents.get(position);
        holder.name.setText(document.getName());
        holder.type.setText("Type: " + document.getType());
        holder.status.setText(document.getStatus());
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    static class DocumentViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView type;
        final TextView status;

        DocumentViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.document_name);
            type = itemView.findViewById(R.id.document_type);
            status = itemView.findViewById(R.id.document_status);
        }
    }
}
