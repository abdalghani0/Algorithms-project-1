public class Data {
    int height;
    int width;
    char name;
    char relation;
    Data(int width, int height, char name) {
        this.height = height;
        this.width = width;
        this.name = name;
    }

    public String printData(String status) {
        if(status.equals("father")) {
            return "{" +
                    "relation=" + relation +
                    ", width=" + width +
                    ", height=" + height +
                    "}";
        }
        return "{" +
                "width=" + width +
                ", height=" + height +
                ", name=" + name +
                '}';
    }

    Data(char relation) {
        this.relation = relation;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public char getName() {
        return name;
    }

    public void setName(char name) {
        this.name = name;
    }

    public char getRelation() {
        return relation;
    }

    public void setRelation(char relation) {
        this.relation = relation;
    }
}                                                                                                       