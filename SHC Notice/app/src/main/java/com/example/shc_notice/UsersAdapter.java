package com.example.shc_notice;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class UsersAdapter extends FirebaseRecyclerAdapter<dbHelper, UsersAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    String accType = "";
    //String upAccType = "";
    //String[] userTypes = {"user login", "admin login", "notice Staff", "Principal", "COE", "Golden Jubilee", "Silver Jubilee", "John Mad"};

    public UsersAdapter(@NonNull FirebaseRecyclerOptions<dbHelper> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull dbHelper model) {
        holder.name.setText(model.getFullname());
        holder.username.setText(model.getUsername());
        holder.password.setText(model.getPassword());
        holder.userType.setText(model.getAcctype());

        accType = model.getAcctype();

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.username.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update_popup))
                        .setExpanded(true, 1200)
                        .create();


                View view = dialogPlus.getHolderView();


//Spinner--
//                AutoCompleteTextView spinner = view.findViewById(R.id.upSpinner_Options);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(holder.username.getContext(),
//                        android.R.layout.simple_spinner_dropdown_item, userTypes);
//                spinner.setAdapter(adapter);
//                spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String selectedItem = (String) parent.getItemAtPosition(position);
//                        upAccType= selectedItem;
//                       // Toast.makeText(holder.username.getContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
//                    }
//                });

                TextInputLayout fullname = view.findViewById(R.id.upFullName);
                TextInputLayout username = view.findViewById(R.id.upUsername);
                TextInputLayout password = view.findViewById(R.id.upPass);

                Button updatebtn = view.findViewById(R.id.updateBtn);
//--
                EditText fullnameEditText = fullname.getEditText();
                EditText usernameEditText = username.getEditText();
                EditText passwordEditText = password.getEditText();

                // spinner.setText(model.getAcctype(), false);

                //  if (fullnameEditText != null && usernameEditText != null && passwordEditText != null) {
                fullnameEditText.setText(model.getFullname());
                usernameEditText.setText(model.getUsername());
                passwordEditText.setText(model.getPassword());
                //}
                dialogPlus.show();
                updatebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("fullname", fullnameEditText.getText().toString());
                        map.put("username", usernameEditText.getText().toString());
                        map.put("password", passwordEditText.getText().toString());

                        //  upAccType = model.getAcctype();
                        FirebaseDatabase.getInstance().getReference().child("" + accType)
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.username.getContext(), "Updated Successfully.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.username.getContext(), "Update Failed.", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.name.getContext());
                builder.setTitle("Are You Sure?");
                builder.setMessage("This data will be Delete.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference().child("" + accType)
                                .child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(holder.name.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

    }




    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_items, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {

        TextView name, username, password, userType;
        ImageView edit, delete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            username = itemView.findViewById(R.id.username);
            password = itemView.findViewById(R.id.password);
            userType = itemView.findViewById(R.id.userType);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
