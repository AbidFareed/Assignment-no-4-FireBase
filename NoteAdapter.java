package com.example.assignmentno04;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;


public class NoteAdapter extends FirebaseRecyclerAdapter<Note,NoteAdapter.ViewHolder> {
    public interface Connection{
        void onNoteClick(String id,Note model);
        void onNoteLong(String id);
    }
    Connection ConnectionObject;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirebaseRecyclerOptions<Note> options,Connection Conectionobject) {
        super(options);
        ConnectionObject=Conectionobject;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Note model) {
        holder.tv_id.setText(model.getNoteid());
        holder.tv_title.setText(model.getTitle());
        holder.tv_content.setText(model.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectionObject.onNoteClick(model.getNoteid(),model);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ConnectionObject.onNoteLong(model.getNoteid());
                return false;
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.notes_layout, parent, false);

        return new ViewHolder(v,ConnectionObject);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id,tv_title, tv_content;
        Connection ConnectionObject;
        public ViewHolder(@NonNull View itemView,Connection ConectionObject) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_Title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_id = itemView.findViewById(R.id.note_id);

        }
    }
}
