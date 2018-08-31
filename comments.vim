General
	1.The request in README.markdown to avoid performing a (git) pull:
      I believe that should be to avoid doing either a pull or a push, although it is not 
      clear why a pull would matter.
	3. The email address for a user must be unique, but the user's name is not specified as such.
	   Therefore the user name does not need to be provided, but a string representing that no name
	   was provided is put into the db. 
	4. If:  
	      an update is requested for a user that does not exist, 404 is returned.
	      a  delete is requested for a user that does not exist, 404 is returned.
	      a search is requested, a List is returned since the user name field is not required to be
	          unique.
	5. to build: mvn clean install dependency:copy-dependencies 
	6. to run in a jvm: java -cp target/dependency/*:target/java-refactoring-excercise-0.0.1-SNAPSHOT.jar com.h2rd.refactoring.usermanagement.SpringMain
	7. added spring support into classes and flushed out application-config.xml
    8. I chose the path of using Spring as the container. I did not deploy to Tomcat, and also 
       commented out the servlet/jsp dependencies in pom.xml.
	
pom.xml
	- added maven plugin to use java 1.8, and set plugin versions
	- added build config to generate a jar as well as a war.
	- added cxf/jackson dependencys to support webservice
	   - todo: make sure the versions are the most recent
	- did not remove the jersey dependency but expected to; removing it caused these lines in UserIntegrationTest.java
	  to through a class cast
	  exception:  List<User> users =  ((GenericEntity<List<User>>)response.getEntity()).getEntity(); This one is a head scratcher:
	  mvn dependency:tree did not reveal why it is needed.
User.java
    - make instance variables private.
    - make the User object immutable once created; 
    - add equals() and hashcode().
UserResource.java
	- use @PUT for add/update as these are idempotent actions.
	- getUser uses an incompatible way from the other methods to initialize the member: userDao. Both will use the 
	  injectiont with Spring.
	- Replaced @Respository with @Service: increase clarity.
	- fixed @Path annotation strings.
UserDaoUnitTest.java
    - had no asserts, so tests didn't test anything.
    - added mockito and one test case to establish the mocking pattern. 
    - TODO: add more test cases, although one could argue this is redundant, as the integration test covers this.
      But for completeness, and to set the pattern of separating integration testing from unit testing, this should
      be completed.
UserDao.java
	- this needs to be a singleton. Spring does this via injection.
	- Using spring, the class annnotion is set to @Repository. It could be
	  @Component, but makes it easier to read.
	- added equals/hashcode methods to User.
	- remove the exception throwing/catching: not needed at this point.
	- deleteUser: return NULL_USER if user doesn't exist.
	- Changed <users> reference to List<User> from ArrayList<User>;
	- getUsers(): interesting bug was fixed by doing a shallow copy of <users>: the cleanup method
	  in the integrations tests would through a concurrent access exception; see comments for that class. 
	- made the "db", that is the <users> array list, synchronized. If using an rdb, the calls to the
	  db would be controlled by transactions, which are somewhat equivalent. 
    - TODO: An outage here is the situations (such as in delete), where the db is queried and then deleted from.
      These two steps should be in a transaction (or in this case a synchronized block). 
	- rather than throw exceptions, either a USER_NULL is returned or an empty list. For this code
	  that is likely good enough. If a retry mechanism is needed, they this should be re-addressed.
	- the method guards at the beginning of some of the methods are redundant but improve performance.
UserIntegrationTest.java
	- remove calls from one test to another test. Tests should not depend on other tests.
	- the @After cleanup() method was needed to guarantee the state of the db (UserDao.users) as
	  apparently, junit was running test in parallel, though this is not enabled: this is a mystery.
application-config.xml
	- used component scan to discover beans. If the project becomes
	  larger, and the startup time is an issue, then bean definitions
	  could be used to avoid the overhead incurred using reflection to
	  find the beans.
	  
Logging: support for log4j was added, though for some reason, the log4j2.xml could not be parsed.
         The runtime complaint is the first character is an EOF, but that is clearly not the case.
    - TODO: make the configuration of log4j work.
    - TODO: make log4j work in the tests as well.
Testing:
 	- all the unit tests are green.
 	- testing by hand using postman with the following urls:
 		
    PUT: http://localhost:8080/users/add?name=sam&email=sam@sam.com                  (fails: no role)
    GET: http://localhost:8080/users/find
    GET: http://localhost:8080/users/search?name=sam
    DELETE: http://localhost:8080/users/delete?name=sam&email=sam@sam.com
    PUT: http://localhost:8080/users/add?name=sam&email=samRR@sam.com&roles=admin&roles=master (fails:role param misspelled)
    PUT http://localhost:8080/users/add?name=sam&email=samR@sam.com&role=[admin,master]
    
    
Some next steps (besides the TODOs):
	- use a db in place of the list in UserDao.
	- for integration testing, this could be an in-memory db like h2.
	- write integration tests that issue simultaneous rest calls.
	- introduce transactions appropriately in UserDao.
	- Modify the UserIntegrationTest to instantiate a web server.
	- Consider introducing Groovy:
		- for the model objects
		- for the tests. This would make injecting mock behaviour easier, when new features require more complication mocking.
	-  pass the user info to a rest resource via a post/put body rather than query
	   parameters to be more practical when adding new fields in the future.
	- with this last suggestion, allow multiple users to be added/updated/deleted in one call.

-----------------trying to make a patch ---------------------
this I found somewhere to generate the patch for Hybris and it was totally bogus because I didnt have a branch defined (I think). I didnt get to test this, but hybris said very little compiled. <BS>https://www.ivankristianto.com/create-patch-files-from-multiple-commits-in-git/

git format-patch -4 --stdout > refactor.patch

Then I tried to create a branch and move the head pointer. git log was helpful, but the website that said to do this was bonkers...it wiped out my changes (I had backed them up).
https://lostechies.com/derickbailey/2010/06/08/git-d-oh-i-meant-to-create-a-new-branch-first/

To create a patch from the last 2 commits: git format-patch HEAD~~ 
Gotta love the syntax, of course there are a million ways to do this 
rmat-patch HEAD~~ 
Gotta love the syntax, of course there are a million ways to do this 
to apply the patch: git am patch.patch.

To see info on the patch, execute the following in a local that doesnt have the commits, otherwise they are garbage
git apply --stat a_file.patch
git apply --check a_file.patch






