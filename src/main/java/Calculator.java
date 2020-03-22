import java.util.List;

public class Calculator {
    public double calculateAverage(List<Rate> rates){
        return rates.stream().mapToDouble(Rate::getMid).average().getAsDouble();
    }
    public double calculateVarience(List<Rate> rates,double avg){
        double varience = rates.stream().mapToDouble(i->(i.getMid()-avg)).map(i->i*i).average().getAsDouble();
        return Math.sqrt(varience);
    }
}
