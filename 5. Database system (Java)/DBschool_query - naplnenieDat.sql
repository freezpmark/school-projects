CREATE SEQUENCE school_id_seq START 100001;
CREATE TABLE School
(
  id integer NOT NULL DEFAULT nextval('school_id_seq'),
  name character varying(50),
  address character varying(50),
  phone character varying(100),
  CONSTRAINT school_id_pk PRIMARY KEY (id) 
);

CREATE SEQUENCE classroom_id_seq START 100001;
CREATE TABLE Classroom
(
  id integer NOT NULL DEFAULT nextval('classroom_id_seq'),
  school_id integer,
  capacity integer,
  name character varying(20),
  CONSTRAINT classroom_id_pk PRIMARY KEY (id),
  CONSTRAINT school_id_fk FOREIGN KEY (school_id)
      REFERENCES School (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE teacher_id_seq START 100001;
CREATE TABLE Teacher
(
  id integer NOT NULL DEFAULT nextval('teacher_id_seq'),
  classroom_id integer,
  name character varying(30),
  surname character varying(30),
  birthDate date,
  email character varying(50),
  salary integer,
  CONSTRAINT teacher_id_pk PRIMARY KEY (id),
  CONSTRAINT classroom_id_fk FOREIGN KEY (classroom_id)
      REFERENCES Classroom (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE student_id_seq START 1000001;
CREATE TABLE Student
(
  id integer NOT NULL DEFAULT nextval('student_id_seq'),
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

CREATE SEQUENCE subject_id_seq START 1001;
CREATE TABLE Subject
(
  id integer NOT NULL DEFAULT nextval('subject_id_seq'),
  type_id integer,
  name character varying(100),
  description text,
  CONSTRAINT subject_id_pk PRIMARY KEY (id),
  CONSTRAINT type_id_fk FOREIGN KEY (type_id)
	REFERENCES Type_of_discipline (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE exam_id_seq START 10001;
CREATE TABLE Exam
(
  id integer NOT NULL DEFAULT nextval('exam_id_seq'),
  subject_id integer,
  content character varying(75),
  CONSTRAINT exam_id_pk PRIMARY KEY (id),
  CONSTRAINT subject_id_fk FOREIGN KEY (subject_id)
	REFERENCES Subject (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE result_id_seq START 500001;
CREATE TABLE Result
(
  id integer NOT NULL DEFAULT nextval('result_id_seq'),
  exam_id integer,
  mark integer,
  CONSTRAINT result_id_pk PRIMARY KEY (id),
  CONSTRAINT exam_id_fk FOREIGN KEY (exam_id)
	REFERENCES Exam (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE studies
(
  student_id integer,
  subject_id integer,
  dateTo date,
  grade integer,
  CONSTRAINT student_id_fk FOREIGN KEY (student_id)
	REFERENCES Student (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT subject_id_fk FOREIGN KEY (subject_id)
	REFERENCES Subject (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE TABLE teaches
(
  teacher_id integer,
  subject_id integer,
  dateFrom date,
  CONSTRAINT teacher_id_fk FOREIGN KEY (teacher_id)
	REFERENCES Student (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT subject_id_fk FOREIGN KEY (subject_id)
	REFERENCES Subject (id) MATCH SIMPLE
	ON UPDATE NO ACTION ON DELETE CASCADE
);


COPY Type_of_discipline ( id, disc )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\disc.csv' DELIMITER ',' CSV HEADER;

COPY Subject ( id, type_id, name, description )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\subject.csv' DELIMITER ';' CSV HEADER;

COPY School ( id, name, address, phone )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\school.csv' DELIMITER ',' CSV HEADER;

COPY Classroom ( id, school_id, capacity, name )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\classroom.csv' DELIMITER ',' CSV HEADER;

COPY Teacher ( id, classroom_id, name, surname, birthDate, email, salary )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\teacher.csv' DELIMITER ',' CSV HEADER;

COPY Student ( id, name, surname, email )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\student.csv' DELIMITER ',' CSV HEADER;

COPY Exam ( id, subject_id, content )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\exam.csv' DELIMITER ',' CSV HEADER;

COPY Result ( id, exam_id, mark )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\result.csv' DELIMITER ',' CSV HEADER;

COPY studies ( student_id, subject_id, dateTo, grade )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\studies.csv' DELIMITER ',' CSV HEADER;

COPY teaches ( teacher_id, subject_id, dateFrom )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\teaches.csv' DELIMITER ',' CSV HEADER;
