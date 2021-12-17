package banking;

import banking.model.Card;

public class CardValidator {

    private final String BINumber;

    public CardValidator() {
        this.BINumber = "400000";
    }

    public Card generate() {
        String number=getNewCardNumber();
        String PIN=getNewPin();
        return new Card(number, new StringBuilder(PIN));
    }

    private String getNewCardNumber(){
        String number = BINumber + getAccIdentifier();
        do {
            char checksum=getChecksumFor(number);
            number+=checksum;
        }while(!isValidCard(number));
        return number;
    }

    private String getAccIdentifier() {
        StringBuilder pin= new StringBuilder();
        for(int i=0; i<9; i++){
            pin.append((char) ((int) ((Math.random() * 10)) + '0'));
        }
        return pin.toString();
    }

    private String getNewPin(){
        StringBuilder pin= new StringBuilder();
        for(int i=0; i<4; i++){
            pin.append((char) ((int) ((Math.random() * 10)) + '0'));
        }
        return pin.toString();
    }

    private char getChecksumFor(String number){
        int checksum=0;
        for(int i=0; i<15; i++){
            int num=number.charAt(i)-'0';
            if(i%2==0) num*=2;
            if(num>9) num-=9;
            checksum+=num;
        }
        int charNum=10-(checksum%10);
        return (char)(charNum+'0');
    }

    public boolean isValidCard(String card){
        if(card.length()<16) return false;
        char checksum=getChecksumFor(card);
        if(card.charAt(15)!=checksum) return false;
        return true;
    }
}
