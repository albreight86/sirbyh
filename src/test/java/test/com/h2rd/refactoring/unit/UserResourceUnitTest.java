package test.com.h2rd.refactoring.unit;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.h2rd.refactoring.usermanagement.User;
import com.h2rd.refactoring.usermanagement.UserDao;
import com.h2rd.refactoring.web.UserResource;

import junit.framework.Assert;


public class UserResourceUnitTest {

    UserResource userResource;
    @Mock UserDao userDao;
    
    @Before
    public void setup()
    {
       MockitoAnnotations.initMocks(this);
        userResource = new UserResource();
        userResource.setUserDao(userDao);;
    }

    @Test
    public void getUsersTest() {
        
        User user = new User("fake user", "fake@user.com", new ArrayList<String>());
        List<User> users = new ArrayList<>();
        users.add(user);

       when(userDao.saveUser(isA(User.class))).thenReturn(user);
       when(userDao.getUsers()).thenReturn(users);

        userResource.addUser("fake user",  "fake@user.com", new ArrayList<String>());
        Response response = userResource.getUsers();
        
        verify(userDao).getUsers();

        Assert.assertEquals(200, response.getStatus());
        List<User> returnedUsers =  ((GenericEntity<List<User>>)response.getEntity()).getEntity();

        Assert.assertEquals(1, returnedUsers.size());
        Assert.assertEquals(user, returnedUsers.get(0));
    }
}
