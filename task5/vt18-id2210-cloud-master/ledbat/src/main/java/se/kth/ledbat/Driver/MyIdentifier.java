package se.kth.ledbat.Driver;

import se.sics.kompics.util.Identifier;

import java.util.Objects;

public class MyIdentifier implements Identifier {

    String id;

    public MyIdentifier(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyIdentifier that = (MyIdentifier) o;
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