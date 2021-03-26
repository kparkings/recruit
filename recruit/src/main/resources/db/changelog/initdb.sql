CREATE DATABASE recruitdb;
CREATE USER recruitadmin WITH PASSWORD Fender1980;
GRANT ALL PRIVILEGES ON SCHEMA recruitdb TO recruitadmin;

CREATE SCHEMA recruiter;
CREATE SCHEMA candidate;

GRANT ALL PRIVILLEGES ON SCHEMA recruiter TO recruitadmin;
GRANT ALL PRIVILLEGES ON SCHEMA candidate TO recruitadmin;
