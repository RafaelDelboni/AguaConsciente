package alphadelete.aguaconsciente.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.TypeDS;
import alphadelete.aguaconsciente.models.TypeItem;
import alphadelete.aguaconsciente.ui.activities.TimerActivity;
import alphadelete.aguaconsciente.ui.activities.MainActivity;

public class TypeItemArrayViewAdapter extends RecyclerView.Adapter<TypeItemArrayViewAdapter.TypeViewHolder> {
    private Context mContext;
    private List<TypeItem> typeItemList;
    private TypeDS typeDatasource;

    public TypeItemArrayViewAdapter(Context mContext, List<TypeItem> typeList) {
        this.typeItemList = typeList;
        this.mContext = mContext;
    }

    @Override
    public int getItemCount() {
        return typeItemList.size();
    }

    @Override
    public void onBindViewHolder(TypeViewHolder typeViewHolder, int position) {
        TypeItem typeItem = typeItemList.get(position);

        typeViewHolder.vLiter.setText(String.valueOf(getTypeTotal(typeItem.getId())) + " " + mContext.getString(R.string.msg_spend_liters));
        typeViewHolder.vTitle.setText(typeItem.getDesc());

        typeViewHolder.setClickListener(new TypeViewHolder.ClickListener() {
            @Override
            public void onClick(View v, Integer pos, boolean isLongClick) {
                // Create a Alert to confirm delete
                if (pos != null) {
                    callViewActivity(mContext, pos);
                }

            }
        });
    }

    private String getTypeTotal(long typeId){
        // Open connection to timer database
        typeDatasource = new TypeDS(mContext);
        typeDatasource.open();

        float total = typeDatasource.sumType(typeId);

        // Close connection to timer database
        typeDatasource.close();

        return String.format("%.2f", total);
    }

    @Override
    public TypeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_list_view_type, viewGroup, false);

        return new TypeViewHolder(itemView);
    }

    private void callViewActivity(final Context mContext, int position) {
        final TypeItem typeItem = typeItemList.get(position);

        Intent intent = new Intent(mContext, TimerActivity.class);
        intent.putExtra(MainActivity.EXTRA_MESSAGE, String.valueOf(typeItem.getId()));

        mContext.startActivity(intent);

    }

    public static class TypeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener{

        protected TextView vLiter;
        protected TextView vTitle;

        private ClickListener clickListener;

        public TypeViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

            vLiter =  (TextView) v.findViewById(R.id.txtLiter);
            vTitle = (TextView) v.findViewById(R.id.title);
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
