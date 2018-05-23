package se.kth.ledbat.Driver;

import se.sics.kompics.util.Identifiable;

import java.io.Serializable;

public class MyIdentifiable implements Identifiable<MyString>, Serializable {

    MyString id;

    public MyIdentifiable(String id) {
        this.id = new MyString(id);
    }

    @Override
    public MyString getId() {
        return id;
    }
}

