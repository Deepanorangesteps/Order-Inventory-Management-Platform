1.SPRING BOOT ACTUATOR
Exposes endpoints to monitor and manage your application 
You easily getDevops functionality 
  1. HEALTH ENDPOINT 
  /health check status of your application 
  Normally used by monitoring apps to see if your app is up or down 
  /info end point provide information about our applicaiton 
  configure in the application.properties file 
  #management.endpoints.web.exposure.include= health,info -- for sepcific
management.endpoints.web.exposure.include= * this expose all endpoints 
management.info.env.enabled= true

info.app.name = deepanApp
info.app.version = 1.0.1
some of the endpoints below 
http://localhost:8080/actuator/info  -- application information 
http://localhost:8080/actuator/beans -- list of beans used in applicaiton 
http://localhost:8080/actuator/threaddump =  thread list 
http://localhost:8080/actuator/mappings

----------------------------------------------------------
HOW TO RUN SPRING USING THE JAR FILE 
1.compie jar using this command 
mvnw package go to the folder and use this it automatically convert into jar file
then run java -jar 

---------------------------------------------
CREATE CUSTOM PROPERTY AND FETCH IN THE CLASS
1. decalre custom property in application.properties file 

#DEFINE A CUSOTM PROPERTY 
coach.name = Micky mouse
team.name = The mouse club

@Value("${coach.name}")
	private String coachName;
	
	@Value("${team.name}")
	private String teamName;
	
	CHATGPT DEFINITION
	@Value is used to inject values into Spring-managed beans. These values can come from application.properties,
-----------------------------------
CONFIGURE THE APPLICATION.PROPERTIES FILE 

we configure
db url ,log ,log file , server, custom properties , security 
for example 

server.servlet.context-path=/deepan  this is prefix of the URL 
http://localhost:8080/deepan/team
---------------------------------------------
INVERSION OF CONTROL
for more info refer note 
IOC DEFINITION 
The approach of outsourching the construction and management of object

Basically app should be configurable 
Easily change the coach for another sport 
Object factory is spring container based on configuration 
it provides an object 
CONFIGURATION IS BASED On 
xml
java scource ccode
java annotation 
CHATGPT DEFINITION :
IoC is a principle where the Spring container controls object creation and lifecycle,
 and Dependency Injection is the mechanism used to implement IoC.
----------------------------------------------
DEPENDENCY INJECTION 

The dependency inversion principle 
The clinet delegates to another project the responsibility of providing
its dependency

FOR AN EXAMPLE 
MYAPP  ------>  give me a coach object -->|spring object factory|
                    based on config it provide all helper objects like Cricketcoach has another dependency like helper coach 
					
					IN CODE 
					Coach will provide daily workouts
					The Democontroller want to use a coach 
					New helper coach 
					This is a dependency
					Need to inject this dependency
					
					CHATGPT DEFINITION
					Instead of an object creating its dependencies itself, the Spring container injects 
					the required dependencies,
					typically through constructor injection, setter injection, or field injection.
INJECTION TYPE 
1. constructor injection 					
  use this when you have required dependency
  Easier unit testing.
Makes dependencies explicit.
Supports final fields and immutability.
Prevents object creation without required dependencies.
Recommended by the Spring team and widely used in production applications.
2. Setter injection
use this when you have optional dependency  
WHAT IS AUTOWIRING 
For dependency injection , spring use autowiring 
Spring will look for a class that matches 
match my type : class or interface
Spring will inject automatically hence it autowired 
Spring will scan for @Component
Any one implement coach intercace with componenet scan it wil automatically inject 
-------------------
@AUTOWIRED 
@Autowired is used for automatic dependency injection in Spring
By default, Spring resolves dependencies by type and injects the matching bean from the IoC container.
It can be used on fields, setters, and constructors

--------------------------
@COMPONENT ANNOTATION 
@COMPONENT marks the class as a spring bean 
A spring bean is just a regular class that is mananged by spring
@COMPONENT also makes the bean available for dependency injection

