package bash.socialbuddies.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import bash.socialbuddies.R;
import bash.socialbuddies.adapters.AdaptadorImagenesHorizontal;
import bash.socialbuddies.beans.BeanPublicacion;
import bash.socialbuddies.beans.BeanUsuario;
import bash.socialbuddies.behavior.ItemClickSupport;
import bash.socialbuddies.utilities.FirebaseReference;
import bash.socialbuddies.utilities.Singleton;

import static android.app.Activity.RESULT_OK;

public class FragmentContenidoPublicacionRegistro extends Fragment {

    private View view;
    private ImageView imageView;
    private EditText editText;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ImageButton imageButton;
    private ProgressBar progressBar;
    private TextView text;
    private AdaptadorImagenesHorizontal adaptador;
    private BeanPublicacion beanPublicacion;
    private ArrayList<String> listUploaded;
    private ArrayList<Uri> listSelection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nuevo_registro_publicacion, container, false);

        text = (TextView) view.findViewById(R.id.fragment_nuevo_registro_publicacion_textview);
        imageView = ((ImageView) view.findViewById(R.id.fragment_nuevo_registro_publicacion_imageView_imagen));
        editText = ((EditText) view.findViewById(R.id.fragment_nuevo_registro_publicacion_editText_texto));
        recyclerView = ((RecyclerView) view.findViewById(R.id.fragment_nuevo_registro_publicacion_recyclerView));
        imageButton = ((ImageButton) view.findViewById(R.id.fragment_nuevo_registro_publicacion_imageButton));
        floatingActionButton = ((FloatingActionButton) view.findViewById(R.id.fragment_nuevo_registro_publicacion_floatingActionButton));
        progressBar = ((ProgressBar) view.findViewById(R.id.fragment_nuevo_registro_publicacion_progressBar));

        BeanUsuario beanUsuario = Singleton.getInstancia().getBeanUsuario();

        if (beanUsuario != null) {

            listSelection = new ArrayList<>();

            Glide.with(getContext()).load(beanUsuario.getUsu_perfil()).apply(RequestOptions.circleCropTransform()).into(imageView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            adaptador = new AdaptadorImagenesHorizontal(listSelection);
            recyclerView.setAdapter(adaptador);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seleccionarImagen();
                }
            });

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(final RecyclerView recyclerView, final int position, View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Eliminar Imagen");
                    builder.setMessage("¿Está seguro que desea eliminar la imagen?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebaseReference.PUBLICACIONES);
                            storageReference.child(listSelection.get(position).getLastPathSegment()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        listSelection.remove(position);
                                        listUploaded.remove(position);
                                        adaptador = new AdaptadorImagenesHorizontal(listSelection);
                                        recyclerView.setAdapter(adaptador);
                                    } else {
                                        Snackbar.make(getView(), "Ocurrió un error al eliminar el elemento, inténtelo nuevamente", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validar();
                }
            });

        }

        if (Singleton.getInstancia().getBeanUsuario() != null) {

            listUploaded = new ArrayList<>();
            listSelection = new ArrayList<>();

            Glide.with(getContext()).load(beanUsuario.getUsu_perfil()).apply(RequestOptions.circleCropTransform()).into(imageView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            adaptador = new AdaptadorImagenesHorizontal(listSelection);
            recyclerView.setAdapter(adaptador);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    seleccionarImagen();
                }
            });

            ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(final RecyclerView recyclerView, final int position, View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Eliminar Imagen");
                    builder.setMessage("¿Está seguro que desea eliminar la imagen?");
                    builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            StorageReference storageReference = FirebaseStorage.getInstance().getReference(FirebaseReference.PUBLICACIONES);

                            storageReference.child(listSelection.get(position).getLastPathSegment()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        listSelection.remove(position);
                                        listUploaded.remove(position);
                                        adaptador = new AdaptadorImagenesHorizontal(listSelection);
                                        recyclerView.setAdapter(adaptador);
                                    } else {
                                        Snackbar.make(getView(), "Ocurrió un error al eliminar el elemento, inténtelo nuevamente", Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    Dialog dialog = builder.create();
                    dialog.show();
                }
            });

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validar();
                }
            });

        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            floatingActionButton.setEnabled(false);
            final Uri uri = data.getData();
            StorageReference storage = FirebaseStorage.getInstance().getReference(FirebaseReference.PUBLICACIONES);

            storage.child(uri.getLastPathSegment()).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        floatingActionButton.setEnabled(true);

                        String s = (task.getResult().getDownloadUrl().toString());

                        listUploaded.add(s);
                        listSelection.add(uri);
                        adaptador.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    public void seleccionarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione una imagen"), 10);
    }

    private void validar() {

        if (editText.getText().toString().equals("") && listUploaded.size() == 0) {
            Snackbar.make(getView(), "No se ha añadido ningún contenido", Snackbar.LENGTH_SHORT).show();
        } else {
            beanPublicacion = new BeanPublicacion();
            beanPublicacion.setPub_fecha(System.currentTimeMillis());
            beanPublicacion.setPub_descripcion(editText.getText().toString());
            beanPublicacion.setBeanUsuario(Singleton.getInstancia().getBeanUsuario());
            beanPublicacion.setBeanPublicacionImagenes(listUploaded);
            finalizar();
        }
    }

    private void finalizar() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseReference.PUBLICACIONES + "/" + beanPublicacion.getBeanUsuario().getUsu_id());

        reference.push().setValue(beanPublicacion).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Publicado", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                } else {
                    Snackbar.make(getView(), "Hubo un error al subir, inténtelo nuevamente", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


}
