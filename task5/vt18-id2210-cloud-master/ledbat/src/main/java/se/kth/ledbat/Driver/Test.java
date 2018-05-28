package se.kth.ledbat.Driver;

public class Test {

    public static void main(String[] args) {


        MyIdentifier i1 = new MyIdentifier("hello");
        MyIdentifier i2 = new MyIdentifier("hello");
        MyIdentifier i3 = new MyIdentifier("hello3");
        System.out.println("Compare (same): " + i1.compareTo(i2));
        System.out.println("Compare (not same): " + i1.compareTo(i3));

        System.out.println("String compareTo");
        System.out.println("hello compareTo hello -> " + "hello".compareTo("hello"));
        System.out.println("hello compareTo hello3 -> " + "hello".compareTo("hello3"));

        System.out.println("Integer compareTo");
        System.out.println("1 compareTo 5 -> " + new Integer(1).compareTo(new Integer(5)));
        System.out.println("5 compareTo 1 -> " + new Integer(5).compareTo(new Integer(1)));
        System.out.println("0 compareTo 0 -> " + new Integer(0).compareTo(new Integer(0)));

        System.out.println("Equals (same): " + i1.equals(i2));
        System.out.println("Equals (not same): " + i1.equals(i3));


        System.out.println("Hashcode (same): " + i1.hashCode() + " AND " + i2.hashCode());
        System.out.println("Hashcode (not same): " + i1.hashCode() + " AND " + i3.hashCode());

    }
}
