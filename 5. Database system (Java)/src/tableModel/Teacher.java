package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class Teacher {
	private String id;
	private String classroom_id;
	private String name;
	private String surname;
	private String birthDate;
	private String email;
	private String salary;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getClassId() {
		if(classroom_id.equals("Classroom ID"))
    		return "> 0";
		return classroom_id;
	}
	
	public void setClassId(String id) {
		this.classroom_id = id;
	}
	
	public String getName() {
		if(name.equals("First name"))
			return "_";
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		if(surname.equals("Surname"))
			return "_";
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getBirthDate() {
		if(birthDate.equals("Birth date"))
			return "> '1000-01-01'";
		return birthDate;
	}
	
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	
	public String getEmail() {
		if(email.equals("Email address"))
			return "_";
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getSalary() {
		if(salary.equals("Salary"))
			return "> 0";
		return salary;
	}
	
	public void setSalary(String salary) {
		this.salary = salary;
	}
	
	public Teacher(String id, String classId, String name, String surname, String birthDate, String email, String salary){
		setId(id);
		setClassId(classId);
		setName(name);
		setSurname(surname);
		setBirthDate(birthDate);
		setEmail(email);
		setSalary(salary);
	}
	
	public Teacher(){}
}