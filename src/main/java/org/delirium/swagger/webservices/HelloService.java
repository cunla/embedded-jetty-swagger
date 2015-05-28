package org.delirium.swagger.webservices;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by morand3 on 5/28/2015.
 */
@Path("/rest")
@Api(value = "/rest", description = "Hello Web Service")
public class HelloService {

	@GET
	@Path("/hello")
	@ApiOperation(value = "Hello world", notes = "Returns hello world or name", response = String.class)
	@Produces(MediaType.TEXT_PLAIN)
	public Response helloWorld(@QueryParam("name") String name) {
		String res = "Hello " + ((null != name) ? name : "world");

		return Response.ok().entity(res).build();
	}

	@GET
	@Path("/calc/{calc}")
	@ApiOperation(value = "Calculate", notes = "Calculate String", response = String.class)
	@Produces(MediaType.TEXT_PLAIN)
	public Response calc(@PathParam("calc") String calc) {
		String res = String.format("length of calc: %d", calc.length());

		return Response.ok().entity(res).build();
	}
}
