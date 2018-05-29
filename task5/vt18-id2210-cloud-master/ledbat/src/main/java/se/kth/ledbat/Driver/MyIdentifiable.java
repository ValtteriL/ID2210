package se.kth.ledbat.Driver;

import se.sics.kompics.util.Identifiable;

public class MyIdentifiable implements Identifiable<MyIdentifier> {

    MyIdentifier id;

    public MyIdentifiable(String id) {
        this.id = new MyIdentifier(id);
    }

    @Override
    public MyIdentifier getId() {
        return id;
    }
}

