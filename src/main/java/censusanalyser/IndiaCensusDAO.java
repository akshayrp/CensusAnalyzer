package censusanalyser;

public class IndiaCensusDAO
{
   public String state;
   public String StateCode;
   public int areaInSqKm;
   public int densityPerSqKm;
   public int population;

   public IndiaCensusDAO(IndiaCensusCSV indiaCensusCSV)
   {
      state = indiaCensusCSV.state;
      areaInSqKm = indiaCensusCSV.areaInSqKm;
      densityPerSqKm = indiaCensusCSV.densityPerSqKm;
      population = indiaCensusCSV.population;
   }
}
