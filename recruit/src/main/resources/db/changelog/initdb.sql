CREATE DATABASE recruitdb;
CREATE USER recruitadmin WITH PASSWORD Fender1980;
GRANT ALL PRIVILEGES ON SCHEMA recruitdb TO recruitadmin;

CREATE SCHEMA recruiter;
CREATE SCHEMA candidate;
CREATE SCHEMA users;

GRANT ALL PRIVILEGES ON SCHEMA recruiter TO recruitadmin;
GRANT ALL PRIVILEGES ON SCHEMA candidate TO recruitadmin;
GRANT ALL PRIVILEGES ON SCHEMA users 	 TO recruitadmin;
