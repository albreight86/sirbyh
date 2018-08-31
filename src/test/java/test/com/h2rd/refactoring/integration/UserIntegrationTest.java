package test.com.h2rd.refactoring.integration;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.UserDao;
import com.h2rd.refactoring.web.UserResource;

import junit.framework.Assert;

public class UserIntegrationTest
{
   final static String USER_NAME = "user name";
   final static String USER_EMAIL = "user@email.com";

   final static String UPDATE_USER_NAME = "update user name";

   final static String ROLE_ADMIN = "admin";
   final static String ROLE_MASTER = "master";
   final List<String> roles = new ArrayList<>();

   private User user;
   private User updateUser;
   private UserResource userResource;

   @Before
   public void setup()
   {
      userResource = new UserResource();
      userResource.setUserDao(new UserDao());
      roles.add(ROLE_ADMIN);
      roles.add(ROLE_MASTER);
      user = new User(USER_NAME, USER_EMAIL, roles);
      updateUser = new User(UPDATE_USER_NAME, USER_EMAIL, roles);
   }

   @After
   public void cleanup()
   {
      Response response = userResource.getUsers();

      GenericEntity<List<User>> ge = (GenericEntity<List<User>>) response.getEntity();
      List<User> users = ge.getEntity();

      for (User user : users)
      {
         userResource.deleteUser(user.getName(), user.getEmail(), user.getRoles());
      }
   }

   @Test
   public void createUserTest()
   {

      Response response = userResource.addUser(user.getName(), user.getEmail(), user.getRoles());
      Assert.assertEquals(201, response.getStatus());

      response = userResource.getUsers();

      List<User> users = ((GenericEntity<List<User>>) response.getEntity()).getEntity();

      Assert.assertTrue(users.contains(user));
   }

   @Test
   public void deleteUserTest()
   {

      Response response = userResource.addUser(user.getName(), user.getEmail(), user.getRoles());

      response = userResource.deleteUser(user.getName(), user.getEmail(), user.getRoles());
      Assert.assertEquals(200, response.getStatus());
   }

   @Test
   public void updateExistingUserTest()
   {

      userResource.addUser(user.getName(), user.getEmail(), user.getRoles());

      Response response = userResource.updateUser(updateUser.getName(), updateUser.getEmail(), updateUser.getRoles());
      Assert.assertEquals(200, response.getStatus());
   }

   @Test
   public void updateNonExistingUserTest()
   {

      Response response = userResource.updateUser(updateUser.getName(), updateUser.getEmail(), updateUser.getRoles());
      Assert.assertEquals(404, response.getStatus());
   }

   @Test
   public void getUsers()
   {

      userResource.addUser(user.getName(), user.getEmail(), user.getRoles());

      Response response = userResource.getUsers();
      Assert.assertEquals(200, response.getStatus());

      List<User> users = ((GenericEntity<List<User>>) response.getEntity()).getEntity();
      Assert.assertEquals(1, users.size());
   }

   @Test
   public void getUsersWithNoUsers()
   {

      Response response = userResource.getUsers();
      Assert.assertEquals(200, response.getStatus());

      List<User> users = ((GenericEntity<List<User>>) response.getEntity()).getEntity();
      Assert.assertEquals(0, users.size());
   }

   @Test
   public void findUser()
   {

      userResource.addUser(user.getName(), user.getEmail(), user.getRoles());
      userResource.addUser(1 + user.getName(), 1 + user.getEmail(), user.getRoles());
      userResource.addUser(2 + user.getName(), 2 + user.getEmail(), user.getRoles());
      userResource.addUser(3 + user.getName(), 3 + user.getEmail(), user.getRoles());
      userResource.addUser(" ", 4 + user.getEmail(), user.getRoles());

      Response response = userResource.findUser(2 + user.getName());
      Assert.assertEquals(200, response.getStatus());

      List<User> users = ((GenericEntity<List<User>>) response.getEntity()).getEntity();
      Assert.assertEquals(new User(2 + user.getName(), 2 + user.getEmail(), user.getRoles()), users.get(0));
   }

   @Test
   public void findUserNotThere()
   {

      userResource.addUser(user.getName(), user.getEmail(), user.getRoles());
      userResource.addUser(1 + user.getName(), 1 + user.getEmail(), user.getRoles());
      userResource.addUser(2 + user.getName(), 2 + user.getEmail(), user.getRoles());
      userResource.addUser(3 + user.getName(), 3 + user.getEmail(), user.getRoles());
      userResource.addUser(" ", 4 + user.getEmail(), user.getRoles());

      Response response = userResource.findUser(5 + user.getName());
      Assert.assertEquals(404, response.getStatus());
   }

   @Test
   public void findUserWithNoName()
   {

      userResource.addUser(user.getName(), user.getEmail(), user.getRoles());
      userResource.addUser(1 + user.getName(), 1 + user.getEmail(), user.getRoles());
      userResource.addUser(2 + user.getName(), 2 + user.getEmail(), user.getRoles());
      userResource.addUser(3 + user.getName(), 3 + user.getEmail(), user.getRoles());
      userResource.addUser(" ", 4 + user.getEmail(), user.getRoles());
      userResource.addUser(null, 5 + user.getEmail(), user.getRoles());

      Response response = userResource.findUser(null);
      Assert.assertEquals(200, response.getStatus());

      List<User> users = ((GenericEntity<List<User>>) response.getEntity()).getEntity();
      Assert.assertEquals(new User(User.NO_NAME, 5 + user.getEmail(), user.getRoles()), users.get(0));
   }
}
