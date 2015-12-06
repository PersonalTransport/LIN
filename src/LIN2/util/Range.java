package LIN2.util;

// Why does java not have this class????
public class Range<E> {
    private E begin;
    private E end;

    public Range(E begin, E end) {
        this.begin = begin;
        this.end = end;
    }

    public E getBegin() {
        return begin;
    }

    public void setBegin(E begin) {
        this.begin = begin;
    }

    public E getEnd() {
        return end;
    }

    public void setEnd(E end) {
        this.end = end;
    }
}
