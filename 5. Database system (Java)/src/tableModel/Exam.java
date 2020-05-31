package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class Exam {
    private String id;
    private String subject_id;
    private String content;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getSubjId() {
    	if(subject_id.equals("Subject ID"))
    		return "> 0";
        return subject_id;
    }
    
    public void setSubjId(String subject_id) {
        this.subject_id = subject_id;
    }
    
    public String getContent() {
    	if(content.equals("Content"))
    		return "_";
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }

    public Exam(String id, String subject_id, String content){
		setId(id);
		setSubjId(subject_id);
		setContent(content);
	}
	
	public Exam(){}
}