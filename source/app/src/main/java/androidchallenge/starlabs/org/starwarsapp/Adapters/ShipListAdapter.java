package androidchallenge.starlabs.org.starwarsapp.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import java.util.List;

import androidchallenge.starlabs.org.starwarsapp.JsonParsing.StarShipsActivity;
import androidchallenge.starlabs.org.starwarsapp.Models.StarShip;
import androidchallenge.starlabs.org.starwarsapp.R;

/**
 * Created by AveNGeR on 22-10-2016.
 */
public class ShipListAdapter extends RecyclerView.Adapter<ShipListAdapter.ShipListHolder> {

    private List<StarShip> shipList;
    private LayoutInflater inflater;

    public ShipListAdapter(List<StarShip> shipList, Context c) {
        inflater = LayoutInflater.from(c);
        this.shipList = shipList;
    }

    @Override
    public ShipListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        return new ShipListHolder(view);
    }

    @Override
    public void onBindViewHolder(ShipListHolder holder, int position) {
        StarShip starShip = shipList.get(position);
        holder.tvShipname.setText(starShip.getName());
        holder.tvShipCost.setText(String.valueOf(starShip.getCost()));
        /*Executing the Async task Commented below
        new DownloadFilmListTask(holder.tvFilmLsit).execute(starShip.getFilmUrl());
        */
        String films ="";
        for(int i = 0; i<starShip.getFilms().size();i++) {
            films = films + starShip.getFilms().get(i)+"\n";
        }
        holder.tvFilmLsit.setText(films);

    }

    @Override
    public int getItemCount() {
        return shipList.size();
    }

    class ShipListHolder extends RecyclerView.ViewHolder {

        private TextView tvShipname;
        private TextView tvShipCost;
        private TextView tvFilmLsit;
        private View container;

        public ShipListHolder(View itemView) {
            super(itemView);

            tvShipname = (TextView) itemView.findViewById(R.id.tvShipname);
            tvShipCost = (TextView) itemView.findViewById(R.id.tvShipCost);
            tvFilmLsit = (TextView) itemView.findViewById(R.id.tvFilmLsit);
            container = itemView.findViewById(R.id.rows_of_list);
        }
    }

    /***
     * The Follwing piece is an alternative way to download the film title from the films url.
     * Intially the 15 most expensive ships with name and cost are displayed on the UI and in the background
     * film title is downloaded and is presented one by one
     * In this way we can significantly reduce the intial loading time of the activity as the film titles gets downloaded
     * after the ui is created and then one by one film names are dispayed.
     */
    /*private class DownloadFilmListTask extends AsyncTask<List<StarShip.FilmUrl>, Void, String> {
        TextView tvFilmLsit;

        public DownloadFilmListTask(TextView tvFilmLsit) {
            this.tvFilmLsit = tvFilmLsit;
        }

        @Override
        protected String doInBackground(List<StarShip.FilmUrl>... list) {
            List<StarShip.FilmUrl> filmUrlList = list[0];
            String films="";
                        for (int j = 0; j < filmUrlList.size(); j++) {
                            URL filmurl = null;
                            try {
                                filmurl = new URL(filmUrlList.get(j).toString());
                                HttpURLConnection con = (HttpURLConnection) filmurl.openConnection();
                                con.connect();
                                InputStream inputStream = con.getInputStream();
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                                StringBuffer bf = new StringBuffer();

                                String line1 = "";
                                 while ((line1 = bufferedReader.readLine()) != null) {
                                     bf.append(line1);
                                 }
                                Log.i("json", bf.toString());
                                JSONObject jsonObject = new JSONObject(bf.toString());
                                films=films+jsonObject.getString("title")+"\n";
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
            return films;
        }

        protected void onPostExecute(String result) {
            tvFilmLsit.setText(result);
        }

    }*/
}

