package com.mycodebase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

@Path("/service")
public class EmailOperationsRestService {

	DatabaseService ds = new DatabaseService();

	@GET
	@Path("/signup")
	public Response signUp(User user)
			throws FileNotFoundException, IOException {

		boolean decision = ds.checkUserExistsAndSignUp(user.getEmailAddress, user.getFirstName, user.getLastName, user.getPassword);
		if (decision == false) {
			return Response.ok().status(200).entity("ALLOWED").build();
		} else {
			return Response.ok().status(400).entity("ALREADY EXISTS").build();
		}
	}
	
	@GET
	@Path("/getFirstName")
	public Response getFirstName(@HeaderParam("emailAddress") String inputEmail)
			throws FileNotFoundException, IOException {

			String firstName = ds.getFirstName(inputEmail);
			return Response.ok().status(200).entity(firstName).build();
	}
	
	

	@GET
	@Path("/auth")
	public Response authenticateUser(User user) throws FileNotFoundException, IOException {
		Response resp = null;
		File file = new File("/media/loginpass.txt");

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			for (String line; (line = br.readLine()) != null;) {
				String[] emailAndPass = line.split(",");
				if ((user.getEmailAddress).equals(emailAndPass[0]) && (user.getPassword).equals(emailAndPass[1])) {
					return resp.ok().status(200).entity("WORKED").build();
				}
			}
		}
		return resp.ok().status(401).entity("BAD CREDENTIALS").build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/sendMail")
	public Response sendEmail(@HeaderParam("loginUser") String loginUser, @HeaderParam("to") String to,
			@HeaderParam("header") String header, @HeaderParam("message") String message)
			throws FileNotFoundException, IOException, SQLException {
		ds.postToDB(loginUser, to, header, message);
		return Response.status(200).entity("OK").build();

	}

	@GET
	@Path("/getMail")
	public Response getEmailList(@HeaderParam("emailAddressForm") String email)
			throws FileNotFoundException, IOException, SQLException, ParseException {

		List<Email> listOfEmails = (ArrayList<Email>) ds.retrieveListOfEmails(email);
		GenericEntity<List<Email>> entity = new GenericEntity<List<Email>>(listOfEmails) {
		};
		Gson gson = new Gson();
		return Response.ok(gson.toJson(listOfEmails)).build();

	}
	@DELETE
	@Path("/deleteMail")
	public Response deleteMail(@HeaderParam("uID") String id)
			throws FileNotFoundException, IOException, SQLException, ParseException {

		ds.deleteEmail(id);
		return Response.status(200).build();

	}
	
	@GET
	@Path("/changeReadStatus")
	public Response changeReadStatus(@HeaderParam("uID") String id)
			throws FileNotFoundException, IOException, SQLException, ParseException {

		ds.changeReadStatus(id);
		return Response.status(200).entity("Read Status Changed").build();

	
	}
}