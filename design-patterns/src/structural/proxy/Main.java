package structural.proxy;

public class Main {
    public static void main(String[] args) {
        Document employeeDocument = new DocumentProxy("salary-report.pdf", "EMPLOYEE");
        employeeDocument.display();

        Document adminDocument = new DocumentProxy("salary-report.pdf", "ADMIN");
        adminDocument.display();
        adminDocument.display();
    }
}
