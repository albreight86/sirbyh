package com.h2rd.refactoring.usermanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao
{
   private static final Logger log = LogManager.getLogger(User.class.getName());

   private List<User> users = Collections.synchronizedList(new ArrayList<>());

   public UserDao()
   {
   };

   public User saveUser(User user)
   {
      if (users.contains(user))
      {
         return User.NULL_USER;
      }

      if (users == null)
      {
         users = new ArrayList<User>();
      }
      users.add(user);
      return user;
   }

   public List<User> getUsers()
   {
      // shallow copy is probably OK, but know it is shallow..
      return new ArrayList<User>(users);
   }

   public User deleteUser(User userToDelete)
   {
      if (!users.contains(userToDelete))
      {
         log.warn("Attempt to delete non-existent user: {}", userToDelete);
         return User.NULL_USER;
      }

      for (User user : users)
      {
         if (user.equals(userToDelete))
         {
            users.remove(user);
            return user;
         }
      }
      return User.NULL_USER;
   }

   public User updateUser(User userToUpdate)
   {
      // using an index approach as User objects are immutable.
      int matchedUserIndex = -1;

      for (int i = 0; i < users.size(); i++)
      {
         if (users.get(i).getEmail().equals(userToUpdate.getEmail()))
         {
            matchedUserIndex = i;
         }
      }

      if (matchedUserIndex != -1)
      {
         users.set(matchedUserIndex, userToUpdate);
         return users.get(matchedUserIndex);
      }
      log.warn("Attempt to delete non-existent user: {}", userToUpdate);
      return User.NULL_USER;
   }

   // Since names aren't required to be unique, return a list
   public List<User> findUser(String name)
   {

      List<User> foundUsers = new ArrayList<>();
      for (User user : users)
      {
         if (user.getName().equals(name))
         {
            foundUsers.add(user);
         }
      }
      if (foundUsers.isEmpty())
      {
         log.warn("Attempt to find non-existent user: {}", name);
      }
      return foundUsers;
   }
}
