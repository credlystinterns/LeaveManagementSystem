SELECT setval(
               pg_get_serial_sequence('employees', 'employee_id'),
               (SELECT MAX(employee_id) FROM employee)
       );
