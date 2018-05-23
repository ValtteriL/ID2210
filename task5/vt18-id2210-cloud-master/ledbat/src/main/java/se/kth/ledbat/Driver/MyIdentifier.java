package se.kth.ledbat.Driver;

import se.sics.kompics.util.Identifiable;
import se.sics.kompics.util.Identifier;

import java.util.Objects;

public class MyIdentifier implements Identifiable<MyString> {

    MyString id;

    public MyIdentifier(String id) {
        this.id = new MyString(id);
    }

    @Override
    public MyString getId() {
        return id;
    }
}

