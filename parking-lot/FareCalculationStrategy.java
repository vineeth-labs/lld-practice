import model.Ticket;

public interface FareCalculationStrategy {

    public double calculateFare(Ticket ticket);
}
