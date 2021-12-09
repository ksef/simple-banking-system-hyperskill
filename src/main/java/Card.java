import java.util.Random;

public class Card {

    private final String BIN = "400000";
    private final int accNumber;
    private final int checksum;

    public Card() {
        Random rand = new Random();
        int randAccNumber = 100000000 + rand.nextInt(900000000);
        int check = makeCheckSum(randAccNumber);
        while (DBConnector.containCard(BIN + Integer.toString(randAccNumber + check))) {
            randAccNumber = rand.nextInt(999999999);
            check = makeCheckSum(randAccNumber);
        }
        this.accNumber = randAccNumber;
        this.checksum = check;
    }

    public int getAccNumber() {
        return this.accNumber;
    }

    public long getCardNumber() {
        String cardNumber = BIN + Integer.toString(this.accNumber) + Integer.toString(this.checksum);
        long num = Long.parseLong(cardNumber);
        return num;
    }

    public int makeCheckSum(int accNumber) {
        String tempCardNumber = BIN + Integer.toString(accNumber);
        String[] nums = tempCardNumber.split("");
        int sum = 0;
        int check = 0;
        for (int i = 0; i < tempCardNumber.length(); i++) {
            int num = Integer.parseInt(String.valueOf(tempCardNumber.charAt(i)));
            num = i %2 ==0? num*2:num;
            num = num>9? num-9:num;
            sum += num;
        }
        while (((check + sum) % 10) != 0) {
            check++;
        }
        return check;
    }

    public static boolean checkLoon(String cardNumber){
        String tempCardNumber = cardNumber.substring(0,cardNumber.length()-1);
        int  x = Integer.parseInt(cardNumber.substring(cardNumber.length()-1));
        int sum = 0;
        int check = 0;
        for (int i = 0; i < tempCardNumber.length(); i++) {
            int num = Integer.parseInt(String.valueOf(tempCardNumber.charAt(i)));
            num = i %2 ==0? num*2:num;
            num = num>9? num-9:num;
            sum += num;
        }
        while (((check + sum) % 10) != 0) {
            check++;
        }
        return check == x;
    }
}
