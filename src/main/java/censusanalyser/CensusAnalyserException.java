package censusanalyser;

public class CensusAnalyserException extends Exception {

    enum ExceptionType {
        CENSUS_FILE_PROBLEM,UNABLE_TO_PARSE, UNABLE_TO_IDENTIFY_DELIMITER, EMPTY_FILE
    }

   public ExceptionType type;

    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }


    public CensusAnalyserException(String message, String name) {
        super(message);
        this.type = ExceptionType.valueOf(name);
    }
}
