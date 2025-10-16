package models;

public class CarMarket {

    public CarList allCars = new CarList();

    // a list of cars that are manufactured later than the threshold specified
    public CarList getNewerCars(int fromYear) {
        CarList cars = new CarList();
        for (Car c : allCars) {
            if (c.getYear() >= fromYear) {
                cars.add(c);
            }
        }
        return cars;
    }

    // list of cars whose price is between the limits
    public CarList getByPrice(int fromPrice, int toPrice) {
        CarList cars = new CarList();
        for (Car c : allCars) {
            if (c.getPrice() >= fromPrice && c.getPrice() <= toPrice) {
                cars.add(c);
            }
        }
        return cars;
    }

    // list of cars with the highest mileage
    public CarList getHighestMileageCars() {
        CarList cars = new CarList();
        double maxMileage = 0;
        for (Car c : allCars) {
            double mileage = c.getMileage();
            if (mileage >= maxMileage) {
                if (mileage > maxMileage) {
                    cars.clear();
                    maxMileage = mileage;
                }
                cars.add(c);
            }
        }
        return cars;
    }

    // list of cars whose model code matches the specified one
    public CarList getByMakeAndModel(String makeAndModel) {
        CarList cars = new CarList();
        for (Car c : allCars) {
            String carMakeAndModel = c.getMake() + " " + c.getModel();
            if (carMakeAndModel.startsWith(makeAndModel)) {
                cars.add(c);
            }
        }
        return cars;
    }
    // no main method -> demo testing in class ManualTest
}