BEHIND THE SCENE OF CONSTRUCTOR INJECTION 
Coach theCoach = new Cricketcoach();
DemoController demoController =  new Democontroller(theCoach);
here theCoach is the dependency or a helper class 
-------
@Component, @Service, @Repository, @Controller
All four are stereotype annotations and are detected during component scanning. Spring creates beans for them and manages
@Service, @Repository, and @Controller are specialized versions of @Component
1.@Service
@Service is used for service-layer classes that contain business logic.
 Although it is internally a specialized form of @Component, 
 it improves code readability and clearly indicates that 
 the class belongs to the business layer.
 2. @Repository
Used in the persistence/DAO layer.
@Repository marks a class as a data access component.
3.@Controller
@Controller is used in the presentation layer of a Spring MVC application.
 It receives HTTP requests, delegates processing to the service layer, and returns a view name.
--------------------------
@SpringBootApplication // here this annotation enable the @EnableAutocinfiguration,@CompnenetScan,@Configuration
public class SpringcoredemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcoredemoApplication.class, args);
	}
}

@SpringBootApplication is composed of the follwoing annotation
@EnableAutoconfiguration
Enable spring auto configuration support
@ComponentScan
Enables component scanning of current package
also recursively scans sub packages
@Configuration 
Able to register extra beans with @Bean or import other config class

		SpringApplication.run(SpringcoredemoApplication.class, args);
the able line bootstrap your spring boot application create application 
context and register all bean then start the embeded tomcat etc 

CHATGPT DEFINITION
Auto Configuration is a Spring Boot feature that automatically configures beans and 
application settings based on the dependencies available in the classpath,
 reducing the need for manual configuration.
------------------------------------------
MORE ON COMPONENT SCAN 
by default spring boot start component scanning 
From same package as your maing spring boot application 
also scans sub package 
DRAWBACK 
this not scan the other packages 
so here we want to explicity list the base package to scan 
for exaple in the main class
@SpringBootApplication
		(scanBasePackages = {
				"com.luv2code.springcodedemo",
				"com.luv2code.utils"
		})
public class SpringcoredemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringcoredemoApplication.class, args);
	}
}
---------------------------------------
SETTER INJECTION 
Inject dependencies by calling setter method on your class 

INJECTION TYPE WHICH ONE TO USE ?
CONSTRUCTOR INJECTION
Use this when you have require dependencies
Generally recommended by spring.io
SETTER INJECTION 
Use this when you have optional dependencies
If dependency is not provided your app can provide reasonable default logic 
------------------------
FIELD INJECTION 
In earlier days was popular on spring projects
in recently it has fallen out of favour

In general it makes the code harder to unit test
As a result the spring.io not recommended field injection 
---------------------------
@QUALIFIER
if we have one interface that implemented by many classes 
during constructor injection which one want to inject confused 
FOR EXAMPLE 
inteface Coach {} class Cricketcoach implement Coach{} class TennisCoach implement Coach{}
to fix this we want to use @QUALIFIER to specify which class want to inject for that coach 

CHATGPT DEFINITION:
When multiple beans of the same type exist, 
Spring cannot decide which bean to inject and throws a NoUniqueBeanDefinitionException.
@Qualifier is used to specify which bean should be injected.

------------------------------
@PRIMARY 
In case of multiple Coach implementation 
we resolve it using @QUALIFIER we sepcified a coach by name 

Instead of specifying a coach by name using @QUALIFIER
I simply need a coach I don't care which coach 
If there are multiple coach 
Then you coaches figure it out and tell me who's the PRIMARY coach then inject it automatically
@Component
@Primary
public class TennisCoach implements Coach{}
@PRIMARY - ONLY ONE 
 we cannot mark multiple class @PRIMARY if we do we face an error ,unable to run the application
 IN CASE WE SET PRIMARY AND QUALIFIER BOTH AT SAME TIME 
 @QUALIFIER is the higher priority , because it is sepcific
 
 CHATGPT DEFINITION:
 @Primary is used when multiple beans of the same type are available in the Spring container. 
 During dependency injection, if no specific bean is mentioned using @Qualifier, 
 Spring will inject the bean marked with @Primary by default.
 ----------------------------
 LAZY INITIALIZATION
 @LAZY 
 By default when you application starts all beans are initialized
 @Component, ETC ...
 spring will create an instance of each and make them available 
