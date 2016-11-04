package androidchallenge.starlabs.org.starwarsapp.Models;

import java.util.List;

/**
 * Created by AveNGeR on 19-10-2016.
 * Model class for the ships with private data members
 * and appropriate Constructor, getters and setters
 */
public class StarShip {

    private String name;
    private long cost;
    private List<StarShip.FilmUrl> filmUrl;
    private List<String> films;

    public StarShip(){
    }

    /*Constructor required if we create a new Starship object by passing parameters

    public StarShip(String name, int cost, List<String> films) {
        this.name = name;
        this.cost = cost;
        this.films = films;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public List<String> getFilms() {
        return films;
    }

    public void setFilms(List<String> films) {
        this.films = films;
    }

    public List<StarShip.FilmUrl> getFilmUrl() {
        return filmUrl;
    }

    public void setFilmUrl(List<StarShip.FilmUrl> filmUrl) {
        this.filmUrl = filmUrl;
    }

    public static class FilmUrl{
        private String filmUrl;

        public String getFilmUrl() {
            return filmUrl;
        }

        public void setFilmUrl(String filmUrl) {
            this.filmUrl = filmUrl;
        }

        @Override
        public String toString() {
            return ""+filmUrl;
        }
    }

    @Override
    public String toString() {
        return ""+name +" "+ cost+" "+filmUrl;
    }
}

