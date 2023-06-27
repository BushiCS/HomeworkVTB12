package org.example;

import jakarta.persistence.*;

@Entity
@Table
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private int val;

    @Version
    long version;

    public Item() {
    }

    public Item(int val) {
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", val=" + val +
                '}';
    }
}
