package structural.decorator;

public class Main {
    public static void main(String[] args) {
        Coffee coffee = new SimpleCoffee();
        System.out.println(coffee.getDescription() + " costs Rs. " + coffee.getCost());

        coffee = new MilkDecorator(coffee);
        System.out.println(coffee.getDescription() + " costs Rs. " + coffee.getCost());

        coffee = new ChocolateDecorator(coffee);
        System.out.println(coffee.getDescription() + " costs Rs. " + coffee.getCost());
    }
}
