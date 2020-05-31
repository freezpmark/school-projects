Naplnenie dát

COPY Type_of_discipline ( id, disc )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\disc.csv' DELIMITER ',' CSV HEADER;

COPY Subject ( id, type_id, name, description )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\subject.csv' DELIMITER ';' CSV HEADER;

COPY School ( id, name, address, phone )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\school.csv' DELIMITER ',' CSV HEADER;

COPY Classroom ( id, school_id, capacity, name )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\classroom.csv' DELIMITER ',' CSV HEADER;

COPY Teacher ( id, classroom_id, name, surname, "birthDate", email, salary )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\teacher.csv' DELIMITER ',' CSV HEADER;

COPY Student ( id, name, surname, email )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\student.csv' DELIMITER ',' CSV HEADER;

COPY Exam ( id, subject_id, content )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\exam.csv' DELIMITER ',' CSV HEADER;

COPY Result ( id, exam_id, mark )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\result.csv' DELIMITER ',' CSV HEADER;

COPY studies ( student_id, subject_id, "dateTo", grade )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\studies.csv' DELIMITER ',' CSV HEADER;

COPY teaches ( teacher_id, subject_id, "dateFrom" )
FROM 'C:\Users\GLaDOS\PycharmProjects\untitled1\teaches.csv' DELIMITER ',' CSV HEADER;








Skripty pre nominálne atribúty: 

Poèetnos záznamov

SELECT name, COUNT(name) AS NumOccurrences
FROM student
GROUP BY name
HAVING ( COUNT(name) > 1 )
ORDER BY NumOccurrences

Poèet opakovávaných záznamov 

SELECT NumOccurrences, COUNT(*) as ct_ct
FROM(SELECT COUNT(email) AS NumOccurrences
FROM teacher
GROUP BY email
HAVING ( COUNT(email) > 0 )
ORDER BY NumOccurrences
) a GROUP BY 1
ORDER BY 1;

