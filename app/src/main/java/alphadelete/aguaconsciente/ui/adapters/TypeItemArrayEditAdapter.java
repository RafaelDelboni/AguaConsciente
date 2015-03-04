package alphadelete.aguaconsciente.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.TypeDS;
import alphadelete.aguaconsciente.models.TypeItem;

public class TypeItemArrayEditAdapter extends RecyclerView.Adapter<TypeItemArrayEditAdapter.TypeViewHolder> {
    private Context mContext;
    private List<TypeItem> typeItemList;
    private TypeDS typeDatasource;

    public TypeItemArrayEditAdapter(Context mContext, List<TypeItem> typeList) {
        this.typeItemList = typeList;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return typeItemList.size();
    }

    @Override
    public void onBindViewHolder(final TypeViewHolder typeViewHolder, int position) {
        TypeItem typeItem = typeItemList.get(position);

        typeViewHolder.vLiter.setText(String.valueOf(typeItem.getLiter()) + " " + mContext.getString(R.string.msg_liters_per_minute));
        typeViewHolder.vTitle.setText(typeItem.getDesc());
        typeViewHolder.setClickListener(new TypeViewHolder.ClickListener() {
            @Override
            public void onClick(View v, Integer pos, boolean isLongClick) {
                // Create a Alert to confirm delete
                if (pos != null) {
                    callEditDialog(mContext, typeViewHolder.getPosition());
                }

            }
        });
        if (typeItem.getCustom() == 'S') {
            typeViewHolder.vBtnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create a Alert to confirm delete
                    callDeleteDialog(mContext, typeViewHolder.getPosition());
                }
            });
        } else {
            typeViewHolder.vBtnDelete.setVisibility(View.GONE);
        }
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_list_edit_type, viewGroup, false);

        return new TypeViewHolder(itemView);
    }

    private void callEditDialog(final Context mContext, final int position) {
        final TypeItem typeItem = typeItemList.get(position);

        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        // Set Alert Title
        alert.setTitle(R.string.msg_box_edit_title);
        // Get the alert layout by inflater
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // Inflate and set the layout for the dialog
        View v = inflater.inflate(R.layout.popup_edit_type, null);
        final EditText eLiter =  (EditText) v.findViewById(R.id.editTypeLiter);
        final EditText eTitle = (EditText) v.findViewById(R.id.editTypeName);
        TextView vTitle = (TextView) v.findViewById(R.id.viewTypeName);
        // Set values on layout
        eLiter.setText(String.valueOf(typeItem.getLiter()));
        eTitle.setText(typeItem.getDesc());
        vTitle.setText(typeItem.getDesc());
        // Check it can edit the Name
        if (typeItem.getCustom() == 'N')
        {
            eTitle.setVisibility(v.INVISIBLE);
            vTitle.setVisibility(v.VISIBLE);
        }
        else {
            eTitle.setVisibility(v.VISIBLE);
            vTitle.setVisibility(v.INVISIBLE);
        }
        // Pass null as the parent view because its going in the dialog layout
        alert.setView(v)
            // Add action buttons
            .setPositiveButton(R.string.msg_box_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // Get typed data and save on object
                    typeItem.setDesc(eTitle.getText().toString());
                    typeItem.setLiter(Float.parseFloat(eLiter.getText().toString()));

                    // Open connection to database
                    typeDatasource = new TypeDS(mContext);
                    typeDatasource.open();
                    // Get typed data and save on database
                    typeDatasource.updateType(typeItem);
                    // Close connection to database
                    typeDatasource.close();

                    // Refresh Recycler
                    notifyItemChanged(position);

                    // Clear dialog
                    dialog.dismiss();
                }
            })
            .setNegativeButton(R.string.msg_box_no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Clear dialog
                    dialog.dismiss();
                }
            });

        alert.show();
    }

    private void callDeleteDialog(Context mContext, int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        // Set Alert Title and Message
        alert.setTitle(R.string.msg_box_del_title);
        alert.setMessage(R.string.msg_box_message);
        // If user press YES or NO
        alert
            .setPositiveButton(R.string.msg_box_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do Delete
                    // Clear dialog
                    //timerItemList.remove(position);
                    //notifyItemRemoved(position);
                    dialog.dismiss();

                }
            })
            .setNegativeButton(R.string.msg_box_no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do not delete
                    // Clear dialog
                    dialog.dismiss();
                }
            });

        alert.show();
    }

    public static class TypeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        protected TextView vLiter;
        protected TextView vTitle;
        protected ImageButton vBtnDelete;

        private ClickListener clickListener;

        public TypeViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

            vLiter =  (TextView) v.findViewById(R.id.txtLiter);
            vTitle = (TextView) v.findViewById(R.id.title);
            vBtnDelete = (ImageButton) v.findViewById(R.id.imgBtnDelete);
        }

        // Interface for handling clicks - both normal and long ones.
        public interface ClickListener {
            /**
             * Called when the view is clicked.
             *
             * @param v view that is clicked
             * @param position of the clicked item
             * @param isLongClick true if long click, false otherwise
             */
            public void onClick(View v, Integer position, boolean isLongClick);

        }

        // Setter for listener.
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View v) {

            // If not long clicked, pass last variable as false.
            clickListener.onClick(v, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {

            // If long clicked, passed last variable as true.
            clickListener.onClick(v, getPosition(), true);
            return true;
        }
    }

}
