package tableModel;

/**
 * Created by GLaDOS on 05-Apr-17.
 */
public class Result {
    private String id;
    private String exam_id;
    private String mark;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getExamId() {
    	if(exam_id.equals("Exam ID"))
    		return "> 0";
        return exam_id;
    }
    
    public void setExamId(String exam_id) {
        this.exam_id = exam_id;
    }
    
    public String getMark() {
    	if(mark.equals("Mark"))
    		return "> 0";
        return mark;
    }
    
    public void setMark(String mark) {
        this.mark = mark;
    }

    public Result(String id, String exam_id, String mark){
		setId(id);
		setExamId(exam_id);
		setMark(mark);
	}
	
	public Result(){}
}