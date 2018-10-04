package com.apap.tutorial4.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.apap.tutorial4.model.FlightModel;
import com.apap.tutorial4.model.PilotModel;
import com.apap.tutorial4.service.FlightService;
import com.apap.tutorial4.service.PilotService;


@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
	private String add (@PathVariable (value = "licenseNumber") String licenseNumber, Model model) {
		FlightModel flight = new FlightModel();
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		flight.setPilot(pilot);
		
		model.addAttribute("flight", flight);
		return "addFlight";
	}
	
	@RequestMapping (value = "/flight/add", method = RequestMethod.POST)
	private String addFlightSubmit (@ModelAttribute FlightModel flight) {
		flightService.addFlight(flight);
		return "add";
	}
	
	@RequestMapping (value = "/flight/delete/{id}")
	private String deleteFlight (@PathVariable (value = "id") long id) {
		flightService.deleteFlight(id);
		return "deleteFlight";
	}
	
	@RequestMapping (value = "/flight/update/{id}", method = RequestMethod.GET)
	private String updateFlight (@PathVariable (value = "id") long id, Model model) {
		FlightModel currentFlight = flightService.getFlight(id);
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(currentFlight.getPilot().getLicenseNumber());
		currentFlight.setPilot(pilot);
		model.addAttribute("currentFlight", currentFlight);
		return "updateFlight";
	}
	
	@RequestMapping (value = "/flight/update", method = RequestMethod.POST)
	private String updateFlightSubmit (@ModelAttribute FlightModel currentflight) {		
		flightService.updateFlight(currentflight, currentflight.getId());
		return "suksesUpdateFlight";	
	}
	
	@RequestMapping (value = "/flight/view")
	private String viewFlight (@RequestParam ("flightNumber") String flightNumber, Model model) {
		List <FlightModel> Flights = new ArrayList();
		List <FlightModel> allFlights = flightService.selectAll();
		
		for (FlightModel fli: allFlights) {
			if (fli.getFlightNumber().equals(flightNumber)) {
				Flights.add(fli);
			}
		}
		if (Flights.size() == 0){
			return "error";
		}
		model.addAttribute("flightNumber", flightNumber);
		model.addAttribute("flights", Flights);
		return "viewFlight";		
	}
}