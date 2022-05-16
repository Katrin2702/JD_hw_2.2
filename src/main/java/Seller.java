import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Seller {

    private CarShowroom carShowroom;
    private int sellTime = 1;
    Lock lock;
    Condition condition;

    public Seller(CarShowroom carShowroom) {
        lock = new ReentrantLock(true);
        condition = lock.newCondition();
        this.carShowroom = carShowroom;
    }

    public void receiveCar() {
        lock.lock();
        try {
            System.out.println("Производитель Toyota выпустил 1 авто");
            carShowroom.getCars().add(new Car());
            condition.signal();
        } catch (IllegalMonitorStateException e) {
            e.printStackTrace();

        } finally {
            lock.unlock();
        }
    }

    public Car sellCar() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + " зашел в автосалон");
            while (carShowroom.getCars().size() == 0) {
                System.out.println("Машин нет");

                condition.await();
            }
            Thread.sleep(sellTime);
            System.out.println(Thread.currentThread().getName() + " уехал на новеньком авто");
        } catch (IllegalMonitorStateException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return carShowroom.getCars().remove(0);
    }

}
