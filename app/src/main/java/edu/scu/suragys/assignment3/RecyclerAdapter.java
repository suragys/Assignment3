package edu.scu.suragys.assignment3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by suragys on 5/19/16.
 * <p/>
 * reference http://www.vogella.com/tutorials/AndroidRecyclerView/article.html
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<MyObject> myDataset;
    private Context ctx;

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Log.v("drag", fromPosition + " " + toPosition);
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(myDataset, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(myDataset, i, i - 1);
            }
        }

        notifyItemMoved(fromPosition, toPosition);

        return true;

    }

    @Override
    public void onItemDismiss(int position) {
        remove(position);

    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public ImageView imageIcon;
        //public TextView txtFooter;

        public MyViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.firstLine);
            imageIcon = (ImageView) v.findViewById(R.id.icon);
            itemView.setPadding(10,10,10,10);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent i = new Intent(itemView.getContext(), PhotoViewActivity.class);
                    i.putExtra("position",pos);
                    i.putExtra("object",myDataset.get(pos));
                    itemView.getContext().startActivity(i);
                }
            });
            //txtFooter = (TextView) v.findViewById(R.id.secondLine);
        }
    }

    public void add(int position, MyObject item) {
        myDataset.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {

        MyObject o = myDataset.get(position);

//        Log.v("object to delete" , "object = " + o.getId() + o.getCaption());


        DBHelper dbHelper = new DBHelper(ctx);
        Log.v("object to delete", "object = " + o.getId() + o.getCaption() + dbHelper.getDatabaseName());

        dbHelper.deleteRow(o);
        myDataset.remove(position);

        notifyItemRemoved(position);
    }

    public RecyclerAdapter(ArrayList<MyObject> myDataset, Context context) {
        this.myDataset = myDataset;
        this.ctx = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your data set at this position
        // - replace the contents of the view with that element
        final String name = myDataset.get(position).getCaption();
        File tf = new File(myDataset.get(position).getThumbNailPath());
        //Log.v("thumb anil image", tn.getPath());
        File f = new File(myDataset.get(position).getPath());
        //Log.v("image file = ", f.exists() + " ");
        holder.imageIcon.setImageURI(Uri.fromFile(tf));

        holder.txtHeader.setText(myDataset.get(position).getCaption());
        holder.txtHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                remove();
            }
        });


        //holder.txtFooter.setText("Footer: " + myDataset.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return myDataset.size();
    }

}
