package alphadelete.aguaconsciente.models;

public class TypeItem implements Comparable<TypeItem>  {
    private long id;
    private String desc;
    private float liter;
    private char custom;

    // constructor
    public TypeItem(long itemId, String itemDesc, float itemLiter, char itemCustom) {
        this.id = itemId;
        this.desc = itemDesc;
        this.liter = itemLiter;
        this.custom = itemCustom;
    }

    public long getId() {
        return this.id;
    }

    public String getDesc() {
        return this.desc;
    }
    public void setDesc(String itemDesc) {
        this.desc = itemDesc;
    }

    public float getLiter() {
        return this.liter;
    }
    public void setLiter(float itemLiter) {
        this.liter = itemLiter;
    }

    public char getCustom() {
        return this.custom;
    }

    @Override
    public int compareTo(TypeItem o) {
        return getDesc().compareTo(o.getDesc());
    }

}
