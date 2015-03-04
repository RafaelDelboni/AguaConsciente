package alphadelete.aguaconsciente.models;

import java.sql.Date;

public class TimerItem implements Comparable<TimerItem>  {
    private long id;
    private long typeId;
    private float millis;
    private float liter;
    private Date date;

    // constructor
    public TimerItem(long itemId, long itemTypeId, float itemMillis, float itemLiter, Date itemDate) {
        this.id = itemId;
        this.typeId = itemTypeId;
        this.millis = itemMillis;
        this.liter = itemLiter;
        this.date = itemDate;
    }

    public long getId() {
        return this.id;
    }

    public long getTypeId() {
        return this.typeId;
    }

    public float getMillis() {
        return this.millis;
    }

    public float getLiter() {
        return this.liter;
    }

    public Date getDate() {
        return this.date;
    }

    @Override
    public int compareTo(TimerItem o) {
        return getDate().compareTo(o.getDate());
    }

}
