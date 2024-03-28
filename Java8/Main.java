/******************************************************************************

                            Online Java Compiler.
                Code, Compile, Run and Debug java program online.
Write your code in this editor and press "Run" button to execute it.

*******************************************************************************/
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class Main
{
	public static void main(String[] args) {
		Integer a[] = {10,20,30,40,50,10,30,50,30};
		Set<Integer> listSet = new HashSet<Integer>();
		Arrays.asList(a).stream().filter((no)-> listSet.add(no)).collect(Collectors.toSet()).forEach(System.out::println);
		
		
		
		String str="Hello world Hello User world is here";
		List<String> stringList = Arrays.asList(str.split(" "));
		Map<String, Long> finalmap = stringList.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
		System.out.println(finalmap);
		
		
// 		check the number is prime or not
        int no =10;
        boolean ans = IntStream.range(2,no/2).noneMatch(n-> no%n==0);
        System.out.println(ans);
	    
	   // random number 
	   Random ram = new Random();
	   ram.ints(0,10).limit(1).forEach(System.out::println);
	   //System.out.println(no1);
	   
	   
	   // print the N prime numbers
	   Stream.iterate(2,i-> i+1).filter(Main::checkprimeNumber).limit(10).forEach(System.out::println);
	   
	   
	   
	   // find all the cities
	 Main obj = new Main();
	 obj.findCities();
	 
	 
// 	 find highest number
	  Integer arr[] = {10,20,30,40,50,10,30,50,30};
      List<Integer> listNumber = Arrays.asList(arr).stream().sorted((n1,n2) -> n2-n1).collect(Collectors.toList());
      System.out.println(listNumber);
      
      
      System.out.println(Arrays.asList(arr).stream().mapToInt(x->x).summaryStatistics());
      
      
      
        Integer a1[]={10,20,30,40};
        Integer a2[]={20,50,10,60,80};

        List<Integer> intList = Arrays.stream(a1).filter((ar1)-> Arrays.stream(a2).anyMatch((ar2) -> ar1== ar2)).collect(Collectors.toList());
        System.out.println(intList);
      
        List<String> lagestString = Arrays.asList("test1asd", "test4ada","test3sdvdsds", "test1sds", "test3FShjhj");
        lagestString.stream().sorted((s1,s2) -> s2.length() - s1.length()).limit(1).forEach(System.out::println);
        // System.out.println("length is : " + length);
    
    
        List<String> removeDuplicate = Arrays.asList("test1", "test2","test1", "test1sds", "test2");
        Set<String> data = new LinkedHashSet<>();
        Set<String> strData = removeDuplicate.stream().filter(s-> data.add(s)).collect(Collectors.toSet());
        System.out.println(strData);

      
	}
	
	
	
	public void findCities() {
	     List<String> city = Arrays.asList("test", "test2","test3");
	
	    List<String> city1 = Arrays.asList("test1", "test4","test3");
	    
	    Employee e1=new Employee("1","Pritesh",city);
	    Employee e2=new Employee("2","Pintu",city1);
	    
	    List<Employee> empList = new ArrayList<Employee>();
	    empList.add(e1);
	    empList.add(e2);
	    
	    
	    System.out.println(empList);
	    
	    Set<String> cities = empList.stream().flatMap(emp-> emp.getCities().stream()).collect(Collectors.toSet());
	    System.out.println(cities);
	    
	    
	    // find the frequancy of the word
	    List<String> cityTest = Arrays.asList("test1", "test4","test3", "test1", "test3");
	    Map<String,Long> mapTest = cityTest.stream().collect(Collectors.groupingBy(Function.identity(),Collectors.counting()));
        Set<String> unique= mapTest.entrySet().stream().filter((m) -> m.getValue() > 1).map(m -> m.getKey()).collect(Collectors.toSet());
        System.out.println(unique);
        
        
        
        Trasactional t1 = new Trasactional("product1", 10, 5);
        Trasactional t2 = new Trasactional("product2", 15, 13);
        Trasactional t3 = new Trasactional("product3", 50, 4);
        Trasactional t4 = new Trasactional("product1", 33, 12);
        Trasactional t5 = new Trasactional("product5", 60, 4);
        Trasactional t6 = new Trasactional("product6", 38, 9);
        
        List<Trasactional> transactionlList = new ArrayList<Trasactional>();
        transactionlList.add(t1);
        transactionlList.add(t2);
        transactionlList.add(t3);
        transactionlList.add(t4);
        transactionlList.add(t5);
        transactionlList.add(t6);
        
        
        System.out.println("************ Hey **************");
      
      // total product sell  
      int totalProductSell = transactionlList.stream().map((a) -> a.getQuantity()).reduce((a,b) -> a+b).get();
      System.out.println("totalProductSell : " + totalProductSell);
      
      // top 5 product sell
      List<Trasactional> tList= transactionlList.stream().sorted((a,b) -> (b.getQuantity()*b.getPrice())-(a.getQuantity()*a.getPrice())).limit(5).collect(Collectors.toList());
      System.out.println("Top 5 Product sell is : ");
      tList.forEach((p) -> {
          System.out.println(p.getProductName() + " " + p.getQuantity()*p.getPrice());
      });
      
      
      // average price of the product
      Map<String,List<Trasactional>> traList= transactionlList.stream().collect(Collectors.groupingBy(Trasactional::getProductName,Collectors.toList()));
      traList.forEach((key, value) -> {
        //   System.out.println(key + " ");
        //   value.forEach((v) -> System.out.println(v.getQuantity()));
         
         //product sell amount
          int totalPrice = value.stream().map((a) -> a.getPrice() * a.getQuantity()).reduce((a,b) -> a+b).get();
          System.out.println("************************************************");
          System.out.println("Product is : " + key);
          System.out.println("================================================");
          System.out.println("totalPrice: " + totalPrice);
          System.out.println("Product " + key + " sell amount is : " + totalPrice);
          
          
          int totalProduct = value.stream().map((a) -> a.getQuantity()).reduce((a,b) -> a+b).get();
          System.out.println("totalProduct: " + totalProduct);
          
          // average product price
          float avgPrice = totalPrice / totalProduct;
          System.out.println("Average price of Product " + key + " is : " + avgPrice);
          
          
          System.out.println("================================================");

      });
      System.out.println("************ Hey END **************");
	}
	
	
	class Trasactional {
    	String productName;
    	int price;
    	int quantity;	
    	
    	
    	Trasactional(String prodName, int price, int quantity) {
    	    this.productName = prodName;
    	    this.price=price;
    	    this.quantity=quantity;
    	}
    	
    	
    	public String getProductName() {
    	    return productName;
    	}
    	
    	public int getPrice() {
    	    return price;
    	}
    	
    	public int getQuantity() {
    	    return quantity;
    	}
    }
	
	class Employee {
	    String id;
	    String name;
	    List<String> cities;
	    
	    Employee(String id, String name, List<String> city) {
	        this.id=id;
	        this.name = name;
	        this.cities=city;
	    }
	    
	    public List<String> getCities() {
	        return cities;
	    }
	}
	
	public static boolean checkprimeNumber(Integer no) {
	    return IntStream.range(2,no/2).noneMatch(n -> no%n==0);
	}
	
}
