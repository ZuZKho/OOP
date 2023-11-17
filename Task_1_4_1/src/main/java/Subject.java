public class Subject {

    String title;
    Mark mark;

    public Subject(String title, Mark mark) {
        this.title = title;
        this.mark = mark;
    }

    String title() {
        return this.title;
    }

    Mark mark() {
        return this.mark;
    }
}
