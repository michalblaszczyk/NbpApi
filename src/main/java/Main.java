import org.springframework.context.annotation.Bean;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Calculator calculator = new Calculator();
    private static final String requestUrl = "http://api.nbp.pl/api/exchangerates/rates/a/";
    private boolean running = true;

    public static void main(String[] args){
        new Main().run();
    }

    private void run(){
        System.out.println("Currency rate search:");
        while (running){
            System.out.println("Please enter first date, second date and currency(eur,usd,gbp,chf)" +
                    " to get average and standard deviation of them (press enter after each value)");

            System.out.println("Note: date format: yyyy-MM-dd");
            System.out.println("Note: Limit of 367 days cannot be exceeded");
            System.out.println("To exit the program enter \"quit\" or \"q\"");
            performAction();
        }
        System.out.println("Thank you for using currency search");
    }

    private void performAction() {
        String line = scanner.nextLine();
        if (line.equals("quit") || line.equals("q")) {
            System.out.println("See you");
            running = false;
        } else {
            try {
                String firstDate = line;
                String secondDate = scanner.nextLine();
                String currency = scanner.nextLine();
                if (isValidDate(firstDate) && isValidDate(secondDate) && isValidCurrency(currency)) {
                    Currency cur = getRestTemplate().getForObject(makeFullRequest(firstDate,secondDate,currency),Currency.class);
                    List<Rate> tj = cur.getRates();
                    double avg = calculator.calculateAverage(tj);
                    double tem = calculator.calculateVarience(tj, avg);
                    printInfo(avg,tem,currency,firstDate,secondDate);
                    running = false;
                } else {
                    System.out.println("You enter invalid data");
                }
            } catch (HttpClientErrorException e) {
                System.out.println("Invalid range of date");
            }
        }
    }
    private static void printInfo(double avg, double dev, String currency,String firtDate, String second){
        System.out.println("Result of searching: ");
        System.out.println("Currency: " + currency + "\nRange od dates: " + firtDate+" - " + second);
        System.out.println("Avarage: " + avg + "\nStandard deviation: " + dev);
    }

    public static boolean isValidDate(String text) {
        if (text == null || !text.matches("\\d{4}-[01]\\d-[0-3]\\d"))
            return false;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        try {
            df.parse(text);
            return true;
        } catch (ParseException ex) {
            return false;
        }
    }
    public static boolean isValidCurrency(String cur){
        String text = cur.toLowerCase();
        if(text.equals("eur") || text.equals("chf") || text.equals("usd") || text.equals("gbp")) {
            return true;
        } else{
            return false;
        }
    }

    private static String makeFullRequest(String firstDate, String secondDate, String currency){
        return requestUrl + "/" + currency + "/" + firstDate + "/" + secondDate + "/?format=json";
    }

    @Bean
    public static RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
