fun main() {

    val park = Park()

    val track = Truck(
        5000.0, 2000.0, CarcaseType.AWNING, 1995, "Volvo", "FH I-Save",
        FuelType.DIESEL, 20.0
    )
    val passengerCar = PassengerCar(
        20, 2001, "Hyundai", "Universe", FuelType.PETROL,
        16.0
    )
    val universalCar = UniversalCar(
        2000.0, 400.0, 10, CarcaseType.REFRIGERATOR, 2008, "Citroen",
        "Jumpy", FuelType.GAS, 12.0
    )

    val cars = mutableListOf(track, passengerCar, universalCar)

    cars.forEach { it.refuel() }
    cars.forEach { it.repair() }

    track.loadCargoOrder(CargoOrder(12.0, ProductType.WATER, 100.0, "Belarus", "Russia"))
    track.showCargoOrders()
    track.clearCargoOrders()
    track.sealSalon()

    val order = PassengerOrder(20, "Russia", "China")
    passengerCar.loadPassengerOrder(order)
    passengerCar.showPassengerOrders()
    passengerCar.unloadPassengerOrder(order)
    passengerCar.disinfectSalon()

    universalCar.loadPassengerOrder(order)
    universalCar.clearPassengerOrders()
    universalCar.disinfectSalon()
    universalCar.sealSalon()

    park.cars.addAll(cars)
}

open class Car(val releaseYear: Int, val brand: String, val model: String, val fuelType: FuelType, val fuelConsumption: Double) {

    fun refuel() {
        println("Car was refueled.")
    }

    fun repair() {
        println("Car was repaired.")
    }
}

class Truck(
    private var volume: Double,
    private var loadCapacity: Double,
    private val carcaseType: CarcaseType,
    releaseYear: Int,
    brand: String,
    model: String,
    fuelType: FuelType,
    fuelConsumption: Double,
): Car(releaseYear, brand, model, fuelType, fuelConsumption), CargoTransit, CargoAction {

    override val cargoOrders = ArrayList<CargoOrder>()

    override fun showCargoOrders() {
        cargoOrders.forEach { println(it) }
    }

    override fun clearCargoOrders() {
        cargoOrders.clear()
    }

    override fun loadCargoOrder(order: CargoOrder): Boolean {
        if(loadCapacity > order.weight && volume > order.volume && carcaseType.types.contains(order.productType)) {
            volume -= order.volume
            loadCapacity -= order.weight
            cargoOrders.add(order)
            return true
        }
        return false
    }

    override fun unloadCargoOrder(order: CargoOrder): Boolean {
        if(cargoOrders.contains(order)) {
            volume += order.volume
            loadCapacity += order.weight
            cargoOrders.remove(order)
            return true
        }
        return false
    }

    override fun sealSalon() {
        println("Salon was sealed.")
    }
}

class PassengerCar(
    private var passengerCapacity: Int,
    releaseYear: Int,
    brand: String,
    model: String,
    fuelType: FuelType,
    fuelConsumption: Double,
): Car(releaseYear, brand, model, fuelType, fuelConsumption), PassengerTransit, PassengerAction {

    override val passengerOrders = ArrayList<PassengerOrder>()

    override fun showPassengerOrders() {
        passengerOrders.forEach { println(it) }
    }

    override fun clearPassengerOrders() {
        passengerOrders.clear()
    }

    override fun loadPassengerOrder(order: PassengerOrder): Boolean {
        if(passengerCapacity > order.passengerCapacity) {
            passengerCapacity -= order.passengerCapacity
            passengerOrders.add(order)
            return true
        }
        return false
    }

    override fun unloadPassengerOrder(order: PassengerOrder): Boolean {
        if(passengerOrders.contains(order)) {
            passengerCapacity += order.passengerCapacity
            passengerOrders.remove(order)
            return true
        }
        return false
    }

    override fun disinfectSalon() {
        println("Salon was disinfected.")
    }
}

class UniversalCar(
    private var volume: Double,
    private var loadCapacity: Double,
    private var passengerCapacity: Int,
    private val carcaseType: CarcaseType,
    releaseYear: Int,
    brand: String,
    model: String,
    fuelType: FuelType,
    fuelConsumption: Double,
): Car(releaseYear, brand, model, fuelType, fuelConsumption),
    CargoTransit, PassengerTransit, CargoAction, PassengerAction {

    override val cargoOrders = ArrayList<CargoOrder>()
    override val passengerOrders = ArrayList<PassengerOrder>()

    override fun showCargoOrders() {
        cargoOrders.forEach { println(it) }
    }

    override fun clearCargoOrders() {
        cargoOrders.clear()
    }

    override fun loadCargoOrder(order: CargoOrder): Boolean {
        if(loadCapacity > order.weight && volume > order.volume && carcaseType.types.contains(order.productType)) {
            volume -= order.volume
            loadCapacity -= order.weight
            cargoOrders.add(order)
            return true
        }
        return false
    }

    override fun unloadCargoOrder(order: CargoOrder): Boolean {
        if(cargoOrders.contains(order)) {
            volume += order.volume
            loadCapacity += order.weight
            cargoOrders.remove(order)
            return true
        }
        return false
    }

    override fun showPassengerOrders() {
        passengerOrders.forEach { println(it) }
    }

    override fun clearPassengerOrders() {
        passengerOrders.clear()
    }

    override fun loadPassengerOrder(order: PassengerOrder): Boolean {
        if(passengerCapacity > order.passengerCapacity) {
            passengerCapacity -= order.passengerCapacity
            passengerOrders.add(order)
            return true
        }
        return false
    }

    override fun unloadPassengerOrder(order: PassengerOrder): Boolean {
        if(passengerOrders.contains(order)) {
            passengerCapacity += order.passengerCapacity
            passengerOrders.remove(order)
            return true
        }
        return false
    }

    override fun disinfectSalon() {
        println("Salon was disinfected.")
    }

    override fun sealSalon() {
        println("Salon was sealed.")
    }
}

open class Order(val startPoint: String, val endPoint: String)

class PassengerOrder(
    val passengerCapacity: Int,
    startPoint: String,
    endPoint: String
): Order(startPoint, endPoint)

class CargoOrder(
    val volume: Double,
    val productType: ProductType,
    val weight: Double,
    startPoint: String,
    endPoint: String,
): Order(startPoint, endPoint)

class Park {
    val cars: MutableList<Car> = mutableListOf()
}

interface CargoTransit {
    val cargoOrders: ArrayList<CargoOrder>

    fun showCargoOrders()
    fun clearCargoOrders()
    fun loadCargoOrder(order: CargoOrder): Boolean
    fun unloadCargoOrder(order: CargoOrder): Boolean
}

interface PassengerTransit {
    val passengerOrders: ArrayList<PassengerOrder>

    fun showPassengerOrders()
    fun clearPassengerOrders()
    fun loadPassengerOrder(order: PassengerOrder): Boolean
    fun unloadPassengerOrder(order: PassengerOrder): Boolean
}

interface PassengerAction {
    fun disinfectSalon()
}

interface CargoAction {
    fun sealSalon()
}

enum class FuelType {
    GAS,
    DIESEL,
    PETROL,
}

enum class CarcaseType {
    AWNING {
        override val types = listOf(ProductType.INDUSTRIAL)
    },
    REFRIGERATOR {
        override val types = listOf(ProductType.FOOD)
    },
    CISTERN {
        override val types = listOf(ProductType.WATER)
    };

    abstract val types: List<ProductType>
}

enum class ProductType {
    INDUSTRIAL,
    FOOD,
    WATER,
}