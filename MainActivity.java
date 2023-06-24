package com.example.assignmentno04;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NoteAdapter.Connection{
    RecyclerView rvlist;
    NoteAdapter noteAdapter;
    FirebaseRecyclerOptions<Note> options;
    FloatingActionButton floating_btn;
    ItemTouchHelper helperforswiping;
    DatabaseReference databaseNote;
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseNote=FirebaseDatabase.getInstance().getReference("Notes");
        rvlist=findViewById(R.id.rvlist);
        rvlist.setHasFixedSize(true);
        rvlist.setLayoutManager(new LinearLayoutManager(this));
        floating_btn = findViewById(R.id.F_btn);
        helperforswiping = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView rvlist, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                NoteAdapter adapter = (NoteAdapter)rvlist.getAdapter();

                if (adapter != null) {
                    Note item = adapter.getItem(position);
                    String b=item.getNoteid();
                    onNoteLong(b);
                }
            }

        });
        helperforswiping.attachToRecyclerView(rvlist);
        floating_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adddata();
            }
        });
                FirebaseRecyclerOptions<Note> options
                        = new FirebaseRecyclerOptions.Builder<Note>()
                        .setQuery(databaseNote, Note.class)
                        .build();
                noteAdapter=new NoteAdapter(options,(NoteAdapter.Connection)this);
                rvlist.setAdapter(noteAdapter);

    }

    private void adddata() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

        View view = LayoutInflater.from(this).inflate(R.layout.notedialog_layout, null);
        EditText editTextTitle = view.findViewById(R.id.ET_Title);
        EditText et_Content = view.findViewById(R.id.ET_Content);
        EditText et_id=view.findViewById(R.id.et_id);

        builder.setView(view);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String title = editTextTitle.getText().toString().trim();
                String content = et_Content.getText().toString().trim();
                String id=et_id.getText().toString().trim();
                if (!title.isEmpty()) {
                    HashMap<String, Object> data=new HashMap<>();
                    data.put("noteid",id);
                    data.put("title",title);
                    data.put("content",content);
                    FirebaseDatabase
                            .getInstance()
                            .getReference()
                            .child("Notes")
                            .child(id)
                            .updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(),"Successfully add",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Unsuccessfully add", Toast.LENGTH_SHORT).show();
                                } });
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a title", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    public void onNoteClick(String id,Note model) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Edit Note");

                View view1 = LayoutInflater.from(this).inflate(R.layout.notedialog_layout, null);
                EditText et_Title = view1.findViewById(R.id.ET_Title);
                EditText et_Content = view1.findViewById(R.id.ET_Content);
                EditText et_id=view1.findViewById(R.id.et_id);
                et_id.setEnabled(false);
                et_id.setFocusable(false);
                et_id.setFocusableInTouchMode(false);
                et_Title.setText(model.getTitle());
                et_Content.setText(model.getContent());
                et_id.setText(model.getNoteid());
                String org_id=model.getNoteid();
                builder.setView(view1);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = et_Title.getText().toString().trim();
                        String content = et_Content.getText().toString().trim();
                        String id=et_id.getText().toString().trim();
                        if (!title.isEmpty()) {
                            HashMap<String,Object> obj=new HashMap<>();
                            obj.put("noteid",id);
                            obj.put("title",title);
                            obj.put("content",content);
                            FirebaseDatabase.getInstance().getReference("Notes").child(org_id).updateChildren(obj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(),"Successfully update",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Unsuccessfully update",Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter a title", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }

    @Override
    public void onNoteLong(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete this note?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference("Notes").child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Successfully deleted",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"UnSuccessfully deleted",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                rvlist.getAdapter().notifyItemChanged(Integer.parseInt(id));
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

}