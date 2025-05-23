import java.util.Random;

public class Hitter extends Player {
    private int contact;
    private int batSpeed;
    private int barrelControl;
    private int eye;

    private String approach;

    private String tendency; // flyball, linedrive, groundball
    private Integer[] bbDirection; // 0 is pull, 1 is center, 2 is oppo

    public Hitter() {
        super();

        Random random = new Random();

        contact = random.nextInt(100);
        batSpeed = (int) (18 * random.nextDouble()) + 60;
        barrelControl = random.nextInt(100);
        eye = random.nextInt(100);

        approach = (String) getRandomElement(new Object["contact", "aggressive", "power", "passive", "balanced"], 5);
        tendency = (String) getRandomElement(new Object["flyball", "linedrive", "groundball"], 3)

        bbDirection = new Integer[3];

        bbDirection[0] = 25 + random.getNextInt(30);
        bbDirection[1] = 21 + random.getNextInt(30);
        bbDirection[2] = 100 - bbDirection[1] - bbDirection[0];

    }

    private Object getRandomElement(Object[] array, int size) {
        Random random = new Random();
        return array[random.getNextInt(size)];
    }



}