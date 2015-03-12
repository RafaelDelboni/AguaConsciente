package alphadelete.aguaconsciente.models;

public class SumTypeItem implements Comparable<SumTypeItem>  {
    private long id;
    private String desc;
    private float liter;

    // constructor
    public SumTypeItem(long itemId, String itemDesc, float itemLiter) {
        this.id = itemId;
        this.desc = itemDesc;
        this.liter = itemLiter;
    }

    public long getId() {
        return this.id;
    }

    public String getDesc() {
        return this.desc;
    }

    public float getLiter() {
        return this.liter;
    }

    @Override
    public int compareTo(SumTypeItem o) {
        return getDesc().compareTo(o.getDesc());
    }

}
