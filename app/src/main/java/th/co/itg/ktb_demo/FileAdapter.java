package th.co.itg.ktb_demo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.AlbumFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by ToCHe on 21/8/2018 AD.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileHolder> {

    FileListener listener;

    public ArrayList<AlbumFile> filePaths = new ArrayList<>();
    public ArrayList<String> base64Files = new ArrayList<>();

    public FileAdapter(FileListener listener) {
        this.listener = listener;
    }

    public void addFile(String path){
        AlbumFile albumFile = new AlbumFile();
        albumFile.setPath(path);
        filePaths.add(albumFile);
        base64Files.add(FileUtils.encodeImage(path));
        notifyDataSetChanged();
    }

    public void clearImage(){
        for (AlbumFile f : filePaths){
            File data = new File(f.getPath());
            if (data.isFile()){
                data.delete();
            }
        }
        filePaths.clear();
        base64Files.clear();
        notifyDataSetChanged();
    }

    public void updateFiles(ArrayList<AlbumFile> data){
        this.filePaths.clear();
        this.filePaths.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        return new FileHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileHolder holder, int position) {
        holder.bind(filePaths.get(position));
    }

    @Override
    public int getItemCount() {
        return filePaths.size();
    }

    class FileHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        public FileHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }

        public void bind(AlbumFile data){
            listener.onClickFile();
            Glide.with(itemView.getContext())
                    .load(data.getPath())
                    .into(imageView);
        }


    }


    interface FileListener{
        public void onClickFile();
    }
}
