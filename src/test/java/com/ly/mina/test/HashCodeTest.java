package com.ly.mina.test;

import java.util.HashMap;
public class HashCodeTest {
	
	public static void main(String[] args) {
		
		HashMap<Person,String> personMap =  new HashMap<Person,String>();
		
		Person person1 = new Person(23,"liyang");
		Person person2= new Person(25,"liyang");
		
		System.out.println(person1.equals(person2));
		
		personMap.put(person1, "person1");
		personMap.put(person2, "person2");
		
		System.out.println(personMap.keySet().size());
	}
}

/**
 *  如果hashcode不同,则要求eqalus一定不相同  如果相同的话  会造成两个相同的equals 分步在不同的hash表中 , 这与hash数据结构相违背
 *  如果hashcode相同,则equals不一定要相同
 *  如果equals相同  则hashcode一定要相同
 *  如果equals不相同,则hasecode要尽大可能的让其不相同
 */
class Person{
	int age;
	String name;
	
	public Person(int age,String name){
		this.age  = age;
		this.name = name;
	}
	
	@Override
	public int hashCode() {
		return age;
	}
	
	@Override
	public boolean equals(Object obj) {
		Person p = (Person) obj;
		return p.name==this.name;
	}
}

