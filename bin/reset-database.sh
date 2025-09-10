#!/bin/bash

psql -h localhost -U postgres -c "DROP DATABASE IF EXISTS my_finances_db"
psql -h localhost -U postgres -c "CREATE DATABASE my_finances_db"