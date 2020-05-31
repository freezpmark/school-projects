package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class Student {
 	private String id;
	private String name;
	private String surname;
	private String email;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
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
	
	public String getEmail() {
		if(email.equals("Email address"))
			return "_";
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Student(String id, String name, String surname, String email){
		setId(id);
		setName(name);
		setSurname(surname);
		setEmail(email);
	}
	
	public Student(){}
}