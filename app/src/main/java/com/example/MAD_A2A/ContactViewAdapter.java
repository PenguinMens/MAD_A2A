package com.example.MAD_A2A;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactViewAdapter extends RecyclerView.Adapter<ContactViewAdapter.MyViewHolder>{

    private final ContactViewInterface contactViewInterface;
    private int selectedPos = RecyclerView.NO_POSITION;
    private List<Contact> listData;
    public ContactViewAdapter(List<Contact> listData, ContactViewInterface contactViewInterface){
        this.listData = listData;
        this.contactViewInterface = contactViewInterface;
    }

    @Override
    public ContactViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_contact_layout,parent,false);
        return new ContactViewAdapter.MyViewHolder(view,contactViewInterface);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        byte[] img = listData.get(position).getImage();
        if(img != null)
            holder.contactPic.setImageBitmap(BitmapFactory.decodeByteArray(img,0, img.length));
        holder.name.setText(listData.get(position).getFirstName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //Grab Views
        //Similar to onCreate method
        ImageView contactPic;
        TextView name;
        // TextView texted;

        public MyViewHolder(@NonNull View itemView, ContactViewInterface contactViewInterface) {
            super(itemView);
            ViewGroup.LayoutParams lp = itemView.getLayoutParams();
            contactPic = itemView.findViewById(R.id.contact_image);
            name = itemView.findViewById(R.id.contact_name);

            itemView.setOnClickListener(new  View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    if(contactViewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION) {
                            selectedPos = getLayoutPosition();
                            contactViewInterface.onItemCLick(pos,view);
                            notifyItemChanged(selectedPos);
                        }
                    }
                }
            });

        }
    }
}
