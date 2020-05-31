package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class School {
	private String id;
	private String name;
	private String address;
	private String phone;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		if(name.equals("School name"))
			return "_";
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		if(address.equals("Address"))
			return "_";
		return address;
	}
	
	public void setAddress(String addr) {
		this.address = addr;
	}
	
	public String getPhone() {
		if(phone.equals("Phone number"))
			return "_";
		return phone;
	}
	
	public void setPhone(String phoneNum) {
		this.phone = phoneNum;
	}
	
	public School(String id, String name, String address, String phone){
		setId(id);
		setName(name);
		setAddress(address);
		setPhone(phone);
	}
	
	public School(){}
}