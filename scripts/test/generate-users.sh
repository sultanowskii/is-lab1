#!/bin/bash

function create_user {
    name=$1
    password=$2

    curl -X 'POST' \
    'http://localhost:8080/auth/signup' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d "{
    \"username\": \"${name}\",
    \"password\": \"${password}\",
    \"userType\": \"USER\"
    }"
}

function create_admin {
    name=$1
    password=$2

    curl -X 'POST' \
    'http://localhost:8080/auth/signup' \
    -H 'accept: */*' \
    -H 'Content-Type: application/json' \
    -d "{
    \"username\": \"${name}\",
    \"password\": \"${password}\",
    \"userType\": \"ADMIN\"
    }"
}

create_user "user" "user"
for i in {0..100}; do
    create_user "user${i}" "password"
done

create_admin "admin" "admin"
for i in {0..5}; do
    create_admin "admin${i}" "password"
done
