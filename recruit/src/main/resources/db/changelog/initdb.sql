CREATE DATABASE recruitdb;
CREATE USER recruitadmin WITH PASSWORD Fender1980;
GRANT ALL PRIVILEGES ON SCHEMA recruitdb TO recruitadmin;

CREATE SCHEMA recruiter;
CREATE SCHEMA candidate;
CREATE SCHEMA users;
CREATE SCHEMA curriculum;
CREATE SCHEMA listings;

GRANT ALL PRIVILEGES ON SCHEMA recruiter 	TO recruitadmin;
GRANT ALL PRIVILEGES ON SCHEMA candidate 	TO recruitadmin;
GRANT ALL PRIVILEGES ON SCHEMA users 	 	TO recruitadmin;
GRANT ALL PRIVILEGES ON SCHEMA curriculum 	TO recruitadmin;
GRANT ALL PRIVILEGES ON SCHEMA listings 	TO recruitadmin;

