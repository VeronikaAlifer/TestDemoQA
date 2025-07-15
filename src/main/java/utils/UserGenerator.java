package utils;

public class UserGenerator {

    public String getRandomFirstName() {
        return "firstName" + getRandomValue();
    }

    private int getRandomValue() {
        return (int) (Math.random() * 500);
    }
}
