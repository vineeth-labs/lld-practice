package structural.proxy;

public class DocumentProxy implements Document {
    private final String fileName;
    private final String userRole;
    private ConfidentialDocument confidentialDocument;

    public DocumentProxy(String fileName, String userRole) {
        this.fileName = fileName;
        this.userRole = userRole;
    }

    @Override
    public void display() {
        if (!"ADMIN".equalsIgnoreCase(userRole)) {
            System.out.println("Access denied for role: " + userRole);
            return;
        }

        if (confidentialDocument == null) {
            confidentialDocument = new ConfidentialDocument(fileName);
        }

        confidentialDocument.display();
    }
}
