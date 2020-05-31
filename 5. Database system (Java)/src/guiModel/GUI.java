package guiModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.HeadlessException;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.sql.SQLException;
import org.postgresql.ds.PGPoolingDataSource;

import managers.ManagerClassroom;
import managers.ManagerExam;
import managers.ManagerResult;
import managers.ManagerSchool;
import managers.ManagerStudent;
import managers.ManagerStudies;
import managers.ManagerSubject;
import managers.ManagerTeacher;
import managers.ManagerTeaches;
import tableModel.Classroom;
import tableModel.Exam;
import tableModel.Result;
import tableModel.School;
import tableModel.Student;
import tableModel.Subject;
import tableModel.Teacher;
import tableModel.studies;
import tableModel.teaches;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("deprecation")
public class GUI {
	ManagerStudent 	studentM;
	ManagerTeacher 	teacherM;
	ManagerSubject 	subjectM;
	ManagerSchool 	schoolM;
	ManagerClassroom classroomM;
	ManagerExam 	examM;
	ManagerResult 	resultM;
	ManagerStudies 	studiesM;
	ManagerTeaches 	teachesM;
	
	boolean filter = false;
	JFrame frame;
	public JScrollPane scrollPane;
	
	private JTextField txtFirstName;
	private JTextField txtSurname;
	private JTextField txtEmailAddress;
	private JTextField txtBirthDate;
	private JTextField txtSalary;
	private JTextField textSurname;
	private JTextField textName;
	private JTextField textEmail;
	private JTextField textNameSubject;
	private JTextField textNameSchool;
	private JTextField textAddress;
	private JTextField textPhone;
	private JTextField textNameClass;
	private JTextField textCapacity;
	private JTextField textContent;
	private JTextField textMark;
	private JTextField textDateTo;
	private JTextField textDateFrom;
	private JTextField textGrade;
	private JTextField textFKtype;
	private JTextField textFKschool;
	private JTextField textFKclass;
	private JTextField textFKsubject;
	private JTextField textFKexam;
	private JTextField textFKstudent;
	private JTextField textFKstudiesSubject;
	private JTextField textFKteacher;
	private JTextField textFKteachesSubject;
	
	private JTable tableStudent;
	private JTable tableTeacher;
	private JTable tableSubject;
	private JTable tableSchool;
	private JTable tableClassroom;
	private JTable tableExam;
	private JTable tableResult;
	private JTable tableStudies;
	private JTable tableTeaches;
	private JTable tableDiscipline;
	
	private Student filterStudent;
	private Teacher filterTeacher;
	private Subject filterSubject;
	private School filterSchool;
	private Classroom filterClassroom;
	private Exam filterExam;
	private Result filterResult;
	private studies filterStudies;
	private teaches filterTeaches;
	
	public static void main(String[] args) throws SQLException {

		System.out.println("START!");
		PGPoolingDataSource source = new PGPoolingDataSource();
		source.setDataSourceName("A Data Source");
		source.setServerName("localhost");
		source.setDatabaseName("c2");
		source.setUser("postgres");
		source.setPassword("");
		source.setMaxConnections(10);
		GUI gui = new GUI(source);
		gui.frame.setVisible(true);
	}
	
	public GUI(PGPoolingDataSource source) throws SQLException {
		this.studentM 	= new ManagerStudent(source);
		this.teacherM 	= new ManagerTeacher(source);
		this.subjectM 	= new ManagerSubject(source);
		this.schoolM 	= new ManagerSchool(source);
		this.classroomM = new ManagerClassroom(source);
		this.examM 		= new ManagerExam(source);
		this.resultM 	= new ManagerResult(source);
		this.studiesM 	= new ManagerStudies(source);
		this.teachesM 	= new ManagerTeaches(source);
		initialize();
	}
	
