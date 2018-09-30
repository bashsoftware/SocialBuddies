package bash.socialbuddies.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import bash.socialbuddies.R;

public class AdaptadorImagenesHorizontal extends RecyclerView.Adapter<AdaptadorImagenesHorizontal.ViewHolder> {

    private Context context;
    private ArrayList<Uri> items;

    public AdaptadorImagenesHorizontal(ArrayList<Uri> items) {
        this.items = items;
    }

    @Override
    public AdaptadorImagenesHorizontal.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_nuevo_registro_publicacion_imagen, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Uri uri = items.get(position);
        {
            holder.imageButton.setPadding(0, 0, 0, 0);
            holder.imageButton.setImageURI(uri);
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageButton;

        public ViewHolder(View view) {
            super(view);
            imageButton = ((ImageView) view.findViewById(R.id.fragment_nuevo_registro_publicacion_imagen_imageView));
        }

    }
}




