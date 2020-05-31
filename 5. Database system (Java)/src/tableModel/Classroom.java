package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class Classroom {
    private String id;
    private String school_id;
    private String capacity;
    private String name;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSchoolId() {
    	if(school_id.equals("School ID"))
    		return "> 0";
        return school_id;
    }
    
    public void setSchoolId(String SchoolId) {
        this.school_id = SchoolId;
    }
    
    public String getCapacity() {
    	if(capacity.equals("Capacity")) {
    		return ">= 0";
    	}
        return capacity;
    }
    
    public void setCapacity(String capacity) {
    	if(capacity.equals("Capacity"))
    		this.capacity = "0";
        this.capacity = capacity;
    }
    
    public String getName() {
    	if(name.equals("Class name"))
    		return "_";
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Classroom (String id, String school_id, String capacity, String name){
		setId(id);
		setSchoolId(school_id);
		setCapacity(capacity);
		setName(name);
	}
	
	public Classroom(){}
}