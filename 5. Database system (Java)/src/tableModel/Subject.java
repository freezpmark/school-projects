package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class Subject {
	private String id;
	private String type_id;
	private String name;
	private String description;

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getType_id() {
		if(type_id.equals("Type ID"))
    		return "> 0";
		return type_id;
	}
	
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	
	public String getName() {
		if(name.equals("Subject name"))
			return "_";
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String descr) {
		this.description = descr;
	}
	
	public Subject(String id, String typeId, String name, String descr){
		setId(id);
		setType_id(typeId);
		setName(name);
		setDescription(descr);
	}
	
	public Subject(){}
}