	public void setStudentDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][5];
		int i = 0;
		if(!filter)
			for (Student item : studentM.getAllStudents()) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = item.getSurname();
				data[i][3] = item.getEmail();
				data[i][4] = studentM.getVsp(data[i++][0]);
			}
		else {
			for (Student item : studentM.getAllStudents(filterStudent)) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = item.getSurname();
				data[i][3] = item.getEmail();
				data[i][4] = studentM.getVsp(data[i++][0]);
			}
		}
		tableStudent = new JTable();
		scrollPane.setViewportView(tableStudent);
		tableStudent.setModel(new ModelStudent(data));
	}
	
	public void setTeacherDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][7];
		int i = 0;
		if(!filter)
			for (Teacher item : teacherM.getAllTeachers()) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = item.getSurname();
				data[i][3] = item.getBirthDate();
				data[i][4] = item.getEmail();
				data[i][5] = item.getSalary();
				data[i][6] = teacherM.getDuration(data[i++][0]);
			}
		else {
			for(Teacher item : teacherM.getAllTeachers(filterTeacher)) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = item.getSurname();
				data[i][3] = item.getBirthDate();
				data[i][4] = item.getEmail();
				data[i][5] = item.getSalary();
				data[i][6] = teacherM.getDuration(data[i++][0]);
			}
		}
		tableTeacher = new JTable();
		scrollPane.setViewportView(tableTeacher);
		tableTeacher.setModel(new ModelTeacher(data));
	}
	
	public void setSubjectDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][5];
		int i = 0;
		if(!filter)
			for (Subject item : subjectM.getAllSubjects()) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = subjectM.getType(data[i][0]);
				data[i][3] = subjectM.getStudyAmount(data[i][0]);
				data[i][4] = subjectM.getTeachAmount(data[i++][0]);
			}
		else {
			for(Subject item : subjectM.getAllSubjects(filterSubject)) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = subjectM.getType(data[i][0]);
				data[i][3] = subjectM.getStudyAmount(data[i][0]);
				data[i][4] = subjectM.getTeachAmount(data[i++][0]);
			}
		}
		tableSubject = new JTable();
		scrollPane.setViewportView(tableSubject);
		tableSubject.setModel(new ModelSubject(data));
	}

	public void setSchoolDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][5];
		int i = 0;
		if(!filter)
			for (School item : schoolM.getAllSchools()) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = item.getAddress();
				data[i][3] = item.getPhone();
				data[i][4] = schoolM.getClassAmount(data[i++][0]);
			}
		else {
			for(School item : schoolM.getAllSchools(filterSchool)) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = item.getAddress();
				data[i][3] = item.getPhone();
				data[i][4] = schoolM.getClassAmount(data[i++][0]);
			}
		}
		tableSchool = new JTable();
		scrollPane.setViewportView(tableSchool);
		tableSchool.setModel(new ModelSchool(data));
	}

	public void setClassroomDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][4];
		int i = 0;
		if(!filter)
			for (Classroom item : classroomM.getAllClassrooms()) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = item.getCapacity();
				data[i][3] = classroomM.getTeachersAmount(data[i++][0]);
			}
		else {
			for(Classroom item : classroomM.getAllClassrooms(filterClassroom)) {
				data[i][0] = item.getId();
				data[i][1] = item.getName();
				data[i][2] = item.getCapacity();
				data[i][3] = classroomM.getTeachersAmount(data[i++][0]);
			}
		}
		tableClassroom = new JTable();
		scrollPane.setViewportView(tableClassroom);
		tableClassroom.setModel(new ModelClassroom(data));
	}

	public void setExamDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][3];
		int i = 0;
		if(!filter)
		for (Exam item : examM.getAllExams()) {
			data[i][0] = item.getId();
			data[i][1] = item.getContent();
			data[i][2] = examM.getResultAmount(data[i++][0]);
		}
		else {
			for (Exam item : examM.getAllExams(filterExam)) {
				data[i][0] = item.getId();
				data[i][1] = item.getContent();
				data[i][2] = examM.getResultAmount(data[i++][0]);
			}
		}
		tableExam = new JTable();
		scrollPane.setViewportView(tableExam);
		tableExam.setModel(new ModelExam(data));
	}

	public void setResultDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][2];
		int i = 0;
		if(!filter)
		for (Result item : resultM.getAllResults()) {
			data[i][0] = item.getId();
			data[i++][1] = item.getMark();
		}
		else {
			for (Result item : resultM.getAllResults(filterResult)) {
				data[i][0] = item.getId();
				data[i++][1] = item.getMark();
			}
		}
		tableResult = new JTable();
		scrollPane.setViewportView(tableResult);
		tableResult.setModel(new ModelResult(data));
	}

	public void setStudiesDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][4];
		int i = 0;
		if(!filter)
			for (studies item : studiesM.getAllStudies()) {
				data[i][0] = item.getStudent_id();
				data[i][1] = item.getSubject_id();
				data[i][2] = item.getDateTo();
				data[i++][3] = item.getGrade();
			}
		else {
			for (studies item : studiesM.getAllStudies(filterStudies)) {
				data[i][0] = item.getStudent_id();
				data[i][1] = item.getSubject_id();
				data[i][2] = item.getDateTo();
				data[i++][3] = item.getGrade();
			}
		}
		tableStudies = new JTable();
		scrollPane.setViewportView(tableStudies);
		tableStudies.setModel(new ModelStudies(data));
	}
	
	public void setTeachesDisplay(JScrollPane scrollPane) throws SQLException {
		Object[][] data = new Object[25][3];
		int i = 0;
		if(!filter)
			for (teaches item : teachesM.getAllTeaches()) {
				data[i][0] = item.getTeacher_id();
				data[i][1] = item.getSubject_id();
				data[i++][2] = item.getDateFrom();
			}
		else {
			for (teaches item : teachesM.getAllTeaches(filterTeaches)) {
				data[i][0] = item.getTeacher_id();
				data[i][1] = item.getSubject_id();
				data[i++][2] = item.getDateFrom();
			}
		}
		tableTeaches = new JTable();
		scrollPane.setViewportView(tableTeaches);
		tableTeaches.setModel(new ModelTeaches(data));
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		frame = new JFrame();
		frame.setBounds(100, 100, 826, 632);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 790, 510);
		frame.getContentPane().add(tabbedPane);
		
		JPanel Student = new JPanel();
		tabbedPane.addTab("Students", null, Student, null);
		Student.setLayout(null);
		JScrollPane scrollStudent = new JScrollPane();
		scrollStudent.setBounds(10, 11, 765, 424);
		Student.add(scrollStudent);
		setStudentDisplay(scrollStudent);
		
		//************************************** STUDENTS TAB ***************************************************//
		// PAGING BUTTONS
		JButton pageBack = new JButton("<");
		pageBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	studentM.setfilterInkrement(25);
				else		studentM.setInkrement(25);
				try {
					setStudentDisplay(scrollStudent);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		pageBack.setBounds(669, 451, 41, 20);
		Student.add(pageBack);
		////////////////////////////////////////
		JButton pageForward = new JButton(">");
		pageForward.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	studentM.setfilterInkrement(25);
				else		studentM.setInkrement(25);
				try {
					setStudentDisplay(scrollStudent);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		//TEXTFIELD FOR ADDING ENTRY
		txtSurname = new JTextField();
		txtSurname.setBounds(106, 451, 86, 20);
		Student.add(txtSurname);
		txtSurname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtSurname.getText().trim().equals("") || (txtSurname.getText().trim().equals("Surname")))
					txtSurname.setText("Surname");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(txtSurname.getText().trim().equals("Surname"))
					txtSurname.setText("");
			}
		});
		txtSurname.setText("Surname");
		txtSurname.setColumns(10);
		////////////////////////////////////////
		txtFirstName = new JTextField();
		txtFirstName.setBounds(10, 451, 86, 20);
		Student.add(txtFirstName);
		txtFirstName.setText("First name");
		txtFirstName.addFocusListener(new FocusAdapter() {
				@Override
			public void focusLost(FocusEvent e) {
				if(txtFirstName.getText().trim().equals("") || (txtFirstName.getText().trim().equals("First name")))
					txtFirstName.setText("First name");
				}
			@Override
			public void focusGained(FocusEvent e) {
				if(txtFirstName.getText().trim().equals("First name"))
					txtFirstName.setText("");
			}
		});
		txtFirstName.setToolTipText("");
		////////////////////////////////////////
		txtEmailAddress = new JTextField();
		txtEmailAddress.setBounds(202, 451, 86, 20);
		Student.add(txtEmailAddress);
		txtEmailAddress.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
			if(txtEmailAddress.getText().trim().equals("") || (txtEmailAddress.getText().trim().equals("Email address")))
				txtEmailAddress.setText("Email address");
			}
				
			@Override
			public void focusGained(FocusEvent e) {
				if(txtEmailAddress.getText().trim().equals("Email address"))
					txtEmailAddress.setText("");
			}
			
		});
		txtEmailAddress.setText("Email address");
		txtEmailAddress.setColumns(10);
		
		pageForward.setBounds(720, 451, 41, 20);
		Student.add(pageForward);
		
		//************************************** TEACHERS TAB ***************************************************//
		JPanel Teachers = new JPanel();
		tabbedPane.addTab("Teachers", null, Teachers, null);
		Teachers.setLayout(null);
		////////////////////////////////////////////
		txtBirthDate = new JTextField();
		txtBirthDate.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				if(txtBirthDate.getText().trim().equals("") || (txtBirthDate.getText().trim().equals("Birth date")))
					txtBirthDate.setText("Birth date");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(txtBirthDate.getText().trim().equals("Birth date"))
					txtBirthDate.setText("");
			}
		});
		
		JScrollPane scrollTeacher = new JScrollPane();
		scrollTeacher.setBounds(10, 11, 765, 424);
		Teachers.add(scrollTeacher);
		setTeacherDisplay(scrollTeacher);
		
		scrollTeacher.setViewportView(tableTeacher);
		txtBirthDate.setToolTipText("");
		txtBirthDate.setText("Birth date");
		txtBirthDate.setColumns(10);
		txtBirthDate.setBounds(298, 451, 86, 20);
		Teachers.add(txtBirthDate);
		////////////////////////////////////////
		txtSalary = new JTextField();
		txtSalary.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(txtSalary.getText().trim().equals("") || (txtSalary.getText().trim().equals("Salary")))
					txtSalary.setText("Salary");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(txtSalary.getText().trim().equals("Salary"))
					txtSalary.setText("");
			}
		});
		txtSalary.setToolTipText("");
		txtSalary.setText("Salary");
		txtSalary.setColumns(10);
		txtSalary.setBounds(394, 451, 86, 20);
		Teachers.add(txtSalary);
		
		textSurname = new JTextField();
		textSurname.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textSurname.getText().trim().equals("") || (textSurname.getText().trim().equals("Surname")))
					textSurname.setText("Surname");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textSurname.getText().trim().equals("Surname"))
					textSurname.setText("");
			}
		});
		textSurname.setToolTipText("");
		textSurname.setText("Surname");
		textSurname.setColumns(10);
		textSurname.setBounds(106, 451, 86, 20);
		Teachers.add(textSurname);
		textName = new JTextField();
		textName.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textName.getText().trim().equals("") || (textName.getText().trim().equals("First name")))
					textName.setText("First name");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textName.getText().trim().equals("First name"))
					textName.setText("");
			}
		});
		textName.setToolTipText("");
		textName.setText("First name");
		textName.setColumns(10);
		textName.setBounds(10, 451, 86, 20);
		Teachers.add(textName);
		
		textEmail = new JTextField();
		textEmail.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textEmail.getText().trim().equals("") || (textEmail.getText().trim().equals("Email address")))
					textEmail.setText("Email address");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textEmail.getText().trim().equals("Email address"))
					textEmail.setText("");
			}
		});
		textEmail.setText("Email address");
		textEmail.setColumns(10);
		textEmail.setBounds(202, 451, 86, 20);
		Teachers.add(textEmail);
		
		JButton button = new JButton("<");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	teacherM.setfilterInkrement(-25);
				else		teacherM.setInkrement(-25);
				try {
					setTeacherDisplay(scrollTeacher);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button.setBounds(669, 451, 41, 20);
		Teachers.add(button);
		
		JButton button_1 = new JButton(">");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	teacherM.setfilterInkrement(25);
				else		teacherM.setInkrement(25);
				try {
					setTeacherDisplay(scrollTeacher);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_1.setBounds(720, 451, 41, 20);
		Teachers.add(button_1);
		
		textFKclass = new JTextField();
		textFKclass.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKclass.getText().trim().equals("") || (textFKclass.getText().trim().equals("Classroom ID")))
					textFKclass.setText("Classroom ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKclass.getText().trim().equals("Classroom ID"))
					textFKclass.setText("");
			}
		});
		textFKclass.setText("Classroom ID");
		textFKclass.setBounds(520, 451, 86, 20);
		Teachers.add(textFKclass);
		textFKclass.setColumns(10);
		
		//************************************** SUBJECTS TAB ***************************************************/
		JPanel Subjects = new JPanel();
		tabbedPane.addTab("Subjects", null, Subjects, null);
		Subjects.setLayout(null);
		
		JScrollPane scrollSubject = new JScrollPane();
		scrollSubject.setBounds(10, 11, 765, 424);
		Subjects.add(scrollSubject);
		setSubjectDisplay(scrollSubject);
		scrollSubject.setViewportView(tableSubject);
		textNameSubject = new JTextField();
		textNameSubject.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textNameSubject.getText().trim().equals("") || (textNameSubject.getText().trim().equals("Subject name")))
					textNameSubject.setText("Subject name");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textNameSubject.getText().trim().equals("Subject name"))
					textNameSubject.setText("");
			}
		});
		textNameSubject.setToolTipText("");
		textNameSubject.setText("Subject name");
		textNameSubject.setColumns(10);
		textNameSubject.setBounds(10, 451, 86, 20);
		Subjects.add(textNameSubject);
		
		JButton button_2 = new JButton("<");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	subjectM.setfilterInkrement(-25);
				else		subjectM.setInkrement(-25);
				try {
					setSubjectDisplay(scrollSubject);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_2.setBounds(669, 451, 41, 20);
		Subjects.add(button_2);
		JButton button_3 = new JButton(">");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	subjectM.setfilterInkrement(25);
				else		subjectM.setInkrement(25);
				try {
					setSubjectDisplay(scrollSubject);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_3.setBounds(720, 451, 41, 20);
		Subjects.add(button_3);
		
		textFKtype = new JTextField();
		textFKtype.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKtype.getText().trim().equals("") || (textFKtype.getText().trim().equals("Type ID")))
					textFKtype.setText("Type ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKtype.getText().trim().equals("Type ID"))
					textFKtype.setText("");
			}
		});
		textFKtype.setText("Type ID");
		textFKtype.setBounds(136, 451, 86, 20);
		Subjects.add(textFKtype);
		textFKtype.setColumns(10);
		
		//************************************** SCHOOL TAB ***************************************//
		JPanel Schools = new JPanel();
		tabbedPane.addTab("Schools", null, Schools, null);
		Schools.setLayout(null);
		
		JScrollPane scrollSchool = new JScrollPane();
		scrollSchool.setBounds(10, 11, 765, 424);
		Schools.add(scrollSchool);
		setSchoolDisplay(scrollSchool);
		scrollSchool.setViewportView(tableSchool);
		
		JButton button_4 = new JButton("<");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	schoolM.setfilterInkrement(-25);
				else		schoolM.setInkrement(-25);
				try {
					setSchoolDisplay(scrollSchool);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_4.setBounds(669, 451, 41, 20);
		Schools.add(button_4);
		
		JButton button_5 = new JButton(">");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	schoolM.setfilterInkrement(25);
				else		schoolM.setInkrement(25);
				try {
					setSchoolDisplay(scrollSchool);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_5.setBounds(720, 451, 41, 20);
		Schools.add(button_5);
		
		textNameSchool = new JTextField();
		textNameSchool.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textNameSchool.getText().trim().equals("") || (textNameSchool.getText().trim().equals("School name")))
					textNameSchool.setText("School name");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textNameSchool.getText().trim().equals("School name"))
					textNameSchool.setText("");
			}
		});
		textNameSchool.setToolTipText("");
		textNameSchool.setText("School name");
		textNameSchool.setBounds(10, 451, 86, 20);
		Schools.add(textNameSchool);
		textNameSchool.setColumns(10);
		textAddress = new JTextField();
		textAddress.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textAddress.getText().trim().equals("") || (textAddress.getText().trim().equals("Address")))
					textAddress.setText("Address");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textAddress.getText().trim().equals("Address"))
					textAddress.setText("");
			}
		});
		textAddress.setToolTipText("");
		textAddress.setText("Address");
		textAddress.setBounds(106, 451, 86, 20);
		Schools.add(textAddress);
		textAddress.setColumns(10);
		textPhone = new JTextField();
		textPhone.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textPhone.getText().trim().equals("") || (textPhone.getText().trim().equals("Phone number")))
					textPhone.setText("Phone number");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textPhone.getText().trim().equals("Phone number"))
					textPhone.setText("");
			}
		});
		textPhone.setToolTipText("");
		textPhone.setText("Phone number");
		textPhone.setBounds(202, 451, 86, 20);
		Schools.add(textPhone);
		textPhone.setColumns(10);
		
		//************************************** CLASSROOM TAB ***************************************************//
		JPanel Classrooms = new JPanel();
		tabbedPane.addTab("Classrooms", null, Classrooms, null);
		Classrooms.setLayout(null);
		
		JScrollPane scrollClassroom = new JScrollPane();
		scrollClassroom.setBounds(10, 11, 765, 424);
		Classrooms.add(scrollClassroom);
		setClassroomDisplay(scrollClassroom);
		scrollClassroom.setViewportView(tableClassroom);
		
		JButton button_7 = new JButton(">");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	classroomM.setfilterInkrement(25);
				else		classroomM.setInkrement(25);
				try {
					setClassroomDisplay(scrollClassroom);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_7.setBounds(720, 451, 41, 20);
		Classrooms.add(button_7);
		
		JButton button_6 = new JButton("<");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	classroomM.setfilterInkrement(-25);
				else		classroomM.setInkrement(-25);
				try {
					setClassroomDisplay(scrollClassroom);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_6.setBounds(669, 451, 41, 20);
		Classrooms.add(button_6);
		textCapacity = new JTextField();
		textCapacity.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textCapacity.getText().trim().equals("") || (textCapacity.getText().trim().equals("Capacity")))
					textCapacity.setText("Capacity");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textCapacity.getText().trim().equals("Capacity"))
					textCapacity.setText("");
			}
		});
		textCapacity.setToolTipText("");
		textCapacity.setText("Capacity");
		textCapacity.setBounds(106, 451, 86, 20);
		Classrooms.add(textCapacity);
		textCapacity.setColumns(10);
		textNameClass = new JTextField();
		textNameClass.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textNameClass.getText().trim().equals("") || (textNameClass.getText().trim().equals("Class name")))
					textNameClass.setText("Class name");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textNameClass.getText().trim().equals("Class name"))
					textNameClass.setText("");
			}
		});
		textNameClass.setToolTipText("");
		textNameClass.setText("Class name");
		textNameClass.setBounds(10, 451, 86, 20);
		Classrooms.add(textNameClass);
		textNameClass.setColumns(10);
		textFKschool = new JTextField();
		textFKschool.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKschool.getText().trim().equals("") || (textFKschool.getText().trim().equals("School ID")))
					textFKschool.setText("School ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKschool.getText().trim().equals("School ID"))
					textFKschool.setText("");
			}
		});
		textFKschool.setText("School ID");
		textFKschool.setBounds(232, 451, 86, 20);
		Classrooms.add(textFKschool);
		textFKschool.setColumns(10);
		
		//************************************** EXAMS TAB ***************************************************//
		JPanel Exams = new JPanel();
		tabbedPane.addTab("Exams", null, Exams, null);
		Exams.setLayout(null);
		
		JScrollPane scrollExam = new JScrollPane();
		scrollExam.setBounds(10, 11, 765, 424);
		Exams.add(scrollExam);
		setExamDisplay(scrollExam);
		scrollExam.setViewportView(tableExam);
		JButton button_8 = new JButton(">");
		button_8.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	examM.setfilterInkrement(25);
				else		examM.setInkrement(25);
				try {
					setExamDisplay(scrollExam);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_8.setBounds(720, 451, 41, 20);
		Exams.add(button_8);
		
		JButton button_9 = new JButton("<");
		button_9.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	examM.setfilterInkrement(-25);
				else		examM.setInkrement(-25);
				try {
					setExamDisplay(scrollExam);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_9.setBounds(669, 451, 41, 20);
		Exams.add(button_9);
		textContent = new JTextField();
		textContent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textContent.getText().trim().equals("") || (textContent.getText().trim().equals("Content")))
					textContent.setText("Content");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textContent.getText().trim().equals("Content"))
					textContent.setText("");
			}
		});
		textContent.setToolTipText("");
		textContent.setText("Content");
		textContent.setBounds(10, 451, 86, 20);
		Exams.add(textContent);
		textContent.setColumns(10);
		
		textFKsubject = new JTextField();
		textFKsubject.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKsubject.getText().trim().equals("") || (textFKsubject.getText().trim().equals("Subject ID")))
					textFKsubject.setText("Subject ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKsubject.getText().trim().equals("Subject ID"))
					textFKsubject.setText("");
			}
		});
		textFKsubject.setText("Subject ID");
		textFKsubject.setBounds(136, 451, 86, 20);
		Exams.add(textFKsubject);
		textFKsubject.setColumns(10);
		
		//************************************** RESULTS TAB ***************************************************//
		JPanel Results = new JPanel();
		tabbedPane.addTab("Results", null, Results, null);
		Results.setLayout(null);
		
		JScrollPane scrollResult = new JScrollPane();
		scrollResult.setBounds(10, 11, 765, 424);
		Results.add(scrollResult);
		setResultDisplay(scrollResult);
		scrollResult.setViewportView(tableResult);
		
		JButton button_10 = new JButton(">");
		button_10.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	resultM.setfilterInkrement(25);
				else		resultM.setInkrement(25);
				try {
					setResultDisplay(scrollResult);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_10.setBounds(720, 451, 41, 20);
		Results.add(button_10);
		
		JButton button_11 = new JButton("<");
		button_11.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	resultM.setfilterInkrement(-25);
				else		resultM.setInkrement(-25);
				try {
					setResultDisplay(scrollResult);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_11.setBounds(669, 451, 41, 20);
		Results.add(button_11);
		textMark = new JTextField();
		textMark.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textMark.getText().trim().equals("") || (textMark.getText().trim().equals("Mark")))
					textMark.setText("Mark");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textMark.getText().trim().equals("Mark"))
					textMark.setText("");
			}
		});
		textMark.setToolTipText("");
		textMark.setText("Mark");
		textMark.setBounds(10, 451, 86, 20);
		Results.add(textMark);
		textMark.setColumns(10);
		textFKexam = new JTextField();
		textFKexam.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKexam.getText().trim().equals("") || (textFKexam.getText().trim().equals("Exam ID")))
					textFKexam.setText("Exam ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKexam.getText().trim().equals("Exam ID"))
					textFKexam.setText("");
			}
		});
		textFKexam.setText("Exam ID");
		textFKexam.setBounds(136, 451, 86, 20);
		Results.add(textFKexam);
		textFKexam.setColumns(10);
		
		//************************************** studies TAB ***************************************************//
		JPanel studies = new JPanel();
		tabbedPane.addTab("studies", null, studies, null);
		studies.setLayout(null);
		
		JScrollPane scrollStudies = new JScrollPane();
		scrollStudies.setBounds(10, 11, 765, 424);
		studies.add(scrollStudies);
		setStudiesDisplay(scrollStudies);
		scrollStudies.setViewportView(tableStudies);
		
		JButton button_12 = new JButton(">");
		button_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	studiesM.setfilterInkrement(25);
				else		studiesM.setInkrement(25);
				try {
					setStudiesDisplay(scrollStudies);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_12.setBounds(720, 451, 41, 20);
		studies.add(button_12);
		
		JButton button_13 = new JButton("<");
		button_13.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	studiesM.setfilterInkrement(-25);
				else		studiesM.setInkrement(-25);
				try {
					setStudiesDisplay(scrollStudies);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_13.setBounds(669, 451, 41, 20);
		studies.add(button_13);
		
		textFKstudent = new JTextField();
		textFKstudent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKstudent.getText().trim().equals("") || (textFKstudent.getText().trim().equals("Student ID")))
					textFKstudent.setText("Student ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKstudent.getText().trim().equals("Student ID"))
					textFKstudent.setText("");
			}
		});
		textFKstudent.setText("Student ID");
		textFKstudent.setBounds(10, 451, 86, 20);
		studies.add(textFKstudent);
		textFKstudent.setColumns(10);
		textFKstudiesSubject = new JTextField();
		textFKstudiesSubject.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKstudiesSubject.getText().trim().equals("") || (textFKstudiesSubject.getText().trim().equals("Subject ID")))
					textFKstudiesSubject.setText("Subject ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKstudiesSubject.getText().trim().equals("Subject ID"))
					textFKstudiesSubject.setText("");
			}
		});
		textFKstudiesSubject.setText("Subject ID");
		textFKstudiesSubject.setBounds(106, 451, 86, 20);
		studies.add(textFKstudiesSubject);
		textFKstudiesSubject.setColumns(10);
		textDateTo = new JTextField();
		textDateTo.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textDateTo.getText().trim().equals("") || (textDateTo.getText().trim().equals("DateTo")))
					textDateTo.setText("DateTo");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textDateTo.getText().trim().equals("DateTo"))
					textDateTo.setText("");
			}
		});
		textDateTo.setText("DateTo");
		textDateTo.setBounds(202, 451, 86, 20);
		studies.add(textDateTo);
		textDateTo.setColumns(10);
		
		textGrade = new JTextField();
		textGrade.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textGrade.getText().trim().equals("") || (textGrade.getText().trim().equals("Grade")))
					textGrade.setText("Grade");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textGrade.getText().trim().equals("Grade"))
					textGrade.setText("");
			}
		});
		textGrade.setText("Grade");
		textGrade.setBounds(298, 451, 86, 20);
		studies.add(textGrade);
		textGrade.setColumns(10);
		
		//************************************** teaches TAB ***************************************************//
		JPanel teaches = new JPanel();
		tabbedPane.addTab("teaches", null, teaches, null);
		teaches.setLayout(null);
		
		JScrollPane scrollTeaches = new JScrollPane();
		scrollTeaches.setBounds(10, 11, 765, 424);
		teaches.add(scrollTeaches);
		setTeachesDisplay(scrollTeaches);
		scrollTeaches.setViewportView(tableTeaches);
		
		JButton button_14 = new JButton(">");
		button_14.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	teachesM.setfilterInkrement(25);
				else		teachesM.setInkrement(25);
				try {
					setTeachesDisplay(scrollTeaches);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_14.setBounds(720, 451, 41, 20);
		teaches.add(button_14);
		
		JButton button_15 = new JButton("<");
		button_15.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(filter)	teachesM.setfilterInkrement(-25);
				else		teachesM.setInkrement(-25);
				try {
					setTeachesDisplay(scrollTeaches);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		button_15.setBounds(669, 451, 41, 20);
		teaches.add(button_15);
		
		textFKteacher = new JTextField();
		textFKteacher.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKteacher.getText().trim().equals("") || (textFKteacher.getText().trim().equals("Teacher ID")))
					textFKteacher.setText("Teacher ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKteacher.getText().trim().equals("Teacher ID"))
					textFKteacher.setText("");
			}
		});
		textFKteacher.setText("Teacher ID");
		textFKteacher.setBounds(10, 451, 86, 20);
		teaches.add(textFKteacher);
		textFKteacher.setColumns(10);
		
		textFKteachesSubject = new JTextField();
		textFKteachesSubject.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textFKteachesSubject.getText().trim().equals("") || (textFKteachesSubject.getText().trim().equals("Subject ID")))
					textFKteachesSubject.setText("Subject ID");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textFKteachesSubject.getText().trim().equals("Subject ID"))
					textFKteachesSubject.setText("");
			}
		});
		textFKteachesSubject.setText("Subject ID");
		textFKteachesSubject.setBounds(106, 451, 86, 20);
		teaches.add(textFKteachesSubject);
		textFKteachesSubject.setColumns(10);
		
		textDateFrom = new JTextField();
		textDateFrom.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				if(textDateFrom.getText().trim().equals("") || (textDateFrom.getText().trim().equals("DateFrom")))
					textDateFrom.setText("DateFrom");
			}
			@Override
			public void focusGained(FocusEvent e) {
				if(textDateFrom.getText().trim().equals("DateFrom"))
					textDateFrom.setText("");
			}
		});
		textDateFrom.setText("DateFrom");
		textDateFrom.setBounds(202, 451, 86, 20);
		teaches.add(textDateFrom);
		textDateFrom.setColumns(10);
		
		//************************************** DISCIPLINE TAB ***************************************************//
		JPanel Type_of_discipline = new JPanel();
		tabbedPane.addTab("Type of disciplines", null, Type_of_discipline, null);
		Type_of_discipline.setLayout(null);
		
		JScrollPane scrollDiscipline = new JScrollPane();
		scrollDiscipline.setBounds(10, 11, 765, 424);
		Type_of_discipline.add(scrollDiscipline);
		
		tableDiscipline = new JTable();
		tableDiscipline.setModel(new DefaultTableModel(
			new Object[][] {
				{1, "technical", 	subjectM.getTypeAmount(1)},
				{2, "natural", 		subjectM.getTypeAmount(2)},
				{3, "medical", 		subjectM.getTypeAmount(3)},
				{4, "philosophical",subjectM.getTypeAmount(4)},
				{5, "artistic", 	subjectM.getTypeAmount(5)},
				{6, "pedagogic", 	subjectM.getTypeAmount(6)},
				{7, "economic", 	subjectM.getTypeAmount(7)}
			},
			new String[] {
				"Id", "Discipline", "subject_amount"
			}
		));
		scrollDiscipline.setViewportView(tableDiscipline);
		
		/**************************** BUTTONS FOR ALL TABLES ********************************/
		// DELETING ENTRY
		JButton btnRemoveEntry = new JButton("Remove entry");
		btnRemoveEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(tabbedPane.getSelectedIndex()==0) {			// Students [BUGGED FILTER]
						if(!filter) {
							if(!studentM.deleteRow(tableStudent.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else 
								setStudentDisplay(scrollStudent);
						}
						else {
							if(!studentM.deleteFilterRow(tableStudent.getSelectedRow(), filterStudent))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setStudentDisplay(scrollStudent);
						}
					}
					if(tabbedPane.getSelectedIndex()==1) {			// Teachers
						if(!filter) {
							if(!teacherM.deleteRow(tableTeacher.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setTeacherDisplay(scrollTeacher);
						}
						else {
							if(!teacherM.deleteFilterRow(tableTeacher.getSelectedRow(), filterTeacher))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setTeacherDisplay(scrollTeacher);
						}
					}
					if(tabbedPane.getSelectedIndex()==2) {			// Subjects
						if(!filter) {
							if(!subjectM.deleteRow(tableSubject.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setSubjectDisplay(scrollSubject);
						}
						else {
							if(!subjectM.deleteFilterRow(tableSubject.getSelectedRow(), filterSubject))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setSubjectDisplay(scrollSubject);
						}
					}
					if(tabbedPane.getSelectedIndex()==3) {			// Schools
						if(!filter) {
							if(!schoolM.deleteRow(tableSchool.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setSchoolDisplay(scrollSchool);
						}
						else {
							if(!schoolM.deleteFilterRow(tableSchool.getSelectedRow(), filterSchool))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setSchoolDisplay(scrollSchool);
						}
					}
					if(tabbedPane.getSelectedIndex()==4) {			// Classrooms
						if(!filter) {
							if(!classroomM.deleteRow(tableClassroom.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setClassroomDisplay(scrollClassroom);
						}
						else {
							if(!classroomM.deleteFilterRow(tableClassroom.getSelectedRow(), filterClassroom))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setClassroomDisplay(scrollClassroom);
						}
					}
					if(tabbedPane.getSelectedIndex()==5) {			// Exams
						if(!filter) {
							if(!examM.deleteRow(tableExam.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setExamDisplay(scrollExam);
						}
						else {
							if(!examM.deleteFilterRow(tableExam.getSelectedRow(), filterExam))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setExamDisplay(scrollExam);
						}
					}
					if(tabbedPane.getSelectedIndex()==6) {			// Results
						if(!filter) {
							if(!resultM.deleteRow(tableResult.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setResultDisplay(scrollResult);
						}
						else {
							if(!resultM.deleteFilterRow(tableResult.getSelectedRow(), filterResult))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setResultDisplay(scrollResult);
						}
					}
					if(tabbedPane.getSelectedIndex()==7) {			// studies
						if(!filter) {
							if(!studiesM.deleteRow(tableStudies.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setStudiesDisplay(scrollStudies);
						}
						else {
							if(!studiesM.deleteFilterRow(tableStudies.getSelectedRow(), filterStudies))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setStudiesDisplay(scrollStudies);
						}
					}
					
					if(tabbedPane.getSelectedIndex()==8) {			// teaches
						if(!filter) {
							if(!teachesM.deleteRow(tableTeaches.getSelectedRow()))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else 
								setTeachesDisplay(scrollTeaches);
						}
						else {
							if(!teachesM.deleteFilterRow(tableTeaches.getSelectedRow(), filterTeaches))
								JOptionPane.showMessageDialog(null, "Select entry row for deleting first.");
							else
								setTeachesDisplay(scrollTeaches);
						}
					}
				} catch (HeadlessException | SQLException e2) {
					e2.printStackTrace();
				}
			}
		});
		btnRemoveEntry.setBounds(150, 532, 130, 50);
		frame.getContentPane().add(btnRemoveEntry);
				
		// ADDING ENTRY
		JButton btnAddEntry = new JButton("Add entry");
		btnAddEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(tabbedPane.getSelectedIndex()==0) {
						Student student = getStudentData();
						if(studentM.insertStudent(student))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
					if(tabbedPane.getSelectedIndex()==1) {
						Teacher teacher = getTeacherData();
						if(teacherM.insertTeacher(teacher))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
					if(tabbedPane.getSelectedIndex()==2) {
						Subject subject = getSubjectData();
						if(subjectM.insertSubject(subject))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
					if(tabbedPane.getSelectedIndex()==3) {
						School school = getSchoolData();
						if(schoolM.insertSchool(school))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
					if(tabbedPane.getSelectedIndex()==4) {
						Classroom classroom = getClassroomData();
						if(classroomM.insertClassroom(classroom))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
					if(tabbedPane.getSelectedIndex()==5) {
						Exam exam = getExamData();
						if(examM.insertExam(exam))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
					if(tabbedPane.getSelectedIndex()==6) {
						Result result = getResultData();
						if(resultM.insertResult(result))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
					if(tabbedPane.getSelectedIndex()==7) {
						studies studies = getStudiesData();
						if(studiesM.insertStudies(studies))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
					if(tabbedPane.getSelectedIndex()==8) {
						teaches teaches = getTeachesData();
						if(teachesM.insertTeaches(teaches))
							JOptionPane.showMessageDialog(null, "New entry has been successfully created.");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		btnAddEntry.setBounds(10, 532, 130, 50);
		frame.getContentPane().add(btnAddEntry);
			
		// EDITING ENTRY
		JButton btnEditEntry = new JButton("Edit entry");
		btnEditEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if(tabbedPane.getSelectedIndex()==0) {			// Students [BUGGED FILTER]
						Student student = getStudentData();
						if(!filter) {
							if(!studentM.editRow(tableStudent.getSelectedRow(), student))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else 
								setStudentDisplay(scrollStudent);
						}
						else {
							if(!studentM.editFilterRow(tableStudent.getSelectedRow(), student, filterStudent))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setStudentDisplay(scrollStudent);
						}
					}	
					if(tabbedPane.getSelectedIndex()==1) {			// Teachers 
						Teacher teacher = getTeacherData();
						if(!filter) {
							if(!teacherM.editRow(tableTeacher.getSelectedRow(), teacher))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setTeacherDisplay(scrollTeacher);
						}
						else {
							if(!teacherM.editFilterRow(tableTeacher.getSelectedRow(), teacher, filterTeacher))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setTeacherDisplay(scrollTeacher);
						}
					}
					if(tabbedPane.getSelectedIndex()==2) {			// Subjects
						Subject subject = getSubjectData();
						if(!filter) {
							if(!subjectM.editRow(tableSubject.getSelectedRow(), subject))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setSubjectDisplay(scrollSubject);
						}
						else {
							if(!subjectM.editFilterRow(tableSubject.getSelectedRow(), subject, filterSubject))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setSubjectDisplay(scrollSubject);
						}
					}
					if(tabbedPane.getSelectedIndex()==3) {			// Schools
						School school = getSchoolData();
						if(!filter) {
							if(!schoolM.editRow(tableSchool.getSelectedRow(), school))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setSchoolDisplay(scrollSchool);
						}
						else {
							if(!schoolM.editFilterRow(tableSchool.getSelectedRow(), school, filterSchool))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setSchoolDisplay(scrollSchool);
						}
					}
					
					if(tabbedPane.getSelectedIndex()==4) {			// Classrooms
						Classroom classroom = getClassroomData();
						if(!filter) {
							if(!classroomM.editRow(tableClassroom.getSelectedRow(), classroom))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setClassroomDisplay(scrollClassroom);
						}
						else {
							if(!classroomM.editFilterRow(tableClassroom.getSelectedRow(), classroom, filterClassroom))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setClassroomDisplay(scrollClassroom);
						}
					}
					if(tabbedPane.getSelectedIndex()==5) {			// Exams
						Exam exam = getExamData();
						if(!filter) {
							if(!examM.editRow(tableExam.getSelectedRow(), exam))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setExamDisplay(scrollExam);
						}
						else {
							if(!examM.editFilterRow(tableExam.getSelectedRow(), exam, filterExam))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setExamDisplay(scrollExam);
						}
					}
					if(tabbedPane.getSelectedIndex()==6) {			// Results
						Result result = getResultData();
						if(!filter) {
							if(!resultM.editRow(tableResult.getSelectedRow(), result))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setResultDisplay(scrollResult);
						}
						else {
							if(!resultM.editFilterRow(tableResult.getSelectedRow(), result, filterResult))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setResultDisplay(scrollResult);
						}
					}
					if(tabbedPane.getSelectedIndex()==7) {			// studies
						studies studies = getStudiesData();
						if(!filter) {
							if(!studiesM.editRow(tableStudies.getSelectedRow(), studies))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setStudiesDisplay(scrollStudies);
						}
						else {
							if(!studiesM.editFilterRow(tableStudies.getSelectedRow(), studies, filterStudies))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setStudiesDisplay(scrollStudies);
						}
					}
					if(tabbedPane.getSelectedIndex()==8) {			// teaches
						teaches teaches = getTeachesData();
						if(!filter) {
							if(!teachesM.editRow(tableTeaches.getSelectedRow(), teaches))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else 
								setTeachesDisplay(scrollTeaches);
						}
						else {
							if(!teachesM.editFilterRow(tableTeaches.getSelectedRow(), teaches, filterTeaches))
								JOptionPane.showMessageDialog(null, "Select entry row for editing first.");
							else
								setTeachesDisplay(scrollTeaches);
						}
					}
				} catch (HeadlessException | SQLException e2) {
					e2.printStackTrace();
				}
			}
		});
		btnEditEntry.setBounds(290, 532, 130, 50);
		frame.getContentPane().add(btnEditEntry);
		
		// FILTERING ENTRIES
		JButton btnFilterBy = new JButton("Filter by");
		btnFilterBy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					filter = true;
					if(tabbedPane.getSelectedIndex()==0) {
						filterStudent = getStudentData();
						setStudentDisplay(scrollStudent);
					}
					if(tabbedPane.getSelectedIndex()==1) {
						filterTeacher = getTeacherData();
						setTeacherDisplay(scrollTeacher);
					}
					if(tabbedPane.getSelectedIndex()==2) {
						filterSubject = getSubjectData();
						setSubjectDisplay(scrollSubject);
					}
					if(tabbedPane.getSelectedIndex()==3) {
						filterSchool = getSchoolData();
						setSchoolDisplay(scrollSchool);
					}
					if(tabbedPane.getSelectedIndex()==4) {
						filterClassroom = getClassroomData();
						setClassroomDisplay(scrollClassroom); 
					}
					if(tabbedPane.getSelectedIndex()==5) {
						filterExam = getExamData();
						setExamDisplay(scrollExam);
					}
					if(tabbedPane.getSelectedIndex()==6) {
						filterResult = getResultData();
						setResultDisplay(scrollResult);
					}
					if(tabbedPane.getSelectedIndex()==7) {
						filterStudies = getStudiesData();
						setStudiesDisplay(scrollStudies);
					}
					if(tabbedPane.getSelectedIndex()==8) {
						filterTeaches = getTeachesData();
						setTeachesDisplay(scrollTeaches);
					}
				} catch (SQLException e1){
					e1.printStackTrace();
				}
			}
		});
		btnFilterBy.setBounds(600, 532, 100, 50);
		frame.getContentPane().add(btnFilterBy);
		
		// UNFILTER ENTRIES
		JButton btnStopFilterBy = new JButton("Stop filter");
		btnStopFilterBy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					filter = false;
					if(tabbedPane.getSelectedIndex()==0)
						setStudentDisplay(scrollStudent);
					if(tabbedPane.getSelectedIndex()==1)
						setTeacherDisplay(scrollTeacher);
					if(tabbedPane.getSelectedIndex()==2)
						setSubjectDisplay(scrollSubject);
					if(tabbedPane.getSelectedIndex()==3)
						setSchoolDisplay(scrollSchool);
					if(tabbedPane.getSelectedIndex()==4)
						setClassroomDisplay(scrollClassroom);
					if(tabbedPane.getSelectedIndex()==5)
						setExamDisplay(scrollExam);
					if(tabbedPane.getSelectedIndex()==6)
						setResultDisplay(scrollResult);
					if(tabbedPane.getSelectedIndex()==7)
						setStudiesDisplay(scrollStudies);
					if(tabbedPane.getSelectedIndex()==8)
						setTeachesDisplay(scrollTeaches);
				} catch (SQLException e1){
					e1.printStackTrace();
				}
			}
		});
		
		btnStopFilterBy.setBounds(700, 532, 100, 50);
		frame.getContentPane().add(btnStopFilterBy);
		
		// VIEWING DETAILS OF ENTRY
		JButton btnViewDetails = new JButton("View details");
		btnViewDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int idView;
					if(tabbedPane.getSelectedIndex()==0) {			// Students [BUGGED FILTER]
						if(!filter) 
							idView = studentM.getId(tableStudent.getSelectedRow());
						else 
							idView = studentM.getId(tableStudent.getSelectedRow(), filterStudent);
						if(idView == -1)
							JOptionPane.showMessageDialog(frame, "Select entry row for viewing first.");
						else
							JOptionPane.showMessageDialog(null, studentM.viewDetail(idView));
					}
					if(tabbedPane.getSelectedIndex()==1) {			// Teachers
						if(!filter) 
							idView = teacherM.getId(tableTeacher.getSelectedRow());
						else 
							idView = teacherM.getId(tableTeacher.getSelectedRow(), filterTeacher);
						if(idView == -1)
							JOptionPane.showMessageDialog(frame, "Select entry row for viewing first.");
						else 
							JOptionPane.showMessageDialog(null, teacherM.viewDetail(idView));
					}
					if(tabbedPane.getSelectedIndex()==2) {			// Subjects
						if(!filter) 
							idView = subjectM.getId(tableSubject.getSelectedRow());
						else 
							idView = subjectM.getId(tableSubject.getSelectedRow(), filterSubject);
						if(idView == -1)
							JOptionPane.showMessageDialog(frame, "Select entry row for viewing first.");
						else 
							JOptionPane.showMessageDialog(null, subjectM.viewDetail(idView));
					}
					
					if(tabbedPane.getSelectedIndex()==3) {			// Schools
						if(!filter) 
							idView = schoolM.getId(tableSchool.getSelectedRow());
						else 
							idView = schoolM.getId(tableSchool.getSelectedRow(), filterSchool);
						if(idView == -1)
							JOptionPane.showMessageDialog(frame, "Select entry row for viewing first.");
						else 
							JOptionPane.showMessageDialog(null, schoolM.viewDetail(idView));
					}
					if(tabbedPane.getSelectedIndex()==4) {			// Classrooms
						if(!filter) 
							idView = classroomM.getId(tableClassroom.getSelectedRow());
						else 
							idView = classroomM.getId(tableClassroom.getSelectedRow(), filterClassroom);
						if(idView == -1)
							JOptionPane.showMessageDialog(frame, "Select entry row for viewing first.");
						else 
							JOptionPane.showMessageDialog(null, classroomM.viewDetail(idView));
					}
					if(tabbedPane.getSelectedIndex()==5) {			// Exams
						if(!filter) 
							idView = examM.getId(tableExam.getSelectedRow());
						else 
							idView = examM.getId(tableExam.getSelectedRow(), filterExam);
						if(idView == -1)
							JOptionPane.showMessageDialog(frame, "Select entry row for viewing first.");
						else 
							JOptionPane.showMessageDialog(null, examM.viewDetail(idView));
					}
				} catch (HeadlessException | SQLException e2) {
					e2.printStackTrace();
				}
			}
		});
		btnViewDetails.setBounds(430, 532, 130, 50);
		frame.getContentPane().add(btnViewDetails);
	}
	
	private Student getStudentData() {
		Student student = new Student();
		student.setName(txtFirstName.getText());
		student.setSurname(txtSurname.getText());
		student.setEmail(txtEmailAddress.getText());
		return student;
	}
	
	private Teacher getTeacherData() {
		Teacher teacher = new Teacher();
		teacher.setClassId(textFKclass.getText());
		teacher.setName(textName.getText());
		teacher.setSurname(textSurname.getText());
		teacher.setEmail(textEmail.getText());
		teacher.setBirthDate(txtBirthDate.getText());
		teacher.setSalary(txtSalary.getText());
		return teacher;
	}
	
	private Subject getSubjectData() {
		Subject subject = new Subject();
		subject.setType_id(textFKtype.getText());
		subject.setName(textNameSubject.getText());
		return subject;
	}
	
	private School getSchoolData() {
		School school = new School();
		school.setName(textNameSchool.getText());
		school.setAddress(textAddress.getText());
		school.setPhone(textPhone.getText());
		return school;
	}
	
	private Classroom getClassroomData() {
		Classroom classroom = new Classroom();
		classroom.setSchoolId(textFKschool.getText());
		classroom.setName(textNameClass.getText());
		classroom.setCapacity(textCapacity.getText());
		return classroom;
	}
	
	private Exam getExamData() {
		Exam exam = new Exam();
		exam.setSubjId(textFKsubject.getText());
		exam.setContent(textContent.getText());
		return exam;
	}
	
	private Result getResultData() {
		Result result = new Result();
		result.setExamId(textFKexam.getText());
		result.setMark(textMark.getText());
		return result;
	}
	
	private studies getStudiesData() {
		studies studie = new studies();
		studie.setStudent_id(textFKstudent.getText());
		studie.setSubject_id(textFKstudiesSubject.getText());
		studie.setDateTo(textDateTo.getText());
		studie.setGrade(textGrade.getText());
		return studie;
	}

	private teaches getTeachesData() {
		teaches teache = new teaches();
		teache.setTeacher_id(textFKteacher.getText());
		teache.setSubject_id(textFKteachesSubject.getText());
		teache.setDateFrom(textDateFrom.getText());
		return teache;
	}
}