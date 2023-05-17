package com.tonni.efolder.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tonni.efolder.OnFileSelectedListener;
import com.tonni.efolder.R;

import java.io.File;
import java.util.List;

public class File_Adapter extends RecyclerView.Adapter<File_Adapter.FileViewHolder> {

    private Context context;
    private List<File> file;
    private OnFileSelectedListener listener;

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FileViewHolder(LayoutInflater.from(context).inflate(R.layout.file_container, parent, false));
    }

    public File_Adapter(Context context, List<File> files,OnFileSelectedListener listener) {
        this.context = context;
        this.file = files;
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tvName.setText(file.get(position).getName());
        holder.tvName.setSelected(true);
        int items = 0;
        if (file.get(position).isDirectory()) {
            File[] files = file.get(position).listFiles();
            for (File singleFile : files) {

                if (!singleFile.isHidden()){
                    items += 1;
                }

            }

            holder.tvSize.setText(String.valueOf(items) + " Files");

        }
        else {
            holder.tvSize.setText(Formatter.formatShortFileSize(context, file.get(position).length()));
        }
        if (file.get(position).getName().toLowerCase().endsWith(".jpeg")) {
            holder.imgFile.setImageResource(R.drawable.ic_jpg);
        } else if (file.get(position).getName().toLowerCase().endsWith(".jpg")) {
            holder.imgFile.setImageResource(R.drawable.ic_jpg);
        } else if (file.get(position).getName().toLowerCase().endsWith(".pdf")) {
            holder.imgFile.setImageResource(R.drawable.ic_pdf);
        } else if (file.get(position).getName().toLowerCase().endsWith(".png")) {
            holder.imgFile.setImageResource(R.drawable.ic_png);
        } else if (file.get(position).getName().toLowerCase().endsWith(".doc")) {
            holder.imgFile.setImageResource(R.drawable.ic_doc);
        } else if (file.get(position).getName().toLowerCase().endsWith(".mp4")) {
            holder.imgFile.setImageResource(R.drawable.ic_video);
        } else if (file.get(position).getName().toLowerCase().endsWith(".apk")) {
            holder.imgFile.setImageResource(R.drawable.apk);
        } else if (file.get(position).getName().toLowerCase().endsWith(".mkv")) {
            holder.imgFile.setImageResource(R.drawable.ic_fmkv);
        }
        else if (file.get(position).getName().toLowerCase().endsWith(".cpp")) {
            holder.imgFile.setImageResource(R.drawable.ic_cpp);
        }
        else if (file.get(position).isDirectory()) {
            holder.imgFile.setImageResource(R.drawable.ic_folder);
        }
        else if (file.get(position).getName().toLowerCase().endsWith(".mp3")) {
            holder.imgFile.setImageResource(R.drawable.ic_music);
        }
        else {
            holder.imgFile.setImageResource(R.drawable.ic_txt);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnFileClicked(file.get(position));
            }
        });
        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.OnFileLongClicked(file.get(position),position);
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return file.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName, tvSize;
        private CardView container;
        private ImageView imgFile;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_file_name);
            tvSize = itemView.findViewById(R.id.tv_file_size);
            container = itemView.findViewById(R.id.container);
            imgFile = itemView.findViewById(R.id.img_file_type);
        }
    }
}
