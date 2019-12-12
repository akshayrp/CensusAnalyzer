package censusanalyser;

import com.opencsv.bean.CsvBindByName;

public class IndiaCensusCSV
{

   @CsvBindByName(column = "State", required = true)
   public String state;

   @CsvBindByName(column = "Population", required = true)
   public int population;

   @CsvBindByName(column = "AreaInSqKm", required = true)
   public Double areaInSqKm;

   @CsvBindByName(column = "DensityPerSqKm", required = true)
   public Double densityPerSqKm;
}

