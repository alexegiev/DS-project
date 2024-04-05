//package entities;
//
//public class Manager {
//    private List<Accommodation> accommodations;
//
//    public Manager() {
//        this.accommodations = new ArrayList<>();
//    }
//
//    public void addAccommodation(String name) {
//        accommodations.add(new Accommodation(name));
//    }
//
//    public void addAvailableDate(String accommodationName, Date date) {
//        for (Accommodation accommodation : accommodations) {
//            if (accommodation.getName().equals(accommodationName)) {
//                accommodation.addAvailableDate(date);
//                return;
//            }
//        }
//        System.out.println("Accommodation not found!");
//    }
//
//    public void displayAccommodationReservations(String accommodationName) {
//        for (Accommodation accommodation : accommodations) {
//            if (accommodation.getName().equals(accommodationName)) {
//                accommodation.displayReservations();
//                return;
//            }
//        }
//        System.out.println("Accommodation not found!");
//    }
//
//    public void makeReservation(String accommodationName, String tenantName, Date date) {
//        for (Accommodation accommodation : accommodations) {
//            if (accommodation.getName().equals(accommodationName)) {
//                accommodation.addReservation(new Reservation(tenantName, date));
//                return;
//            }
//        }
//        System.out.println("Accommodation not found!");
//    }
//}
