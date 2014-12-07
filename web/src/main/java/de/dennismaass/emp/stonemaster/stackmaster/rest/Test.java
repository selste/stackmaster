package de.dennismaass.emp.stonemaster.stackmaster.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.dennismaass.emp.stonemaster.stackmaster.common.profile.Profile;
import de.dennismaass.emp.stonemaster.stackmaster.common.properties.connection.ComConnectionProperties;

@Path("/properties")
public class Test {

	@GET
	@Path("/connection")
	@Produces(MediaType.APPLICATION_JSON)
	public Profile getProfile() {
		Profile profile = new Profile();
		ComConnectionProperties properties = new ComConnectionProperties();
		properties.setFastDownSpeed(1000);
		profile.setProperties(properties);
		return profile;
	}

	@POST
	@Path("/profile")
	@Consumes(MediaType.APPLICATION_JSON)
	public void getProfile(Profile profile) {

	}
}