LAZY INITIALIZATION
Insetad of creating all beans up front, we specify lazy initialization
A bean will only be initialized in the following cases 
It is needed for dependency injection 
or it is explicity requested
To configure add @Lazy to each class 

If you want to set global configuration property instead of put @Lazy in all required class
we config in application.prop file spring.main.lazy-initialization = true be careful to use this global config

ADVANTAGES
Only create object need 
May help with faster startup time if you have large number of components
DISADVANTAGES
If you have web related components like @Restcontroller , not created until requested
May not discover configuration issues until too late
Need to make sure you have enough memory for all beans once created 
(INitially Lazy feature is disabled by default )
------------------------------
BEAN SCOPE 
scope refer to the lifecycle of a bean 
How long does the bean live ?
How many instances are created ? , How is the bean shared ?
DEFAULT BEAN SCOPE IS SINGLETON 
WHAT IS SINGLETON 
spring container creates only one instance of the bean by default
It cached in memory 
All dependency injections for the bean -> will refer same bean

ADDITIONAL SPRING BEAN SCOPE 
Singleton 
create a single shared instance of bean Default scope 
PROTOTYPE 
Creates a new bean instance for each container request
REQUEST,SESSOON , GLOBAL SESSION
scoped to HTTP web request only used for web apps 
NOW WE SEE THE PROTOTYPE AND SINGLETON SCOPE 
example 
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrackCoach implements Coach{}
   private Coach myCoach;
    private Coach anotherCoach;
    @Autowired
    public DemoController(@Qualifier("trackCoach") Coach theCoach,
                          @Qualifier("trackCoach") Coach theAnotherCoach){
        System.out.println("In constructor : "+getClass().getSimpleName());

        myCoach = theCoach;
        anotherCoach = theAnotherCoach;
    } 
	  @GetMapping("/check")
    public String check(){
        return "Comparing bean myCoach == anotherCoach, "+(myCoach == anotherCoach);
    }// here if singleton means true if prototype means false becuase creating a new object every time 
	-------------------------------------------------
	BEAN LIFE CYCLE METHODS - ANNOTATIONS 
	
	Container statret -> Bean instantiated -> Dependicies injected -> Internal Spring procesing -> your custom init METHOD
	
	               Bean is ready for use
				   Container is shutdown 
				        |
	                Your custom destroy method 
	BEAN LIFE CYCLE METHODS / HOOKS 
You can add custom code during BEAN INITIALIZATION
calling custom business logic methods 
setting up handles resources 
You can add custom code durinng BEAN DESTRUCTION 

calling custome business logic 
clean up handles to resources 	

INIT: METHOD CONFIGURAITON 
  @PostConstruct
    public void doMyStartupStuff(){
        System.out.println("In doMyStartupStuff() : "+getClass().getSimpleName());
    }

DESTROY METHOD CONFIGURATION 
 @PreDestroy
    public void doMyCleanupStuff(){
        System.out.println("In doMyCleanupStuff() : "+getClass().getSimpleName());
    }
	CHATGPT DEFINITION
	The Bean Lifecycle describes the stages a Spring bean goes through from creation to destruction.
	Spring Container Starts->Bean Instantiation->Dependency Injection
	->Spring executes postProcessBeforeInitialization() methods.
	-> Initialization
Initialization callbacks are executed:
@PostConstruct
InitializingBean.afterPropertiesSet()
Custom init-method
Bean Ready for Use
The bean is fully initialized and available for application use.
8. Bean Destruction (When Application Shuts Down)
-----------------------------------
CONFIGURING A BEAN IN JAVA CLASS 
here 
create a @Configuration class 
define @Bean method to configure the bean 
Inject the bean into our controller 

WHY WE WANT TO USE THIS 
Make an existing third party class available to spring framework
You may not have access to source code of third party class
However you would like to use the third party class as a spring bean like jar file classes 

