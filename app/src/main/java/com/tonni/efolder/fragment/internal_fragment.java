package com.tonni.efolder.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.tonni.efolder.FileOpener;
import com.tonni.efolder.OnFileSelectedListener;
import com.tonni.efolder.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class internal_fragment extends Fragment implements OnFileSelectedListener {
    View view;
    private File_Adapter file_adapter;
    private RecyclerView recyclerView;
    private List<File> filelist;
    private ImageView img_back;
    private TextView tv_pathHolder;
    private File storage;
    String data;
    String[] items = {"Details", "Rename", "Delete", "Share"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_internal, container, false);

        tv_pathHolder = view.findViewById(R.id.tv_pathHolder);
        img_back = view.findViewById(R.id.img_back);


        String internalStorage = System.getenv("EXTERNAL_STORAGE");
        storage = new File(internalStorage);

        try {
            data = getArguments().getString("path");
            File file = new File(data);
            storage = file;
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv_pathHolder.setText(storage.getAbsolutePath());
        runtimePermission();
        return view;
    }

    private void runtimePermission() {
        Dexter.withContext(getContext()).withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {

                displayFiles();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {

                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    public ArrayList<File> findFiles(File file) {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                arrayList.add(singleFile);
            }
        }
        for (File singleFile : files) {
            if (singleFile.getName().toLowerCase(Locale.ROOT).endsWith(".jpeg") ||
                    singleFile.getName().toLowerCase(Locale.ROOT).endsWith(".jpg") ||
                    singleFile.getName().toLowerCase(Locale.ROOT).endsWith(".mp3") ||
                    singleFile.getName().toLowerCase(Locale.ROOT).endsWith(".pdf") ||
                    singleFile.getName().toLowerCase(Locale.ROOT).endsWith(".mp4") ||
                    singleFile.getName().toLowerCase(Locale.ROOT).endsWith(".png")) {

                arrayList.add(singleFile);
            }

        }
        return arrayList;
    }

    private void displayFiles() {
        recyclerView = view.findViewById(R.id.recycler_internal);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        filelist = new ArrayList<>();
        filelist.addAll(findFiles(storage));
        file_adapter = new File_Adapter(getContext(), filelist, this);
        recyclerView.setAdapter(file_adapter);

    }

    @Override
    public void OnFileClicked(File file) {
        if (file.isDirectory()) {
            Bundle bundle = new Bundle();
            bundle.putString("path", file.getAbsolutePath());
            internal_fragment internal_fragment_ = new internal_fragment();
            internal_fragment_.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, internal_fragment_).addToBackStack(null).commit();

        } else {
            try {
                FileOpener.openFile(getContext(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void OnFileLongClicked(File file,int position) {
        final Dialog optionDialog = new Dialog(getContext());
        optionDialog.setContentView(R.layout.option_dialogue);
        optionDialog.setTitle("Select options");
        ListView options = (ListView) optionDialog.findViewById(R.id.list);
        CustomAdapter customAdapter = new CustomAdapter();
        options.setAdapter(customAdapter);
        optionDialog.show();


        options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                //switch case for the options
                switch (selectedItem) {
                    case "Details":
                        AlertDialog.Builder detailDialog = new AlertDialog.Builder(getContext());
                        detailDialog.setTitle("Details");
                        final TextView details = new TextView(getContext());
                        detailDialog.setView(details);
                        //Getting details for the file
                        Date lastmodified = new Date(file.lastModified());
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy  HH:mm:ss");
                        String formattedDate = formatter.format(lastmodified);
                        details.setText("File  :" + file.getName() + "\n" +
                                "Size:" + Formatter.formatShortFileSize(getContext(), file.length()) + "\n" +
                                "File Path :" + file.getAbsolutePath() + "\n" +
                                "Last modified :" + lastmodified

                        );

                        detailDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                optionDialog.cancel();
                            }
                        });
                        AlertDialog aletDialog_details = detailDialog.create();
                        aletDialog_details.show();
                        break;

                    //Rename
                    case "Rename":
                        AlertDialog.Builder renameDialog = new AlertDialog.Builder(getContext());
                        renameDialog.setTitle("Rename");
                        final EditText name = new EditText(getContext());
                        renameDialog.setView(name);
                        renameDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String newName = name.getEditableText().toString();
                                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));

                                File current = new File(file.getAbsolutePath());
                                File destination=new File(file.getAbsolutePath().replace(file.getName(),newName)+extension);

                                if(current.renameTo(destination)){
                                    filelist.set(position,destination);
                                    file_adapter.notifyItemChanged(position);
                                    Toast.makeText(getContext(), "Renamed", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getContext(), "Couldn't Rename", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                        renameDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                optionDialog.cancel();
                            }
                        });
                        AlertDialog alertDialog_rename =renameDialog.create();
                        alertDialog_rename.show();
                        break;
                    case "Delete":
                        AlertDialog.Builder deleteDialog=new AlertDialog.Builder(getContext());

                        deleteDialog.setTitle("Delete "+file.getName() + "?");
                        deleteDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               file.delete();
                               filelist.remove(position);
                               file_adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "File has been deleted", Toast.LENGTH_SHORT).show();

                            }
                        });
                        deleteDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               optionDialog.cancel();
                            }
                        });

                        AlertDialog alertDialog_delete=deleteDialog.create();
                        alertDialog_delete.show();
                        break;

                    case "Share":
                        try {
                            String fileName = file.getName();
                            Intent share = new Intent();
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            share.setAction(Intent.ACTION_SEND);
                            share.setType("image/jpeg");
                            share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                            startActivity(Intent.createChooser(share, "Share " + fileName));
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
            }
        });

    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return items[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View myView = getLayoutInflater().inflate(R.layout.option_layout, null);
            TextView txtOptions = myView.findViewById(R.id.txtOption);
            ImageView imgOptions = myView.findViewById(R.id.imgOption);
            txtOptions.setText(items[position]);
            //TODO defined in the string array up there
            //TODO adding icons for my app
            if (items[position].equals("Details")) {
                imgOptions.setImageResource(R.drawable.details);
            } else if (items[position].equals("Rename")) {
                imgOptions.setImageResource(R.drawable.ic_rename);
            } else if (items[position].equals("Delete")) {
                imgOptions.setImageResource(R.drawable.ic_delete);
            } else if (items[position].equals("Share")) {
                imgOptions.setImageResource(R.drawable.ic_share);
            }


            return myView;
        }
    }
}

