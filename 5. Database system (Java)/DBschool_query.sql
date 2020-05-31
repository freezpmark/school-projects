CREATE TABLE School
(
  id integer NOT NULL,
  name character varying(50),
  address character varying(50),
  phone character varying(100),
  CONSTRAINT school_id_pk PRIMARY KEY (id)
);

CREATE TABLE Classroom
(
  id integer NOT NULL,
  school_id integer,
  capacity integer,
  name character varying(20),
  CONSTRAINT classroom_id_pk PRIMARY KEY (id),
  CONSTRAINT school_id_fk FOREIGN KEY (school_id)
      REFERENCES School (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE Teacher
(
  id integer NOT NULL,
  classroom_id integer,
  name character varying(30),
  surname character varying(30),
  "birthDate" date,
  email character varying(50),
  salary integer,
  CONSTRAINT teacher_id_pk PRIMARY KEY (id),
  CONSTRAINT classroom_id_fk FOREIGN KEY (classroom_id)
      REFERENCES Classroom (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE Student
(
  id integer NOT NULL,
  name character varying(30),
  surname character varying(30),
  email character varying(50),
  CONSTRAINT student_id_pk PRIMARY KEY (id)
);

CREATE TYPE disciplineType AS ENUM ('technical', 'natural', 'medical', 'philosophical', 'artistic', 'pedagogic', 'economic');
CREATE TABLE Type_of_discipline
(
  id integer NOT NULL,
  disc disciplineType,
  CONSTRAINT discipline_id_pk PRIMARY KEY (id)
);

CREATE TABLE Subject
(
  id integer NOT NULL,
  type_id integer,
  name character varying(100),
  description text,
  CONSTRAINT subject_id_pk PRIMARY KEY (id),
  CONSTRAINT type_id_fk FOREIGN KEY (type_id)
	REFERENCES Type_of_discipline (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE Exam
(
  id integer NOT NULL,
  subject_id integer,
  content character varying(75),
  CONSTRAINT exam_id_pk PRIMARY KEY (id),
  CONSTRAINT subject_id_fk FOREIGN KEY (subject_id)
	REFERENCES Subject (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION  
);

CREATE TABLE Result
(
  id integer NOT NULL,
  exam_id integer,
  mark integer,
  CONSTRAINT result_id_pk PRIMARY KEY (id),
  CONSTRAINT exam_id_fk FOREIGN KEY (exam_id)
	REFERENCES Exam (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION
);


CREATE TABLE studies
(
  student_id integer,
  subject_id integer,
  "dateTo" date,
  grade integer,
  CONSTRAINT student_id_fk FOREIGN KEY (student_id)
	REFERENCES Student (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT subject_id_fk FOREIGN KEY (subject_id)
	REFERENCES Subject (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE teaches
(
  teacher_id integer,
  subject_id integer,
  "dateFrom" date,
  CONSTRAINT teacher_id_fk FOREIGN KEY (teacher_id)
	REFERENCES Student (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT subject_id_fk FOREIGN KEY (subject_id)
	REFERENCES Subject (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE NO ACTION
);






