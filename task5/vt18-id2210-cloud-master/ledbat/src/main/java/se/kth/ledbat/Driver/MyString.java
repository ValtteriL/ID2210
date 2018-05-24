package se.kth.ledbat.Driver;

import se.sics.kompics.util.Identifier;

import java.io.Serializable;
import java.util.Objects;

public class MyString implements Identifier {

    String id;

    public MyString(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyString that = (MyString) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public int partition(int i) {
        return 0;
    }

    @Override
    public int compareTo(Identifier o) {
        return 0;
    }

    public String getId() {
        return id;
    }
}