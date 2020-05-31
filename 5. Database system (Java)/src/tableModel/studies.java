package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class studies {
	private String student_id;
	private String subject_id;
	private String dateTo;
	private String grade;

	public String getStudent_id() {
		if(student_id.equals("Student ID"))
			return "> 0";
		return student_id;
	}
	
	public void setStudent_id(String student_id) {
		this.student_id = student_id;
	}
	
	public String getSubject_id() {
		if(subject_id.equals("Subject ID"))
			return "> 0";
		return subject_id;
	}
	
	public void setSubject_id(String subject_id) {
		this.subject_id = subject_id;
	}
	
	public String getDateTo() {
		if(dateTo.equals("DateTo"))
			return "> '1000-01-01'";
		return dateTo;
	}
	
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	
	public String getGrade() {
		if(grade.equals("Grade"))
			return "> 0";
		return grade;
	}
	
	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	public studies(String student_id, String subject_id, String dateTo, String grade){
		setStudent_id(student_id);
		setSubject_id(subject_id);
		setDateTo(dateTo);
		setGrade(grade);
	}
	
	public studies(){}
}