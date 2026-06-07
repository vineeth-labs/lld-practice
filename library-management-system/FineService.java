import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import model.Fine;

public class FineService {
    private static final double FIXED_FINE_PER_DAY = 5.0;

    private final List<Fine> fines;
    private final UserService userService;

    public FineService(UserService userService) {
        if (userService == null) {
            throw new IllegalArgumentException("UserService cannot be null");
        }
        this.userService = userService;
        this.fines = new ArrayList<>();
    }

    public void collectFine(String studentId, LocalDateTime borrowDate, LocalDateTime returnDate) {
        userService.validateStudent(studentId);

        long overdueDays = ChronoUnit.DAYS.between(borrowDate.plusDays(14), returnDate);
        if (overdueDays <= 0) {
            return;
        }

        double fineAmount = overdueDays * FIXED_FINE_PER_DAY;
        Fine fine = new Fine(UUID.randomUUID().toString(), studentId, fineAmount);
        fines.add(fine);
    }

    public List<Fine> getFinesForStudent(String studentId) {
        userService.validateStudent(studentId);
        return Collections.unmodifiableList(fines.stream()
                .filter(fine -> fine.getStudentId().equals(studentId))
                .collect(Collectors.toList()));
    }

    public double getTotalFineForStudent(String studentId) {
        return getFinesForStudent(studentId).stream()
                .mapToDouble(Fine::getAmount)
                .sum();
    }

    public List<Fine> getAllFines() {
        return Collections.unmodifiableList(new ArrayList<>(fines));
    }

    public void payFine(String fineId, String studentId) {
        userService.validateStudent(studentId);
        fines.removeIf(fine -> fine.getId().equals(fineId) && fine.getStudentId().equals(studentId));
    }
}
