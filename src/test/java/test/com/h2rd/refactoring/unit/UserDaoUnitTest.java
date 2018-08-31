package test.com.h2rd.refactoring.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.UserDao;

import junit.framework.Assert;

public class UserDaoUnitTest
{
   final static String FAKE_NAME = "Fake Name";
   final static String FAKE_EMAIL = "fake@email.com";
   final static String ROLE_ADMIN = "admin";
   final static String ROLE_MASTER = "master";
   final static String ROLE_SECURITY = "security";
   final List<String> roles = new ArrayList<>();

   private UserDao userDao;

   @Before
   public void setup()
   {
      userDao = new UserDao();
      roles.add(ROLE_ADMIN);
      roles.add(ROLE_MASTER);
   }

   @Test
   public void saveAndGetUserTest()
   {
      User user = new User(FAKE_NAME, FAKE_EMAIL, roles);

      userDao.saveUser(user);

      if (!userDao.getUsers().contains(user))
         Assert.fail();
   }

   @Test
   public void deleteUserExistsTest()
   {

      User user = new User(FAKE_NAME, FAKE_EMAIL, roles);

      userDao.saveUser(user);

      User deletedUser = userDao.deleteUser(user);
      Assert.assertEquals(user, deletedUser);
   }

   @Test
   public void updateAndFindUserTest()
   {

      List<String> newRoles = new ArrayList<>();
      newRoles.add(ROLE_SECURITY);

      User user = new User(FAKE_NAME, FAKE_EMAIL, roles);

      userDao.saveUser(user);

      User updateUser = new User(1 + FAKE_NAME, FAKE_EMAIL, newRoles);

      userDao.updateUser(updateUser);

      List<User> users = userDao.findUser(1 + FAKE_NAME);
      Assert.assertEquals(updateUser, users.get(0));
   }

   @Test
   public void updateNonExistingUserTest()
   {
      List<String> newRoles = new ArrayList<>();
      newRoles.add(ROLE_SECURITY);

      User user = new User(1 + FAKE_NAME, FAKE_EMAIL, newRoles);

      User updatedUser = userDao.updateUser(user);

      Assert.assertEquals(updatedUser, User.NULL_USER);
   }

   @Test
   public void getUsersWithNoUsersTest()
   {
      List<User> users = userDao.getUsers();

      Assert.assertEquals(0, users.size());
   }

   @Test
   public void findUserNotThereTest()
   {
      List<User> users = userDao.findUser(FAKE_NAME);

      Assert.assertEquals(0, users.size());
   }
}