package pl.smollini.springbootmapinit;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class Covid19Parser {

    String url = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    public List<Point> getCovidData() throws IOException {
        List<Point> points = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        String values = restTemplate.getForObject(url, String.class);

        StringReader stringReader = new StringReader(values);
        CSVParser parse = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(stringReader);
        String date = String.valueOf(toDate());
        for (CSVRecord strings : parse) {
         try {
             double lat = Double.parseDouble(strings.get("Lat"));
             double lon = Double.parseDouble(strings.get("Long"));
             String text = strings.get(toDate());
             points.add(new Point(lat, lon, text));
         }
         catch(Exception e)
         {

         }
        }
        return points;
    }
    private String toDate()
    {
        String yesterdayDate = null;
        //Initiate a calendar instance
        Calendar calendar = Calendar.getInstance();
        //subtract 1 date from current date
        calendar.add(Calendar.DATE, -1);
        //format date
        DateFormat dateFormat = new SimpleDateFormat("M/dd/YY");
        //get formatted date
        yesterdayDate = dateFormat.format(calendar.getTime());
        return yesterdayDate;
    }


}
