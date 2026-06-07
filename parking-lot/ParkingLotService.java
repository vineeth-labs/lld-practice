import model.*;

public class ParkingLotService {
    ParkingLot parkingLot;
    TicketService ticketService;
    SpotAllocationStrategy spotAllocationStrategy;

    public ParkingLotService(TicketService ticketService) {
        parkingLot = new ParkingLot();
        this.ticketService = ticketService;
    }

    public void parkVehicle(String vehicleNumber, VehicleType vehicleType, Entrance entrance) {
        ParkingSpot spot = spotAllocationStrategy.getOptimalSpot(parkingLot, entrance, vehicleType);
        if (spot == null) {
            System.out.println("No spot available");
            return;
        }
        Ticket ticket = ticketService.createTicket(vehicleNumber, spot);
        System.out.println("Ticket created");
        spot.park();
    }

    public void unparkVehicle(Ticket ticket) {
        Double price = ticketService.exitTicket(ticket);
        System.out.println("Parking Lot price is " + price);
        ticket.getParkingSpot().unpark();
    }
}
