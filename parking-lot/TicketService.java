import model.Entrance;
import model.ParkingSpot;
import model.Ticket;
import model.VehicleType;

public class TicketService {

    private FareCalculationStrategy fareCalculationStrategy;

    public Ticket createTicket(String vehicleNumber, ParkingSpot spot) {
        Ticket ticket = new Ticket(vehicleNumber, spot);
        return ticket;
    }

    public Double exitTicket(Ticket ticket) {
        Double fare = fareCalculationStrategy.calculateFare(ticket);
        ticket.setPrice(fare);
        return fare;
    }

    public void setFareCalculationStrategy(FareCalculationStrategy fareCalculationStrategy) {
        this.fareCalculationStrategy = fareCalculationStrategy;
    }
}