so see create a class configuration 
@Configuration
public class SportsConfig {
    @Bean("swim") // give a name of the bean as swim 
    public Coach swimCoach(){  // create a method to return object bean
        return  new SwimCoach();
    }
}
// see here no annotation in SwimCoach just normal class 
public class SwimCoach implements Coach{
    @Override
    public String getDailyWorkout() {
        return "Swim 500 meters as a warm up";
    }
}
so in controller we call using the qualifier or bean name as we given above and inject it 
 @Autowired
    public DemoController(@Qualifier("swim") Coach theCoach,
	
	CHATGPT DEFINITION
	@Bean is used on a method inside a @Configuration class.
	The object returned by the method is registered as a Spring bean and managed by the IoC container
	-separate definition for bean and configuration 
	What is @Configuration?
@Configuration tells Spring:
This class contains bean definitions, and Spring should process it to create objects 
and manage them in the IoC Container.
What is @Bean?
@Bean tells Spring:
The object returned by this method should be registered as a Spring Bean.
	
	----------------------------------------
	SPRING BOOT TOPICS 
	1.spring boot actuator 
	2.@Value
	3.IOC
	4. DEPENDENCY injection
	5. @Component
	6. @SpringApplication and springboot.run 
	7. @QUALIFIER
	8. @PRIMARY
	9. LAZY initialization
	10. Bean scope
	11. Bean lifecycle
	-------------------------------------------------------------
	REST API 
	Json 
	java script object notation 
	Light weight data format for storing and exchanging data 
	Languate independent not just for java script
	can use with any programing languate :java C#,python 
	---------------------
	REST OVER HTTP 
	Most common use of Rest is over HTTP 
	Leverage HTTP methods for CRUD Operations 
	POST -> create a new entity 
	GET -> Read a list of entities or single entity
	PUT -> Update an existing entity 
	DELETE -> Delete an existing entity 
	
	HTTP Request Message
	-------
	Request Line : the HTTP command -like post put 
	Header Variables :request metadata
	Message body : contents of message-  actual payload
	
	HTTP RESPONSE Message
	Response line : server protocal and status code 
	Header variables : response metadata - > response like xml or json 
	Message body content of message
	
	HTTP RESPONSE -STATUS CODE 
	100 - 199 informational 
	200 -299 - successful
	300-399 - Redirection
	400-499 - Client side error 
	500-599 Server side error 
------------------------------
Java JSON Data binding 	
Data binding is the process of converting JSON data to a java POJO 
also know as Mapping , serialization/Deserialization 
Marshalling/Unmarshalling 

JSON DATA Binding with Jackson
spring uses the jackson Project behind the scenes
Jackson handles data binding between JSON and java POJO
By default, Jackson will call appropriate getter/setter method 	
**JSON TO JAVA POJO 
Convert JSON to JAVA POJO call setter method on POJO
Note:jackson call the setxxx methods it does not access internal private fields directly 	
Convert Java POJO to Json call getter methods on POJO 
SPRING AND JACKSON SUPPORT 
When building spring REST application 
Spring will automatically handle Jackson Integration 
JSON data being passed to REST controller is converted to POJO 
Java object returned from REST controller is converted to JSON

SPRING BOOT WEB automatically includes the dependency for jackson

BEHIND SCENE OF WORKING 
              api/students 
REST       --------------------->|SPRING REST  | --------> REST SERVICE 
                    JSON             JACKSON      
CLIENT             -----        |            |  <--------
                                 -------  
-------------------------------------------------
@PATHVARIABLE 
/api/studnets/{studentId}--> known as path variables 
 used to bind the path variables to method parameter using @PATHVARIABLE annotation 
 
 OTHER HTTP RELATED ANNOTATIONS 
@RequestMapping is used to map URLs to controller methods.
 It can be applied at both class and method levels and supports HTTP methods 
 such as GET, POST, PUT, DELETE, and PATCH.
 @RequestParam is used to read query parameters from the URL.
 example :
 GET /employees?page=1&size=10
 @GetMapping("/employees")
public List<Employee> getEmployees(
        @RequestParam int page,
        @RequestParam int size) {}
		
		@RequestBody is used to bind the request payload (typically JSON or XML) to a Java object.
ResponseEntity represents the complete HTTP response including body, status code, and headers.
 It provides greater control over API responses.
@Valid 
Validation ensures incoming data is correct before processing.
used on the request or entity  class 
@NotNull,@NotBlank max,min,size,email 
public Employee createEmployee(
        @Valid @RequestBody EmployeeRequest request) {
 
 -----------------------
 RESPONSE ENTITY 
  RESPONSE ENTITY is a wrapper for the HTTP response object
  Response entity provides fine-grained control to specify :
  HTTP status code,HTTP headers and Response body
  ----------------------------
  @CONTROLLERADVICE
  @ControllerAdvice is similar to an interceptor/ filter
  Pre-process requests to controllers
  Post-process responses to handle exceptions
  Perfect for global exception handling 
               
			   api/studnet/999
  rest client -----------------> controller advide--- Rest service
                                 Exception handler
  CHATGPT DEFINITION
  @ExceptionHandler is used to handle specific exceptions and return custom error responses. 
  @ControllerAdvice is used to centralize exception handling across the entire application. 
  When an exception is thrown from a controller, Spring automatically finds the matching 
  @ExceptionHandler method inside the @ControllerAdvice class and executes it.
  ---------------------------------------
  REST API URL desing and rules 
  
  POST /api/employees
  GET /api/employees
  GET /api/employees/{employeeId}
  put /api/employees
  DELETE /api/employees/{employeeId}
  
  DO NOT DO THIS /api/employeeList,/api/deleteEmployee 
 ---------------------------------------------------
SPRING DATA JPA -SOLUTION  
spring data JPA provided the interface : JPA Repository 
Exposes methods (some by inheritance from parents)
it exposes methods like findAll() findById()

---------------------------------------------
SERVICE LAYER PURPOSE 
Service Facade design pattern
Intermediate layer for custom business logic
Integrate data from multiple sources (DAO/repositories)

for example 
Integrated multiple data sources 
employee rest controller<----> employee service  employeeDAO,skillsDAO
provide controller with a single view of the data that we
integrated from multiple backend datasourses 

@SERVICE ANNOTATION 
@SERVICE applied to Service implementation like EmployeeServiceImpl
Spring will automatically register the service implementation 
  
IF we add service layer 
best practice to apply transactional boundries 
It is the service layer's responsibility to manage transaction boundries
For implementation code
in previous we add @Transactional annotation to manage the transaction
Now this service will handle 

--------------------------------------
SPRING SECURITY MODEL
Spring Security defines a framework for security 
Implemented using Servlet filter in the background
Two methods of securing an app: declarative and programmatic

SPRING SECURITY WITH SECURITY FILTERS
Servlet filters are used to pre-process/post-process web requests
Servlet Filters can route web requests based on security logic
Spring rovides a bulk of security functionality with servlet filter

NOW THE PROCESS IS 
Web Browser -> spring security filter -> protected web resource
SECURITY CONCEPTS
Authentication 
Check id and password with credeintials stored in app/db
Authorization
Check to see if user has an authorizes role 

DATABASE SUPPORT IN SPRING SECURITY 
Spring security can read user account info from database
By default, you have to follow spring security's predefined table schemas

Spring security recommends using the popular bcrypt algorithm

BCRYPT
Performs one way encrypted hashing
Adds a random salt to the password for additional protection 
Include support to defeat brute force attacks
Spring Security Login process
1. Retrieve password from db for the user
2. Read the encoding algorithm id (bcrypt etc}
3. For cas of brypt, encrypt plain text password from login form(using salt from db password)
4. compare encrypted password from login form 	WITH encrypted password from db
5. If there is am match, login successful
6. If no match, login NOT successful 
--    ---       ---                     ---
SPRING SECURITY EXPLANATION CHATGPT

--------------------------------------------------------------------------
                                    CHATGPT TOPICS 
--------------------------------------------------------------------------
1. Starter Dependencies?
Starter dependencies are pre-packaged dependency bundles provided by Spring Boot. 
They simplify project setup by including all required libraries for a particular 
feature and ensuring compatible versions. For example, spring-boot-starter-web includes Spring MVC,
 embedded Tomcat, and Jackson, allowing developers to build web applications with minimal configuration.
