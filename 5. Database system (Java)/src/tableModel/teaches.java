package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class teaches {
	private String teacher_id;
	private String subject_id;
	private String dateFrom;

	public String getTeacher_id() {
		if(teacher_id.equals("Teacher ID"))
			return "> 0";
		return teacher_id;
	}
	
	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}
	
	public String getSubject_id() {
		if(subject_id.equals("Subject ID"))
			return "> 0";
		return subject_id;
	}
	
	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}
	
	public String getDateFrom() {
		if(dateFrom.equals("DateFrom"))
			return "> '1000-01-01'";
		return dateFrom;
	}
	
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	
	public teaches(String teacher_id, String subject_id, String dateFrom){
		setTeacher_id(teacher_id);
		setSubject_id(subject_id);
		setDateFrom(dateFrom);
	}
	
	public teaches(){}
}