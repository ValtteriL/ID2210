package se.kth.ledbat.Driver;

import se.sics.kompics.util.Identifiable;

public class MyIdentifiable implements Identifiable<MyString> {

    MyString id;

    public MyIdentifiable(String id) {
        this.id = new MyString(id);
    }

    @Override
    public MyString getId() {
        return id;
    }
}

