package androidchallenge.starlabs.org.starwarsapp.JsonParsing;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidchallenge.starlabs.org.starwarsapp.Adapters.ShipListAdapter;
import androidchallenge.starlabs.org.starwarsapp.Models.StarShip;
import androidchallenge.starlabs.org.starwarsapp.R;
import androidchallenge.starlabs.org.starwarsapp.utilities.CostComparator;

public class StarShipsActivity extends AppCompatActivity {

    List<StarShip> shipList;
    String jsonUrl;
    ProgressDialog dialog;

    private RecyclerView recyclerView;
    private ShipListAdapter shipListAdapter;

    //onCreate method, intialize layout and recycler view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_star_ships);

        //general format of the url, page number needs to added accordingly
        jsonUrl = "http://swapi.co/api/starships/?page=";
        shipList = new ArrayList<>();
        recyclerView = (RecyclerView)findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        /***
         *  Calling Background Thread to download json file
         *  Data that needs to be presented in the Activity is obtained from json objects and stored as
         *  a list of Objects of type StarShip.
         */
        new JsonParseTask().execute(jsonUrl);
    }

    // Async task for downloading and Parsing Json Data
    private class JsonParseTask extends AsyncTask<String, String, List<StarShip>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(StarShipsActivity.this);
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting to server");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected List<StarShip> doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            int x =1;
            try {
                /***
                 *do-while loop to download and parse json data from all four pages
                 * Since the number of pages are known using simple while loop for 4 iterations
                 * for unknown number of pages we can read the "next" parameter value or we can use
                 * the "count" parameter to read all the data till the end
                 */
                do {
                    URL url = new URL(params[0]+x++);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream stream = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();

                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    String finalJson = buffer.toString();  // final json data obtained from the current page
                    Log.i("jsonData", finalJson);  // maintaining logs to check the downloaded json data
                    JSONObject parentObject = new JSONObject(finalJson);
                    JSONArray parentArray = parentObject.getJSONArray("results");

                    // for loop to read the json data from json objects and store in the list of Type Starships
                    for (int i = 0; i < parentArray.length(); i++) {
                        JSONObject finalObject = parentArray.getJSONObject(i);
                        StarShip starShip = new StarShip();
                        starShip.setName(finalObject.getString("name"));
                        String cost = finalObject.getString("cost_in_credits");
                        /*
                        Since we need the cost in Number format, using if-else to eliminate "Unknown" values with 0
                        and parsing the rest into correct number format
                         */
                        if (cost.equals("unknown"))
                            starShip.setCost(0);
                        else
                            starShip.setCost(Long.parseLong(cost));

                            //creating the list of films url
                            List<StarShip.FilmUrl> filmurlList = new ArrayList<>();
                            for (int j = 0; j < finalObject.getJSONArray("films").length(); j++) {
                                StarShip.FilmUrl filmUrl = new StarShip.FilmUrl();
                                filmUrl.setFilmUrl(finalObject.getJSONArray("films").getString(j));
                                filmurlList.add(filmUrl);
                             }
                        starShip.setFilmUrl (filmurlList);
                        shipList.add(starShip); // adding the created object to the shipList
                    }
                }while(x<5);

                Log.i("list",shipList.toString());
                return shipList;

            } catch (MalformedURLException | JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<StarShip> result) {
            super.onPostExecute(result);
            /***
             * Since we need only the 15 most expensive ships
             * using Collections Class and Comparator interface sort the list
             * creating a copy of the list with only 15 most expensive ships
             * Creating a new background thread to download the film title of the 15 ships in new list
             */
            if (result!=null){
                Collections.sort(shipList,new CostComparator());
                Collections.reverse(shipList);
                List<StarShip> shipListCopy = shipList.subList(0,15);
                new DownloadFilmListTask().execute(shipListCopy);
            }else{
                Toast.makeText(getApplicationContext(), "Not able to fetch data from server.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //Async task to download and store the film title using the films url to the corresponding Starship objects
    private class DownloadFilmListTask extends AsyncTask<List<StarShip>, Void, List<StarShip>> {

        @Override
        protected List<StarShip> doInBackground(List<StarShip>... list) {
            List<StarShip> shipListCopy = list[0];
            for (int i = 0;i<shipListCopy.size();i++){
                List<StarShip.FilmUrl> filmUrlList = shipListCopy.get(i).getFilmUrl();
                List<String> filmList = new ArrayList<>();
                for (int j = 0; j < filmUrlList.size(); j++) {
                    URL filmurl = null;
                    try {
                        filmurl = new URL(filmUrlList.get(j).toString());
                        HttpURLConnection con = (HttpURLConnection) filmurl.openConnection();
                        con.connect();
                        InputStream inputStream = con.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer stringBuffer = new StringBuffer();

                        String line = "";
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuffer.append(line);
                        }
                        Log.i("json", stringBuffer.toString());
                        JSONObject jsonObject = new JSONObject(stringBuffer.toString());
                        filmList.add(jsonObject.getString("title"));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                shipListCopy.get(i).setFilms(filmList);
            }
           return shipListCopy;
        }

        @Override
        protected void onPostExecute(List<StarShip> starShipsListFinal) {
            /***
             * instantiating and setting the Adapter class to create recycler list view to present the data in UI
             */
            dialog.dismiss();
            shipListAdapter = new ShipListAdapter(starShipsListFinal,getBaseContext());
            recyclerView.setAdapter(shipListAdapter);
        }
    }
}
