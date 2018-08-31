package com.h2rd.refactoring.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.UserDao;

@Path("/users")
@Service("userResource")
public class UserResource
{
   private UserDao userDao;

   @Resource
   public void setUserDao(UserDao dao)
   {
      this.userDao = dao;
   }

   @PUT
   @Path("/add")
   public Response addUser(@QueryParam("name") String name, @QueryParam("email") String email,
         @QueryParam("role") List<String> roles)
   {
      if(roles.isEmpty())
      {
          return Response.status(400).build();
      }

      if (name == null)
      {
         name = User.NO_NAME;
      }
      

      User user = new User(name, email, roles);
      System.out.println("USER: " +user);
      User userCreated = userDao.saveUser(user);

      if(userCreated == User.NULL_USER)
      {
          // user is already there
          return Response.ok().build();
      }

      return Response.status(201).build();
   }

   @PUT
   @Path("/update")
   public Response updateUser(@QueryParam("name") String name, 
                                              @QueryParam("email") String email,
                                              @QueryParam("role") List<String> roles)
   {
      if(roles.isEmpty())
      {
          return Response.status(400).build();
      }

      User user = new User(name, email, roles);
      User updatedUser = userDao.updateUser(user);

      if (updatedUser == User.NULL_USER)
      {
         return Response.status(404).build();
      }

      return Response.ok().build();
   }

   @DELETE
   @Path("/delete")
   public Response deleteUser(@QueryParam("name") String name, @QueryParam("email") String email,
         @QueryParam("role") List<String> roles)
   {
      User user = new User(name, email, roles);

      User returnedUser = userDao.deleteUser(user);

      if (returnedUser == User.NULL_USER)
      {
         return Response.status(404).build();
      }
      return Response.ok().build();
   }

   @GET
   @Path("/find")
   @Produces({MediaType.APPLICATION_JSON})
   public Response getUsers()
   {
      List<User> users = userDao.getUsers();
      if (users == null)
      {
         users = new ArrayList<User>();
      }

      GenericEntity<List<User>> usersEntity = new GenericEntity<List<User>>(users) {};

      return Response.status(200).entity(usersEntity).build();
   }

   @GET
   @Path("/search")
   @Produces({MediaType.APPLICATION_JSON})
   public Response findUser(@QueryParam("name") String name)
   {
      if (name == null)
      {
         name = User.NO_NAME;
      }

      List<User> users = userDao.findUser(name);
     
      if (users.size() == 0)
      {
         return Response.status(404).build();
      }

      GenericEntity<List<User>> usersEntity = new GenericEntity<List<User>>(users) { };

      return Response.ok().entity(usersEntity).build();
   }
}
