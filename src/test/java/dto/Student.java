package dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Student {

    private String name;
    private int age;
    private String city;
    private List<Integer> marks;

    public Student() {
    }

    public Student(String name, int age, String city, List<Integer> marks) {
        this.name = name;
        this.age = age;
        this.city = city;
        this.marks = marks;
    }
}
