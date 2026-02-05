SELECT setval(
               pg_get_serial_sequence('employees', 'employee_id'),
               (SELECT MAX(employee_id) FROM employee)
       );

ALTER TABLE audit
ALTER COLUMN id
ADD GENERATED ALWAYS AS IDENTITY;
