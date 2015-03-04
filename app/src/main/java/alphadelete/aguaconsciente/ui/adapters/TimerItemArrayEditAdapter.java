package alphadelete.aguaconsciente.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import alphadelete.aguaconsciente.R;
import alphadelete.aguaconsciente.dao.TimerDS;
import alphadelete.aguaconsciente.models.TimerItem;
import alphadelete.aguaconsciente.utils.StringFormat;

public class TimerItemArrayEditAdapter extends RecyclerView.Adapter<TimerItemArrayEditAdapter.TimerViewHolder> {

    private Context mContext;
    private List<TimerItem> timerItemList;
    private TimerDS timerDatasource;

    public TimerItemArrayEditAdapter(Context mContext, List<TimerItem> timerItemList) {
        this.timerItemList = timerItemList;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(final TimerViewHolder timerViewHolder, int position) {
        TimerItem timerItem = timerItemList.get(position);

        String liter = StringFormat.literToString(timerItem.getLiter(), timerItem.getMillis());
        String timer = StringFormat.millisToString(timerItem.getMillis());
        String date = StringFormat.dateToString(timerItem.getDate());

        timerViewHolder.vTitle.setText(liter + " " + mContext.getString(R.string.msg_liter) + " (" + timer + ")");
        timerViewHolder.vDate.setText(date);

        timerViewHolder.vBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // Create a Alert to confirm delete
            callDeleteDialog(mContext, timerViewHolder.getPosition());
            }
        });

    }

    @Override
    public TimerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_list_edit_timer, viewGroup, false);

        return new TimerViewHolder(itemView);
    }

    private void callDeleteDialog(final Context mContext, final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        // Set Alert Title and Message
        alert.setTitle(R.string.msg_box_del_title);
        alert.setMessage(R.string.msg_box_message);
        // If user press YES or NO
        alert.setPositiveButton(R.string.msg_box_yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Get the Timer
                    TimerItem typeItem = timerItemList.get(position);
                    // Open connection to database
                    timerDatasource = new TimerDS(mContext);
                    timerDatasource.open();
                    // Get typed data and delete in database
                    timerDatasource.deleteTimer(typeItem);
                    timerItemList.remove(position);
                    notifyItemRemoved(position);
                    // Close connection to database
                    timerDatasource.close();
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

    @Override
    public int getItemCount() {
        return timerItemList.size();
    }

    public static class TimerViewHolder extends RecyclerView.ViewHolder{

        protected TextView vDate;
        protected TextView vTitle;
        protected ImageButton vBtnDelete;

        public TimerViewHolder(View v) {
            super(v);

            vDate =  (TextView) v.findViewById(R.id.txtDate);
            vTitle = (TextView) v.findViewById(R.id.title);
            vBtnDelete = (ImageButton) v.findViewById(R.id.imgBtnDelete);
        }

    }

}
