package androidchallenge.starlabs.org.starwarsapp.utilities;

import java.util.Comparator;

import androidchallenge.starlabs.org.starwarsapp.Models.StarShip;

/**
 * Created by AveNGeR on 31-10-2016.
 * CostComparator implementing Comparator to sort the ships based on their cost
 */
public class CostComparator implements Comparator<StarShip> {
    @Override
    public int compare(StarShip ship1, StarShip ship2) {
        return Double.compare(ship1.getCost(), ship2.getCost());
    }
}
