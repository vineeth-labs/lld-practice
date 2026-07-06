package structural.proxy;

public class ConfidentialDocument implements Document {
    private final String fileName;

    public ConfidentialDocument(String fileName) {
        this.fileName = fileName;
        loadFromDisk();
    }

    private void loadFromDisk() {
        System.out.println("Loading confidential document: " + fileName);
    }

    @Override
    public void display() {
        System.out.println("Displaying confidential document: " + fileName);
    }
}
