package com.sl.rbcweather.service;

import java.io.IOException;
import java.util.Date;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.fedy2.weather.YahooWeatherService;
import com.github.fedy2.weather.data.Channel;
import com.github.fedy2.weather.data.unit.DegreeUnit;
import com.sl.rbcweather.model.Board;

/**
 * @author Santiago Leiva
 * 03/05/2018
 */
public class WeatherServiceYahoo implements WeatherService {
	private YahooWeatherService service;
	
	@Override
	public Board updateBoard(Board board) {
		return updateBoard(board, null);
	}

	@Override
	public Board updateBoard(Board board, Date lastCheck) {
		try {
			this.service = new YahooWeatherService();
			
			Channel channel = this.service.getForecast(board.getWoeid(), DegreeUnit.FAHRENHEIT);
			
			board.setTemperature(channel.getItem().getCondition().getTemp());
			board.setDegreeUnits(channel.getUnits().getTemperature().name());
			board.setCode(channel.getItem().getCondition().getCode());
			board.setDescription(channel.getItem().getCondition().getText());
			board.setIconPath("http://l.yimg.com/a/i/us/we/52/".concat(String.valueOf(channel.getItem().getCondition().getCode())).concat(".gif"));
			
			if ( lastCheck != null )
				board.setLastCheck(lastCheck);
			else
				board.setLastCheck(new Date());
			
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		}
		
		return board;
	}

}
