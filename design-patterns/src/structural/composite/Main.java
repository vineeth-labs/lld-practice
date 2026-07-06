package structural.composite;

public class Main {
    public static void main(String[] args) {
        MenuSection mainMenu = new MenuSection("Restaurant Menu");

        MenuSection breakfastMenu = new MenuSection("Breakfast");
        breakfastMenu.add(new MenuItem("Masala Dosa", 120.0));
        breakfastMenu.add(new MenuItem("Idli Vada", 90.0));

        MenuSection dessertMenu = new MenuSection("Desserts");
        dessertMenu.add(new MenuItem("Gulab Jamun", 80.0));
        dessertMenu.add(new MenuItem("Ice Cream", 100.0));

        MenuSection dinnerMenu = new MenuSection("Dinner");
        dinnerMenu.add(new MenuItem("Paneer Butter Masala", 240.0));
        dinnerMenu.add(new MenuItem("Veg Biryani", 220.0));
        dinnerMenu.add(dessertMenu);

        mainMenu.add(breakfastMenu);
        mainMenu.add(dinnerMenu);

        mainMenu.print("");
    }
}
