package dbtLab3;

import java.util.List;

public class Performance {
	private String movie;
	private String showdate;
	private String theater;
	private String nbr_seats;
	
	/**
	 * Takes in a list of strings which values are of order
	 * movie, showdate, theater, nbrseats
	 * */
	Performance(List<String> values){
		if(values.size() != 4){
			System.out.print("Wrong length of value list in Performance.");
		}
		this.movie = values.get(0);
		this.showdate = values.get(1);
		this.theater = values.get(2);
		this.nbr_seats = values.get(3);
	}
	public String getMovie(){return movie;};
	public String getShowdate(){return showdate;};
	public String getTheater(){return theater;};
	public String getNbrSeats(){return nbr_seats;};
}
